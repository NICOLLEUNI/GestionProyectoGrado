package co.unicauca.identity.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Configuración de Rate Limiting para proteger los endpoints de autenticación.
 *
 * Esta clase implementa un filtro que limita cuántas solicitudes puede hacer una misma IP
 * hacia /api/auth/login y /api/auth/register en un intervalo determinado.
 *
 * El objetivo es prevenir:
 *  - Ataques de fuerza bruta al login
 *  - Bots creando cuentas de forma masiva
 *  - Envío excesivo de peticiones desde una misma IP
 *
 * Se implementa usando Bucket4j, que maneja “baldes de tokens”.
 * Cada IP tiene su propio bucket: cada solicitud consume 1 token.

 */
@Configuration
@Slf4j
public class RateLimitingConfig {


    // Activa o desactiva el rate limiting mediante application.properties
    @Value("${app.rate-limiting.enabled:true}")
    private boolean enabled;

    // Cantidad máxima de solicitudes permitidas en el bucket por IP
    @Value("${app.rate-limiting.capacity:10}")
    private int capacity;
    // Número de tokens que se agregan en cada intervalo de tiempo
    @Value("${app.rate-limiting.refill-tokens:5}")
    private int refillTokens;

    // Cada cuántos minutos se recargan los tokens
    @Value("${app.rate-limiting.refill-duration:1}")
    private int refillDuration; // en minutos


    /**
     * Filtro que se ejecuta en cada request para aplicar el rate limiting.
     *
     * OncePerRequestFilter garantiza que cada solicitud pase por aquí una sola vez.
     */
    @Bean
    public OncePerRequestFilter rateLimitingFilter() {
        return new OncePerRequestFilter() {
            private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();


            // Se almacena un bucket por cada IP que haga solicitudes
            @Override
            protected void doFilterInternal(HttpServletRequest request,
                                            jakarta.servlet.http.HttpServletResponse response,
                                            jakarta.servlet.FilterChain filterChain)
                    throws jakarta.servlet.ServletException, IOException {

                // Si está desactivado, simplemente continúa la cadena de filtros
                if (!enabled) {
                    filterChain.doFilter(request, response);
                    return;
                }

                // Solo aplicar rate limiting a endpoints de autenticación
                String path = request.getRequestURI();
                if (!path.equals("/api/auth/login") && !path.equals("/api/auth/register")) {
                    filterChain.doFilter(request, response);
                    return;
                }

                String clientIp = getClientIp(request);
                // Obtiene el bucket asociado a la IP o crea uno nuevo
                Bucket bucket = buckets.computeIfAbsent(clientIp, this::createNewBucket);

                if (bucket.tryConsume(1)) {
                    filterChain.doFilter(request, response);
                } else {
                    log.warn("Rate limit excedido para IP: {}", clientIp);
                    response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                    response.setContentType("application/json");
                    response.getWriter().write("""
                        {
                            "success": false,
                            "message": "Demasiadas solicitudes. Por favor, intente nuevamente en unos minutos."
                        }
                        """);
                }
            }

            /**
             * Crea un nuevo bucket con la capacidad y reglas de recarga configuradas.
             */
            private Bucket createNewBucket(String key) {
                Refill refill = Refill.greedy(refillTokens, Duration.ofMinutes(refillDuration));
                Bandwidth limit = Bandwidth.classic(capacity, refill);
                return Bucket.builder().addLimit(limit).build();
            }


            /**
             * Obtiene la IP del cliente revisando cabeceras comunes
             * en caso de que haya un proxy o balanceador.
             */
            private String getClientIp(HttpServletRequest request) {
                String ip = request.getHeader("X-Forwarded-For");
                if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("Proxy-Client-IP");
                }
                if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("WL-Proxy-Client-IP");
                }
                if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                }
                return ip;
            }
        };
    }
}