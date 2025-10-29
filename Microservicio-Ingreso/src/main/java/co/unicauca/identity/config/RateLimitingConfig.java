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
 * Configuración de Rate Limiting para protección contra ataques de fuerza bruta
 */
@Configuration
@Slf4j
public class RateLimitingConfig {

    @Value("${app.rate-limiting.enabled:true}")
    private boolean enabled;

    @Value("${app.rate-limiting.capacity:10}")
    private int capacity;

    @Value("${app.rate-limiting.refill-tokens:5}")
    private int refillTokens;

    @Value("${app.rate-limiting.refill-duration:1}")
    private int refillDuration; // en minutos

    @Bean
    public OncePerRequestFilter rateLimitingFilter() {
        return new OncePerRequestFilter() {
            private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

            @Override
            protected void doFilterInternal(HttpServletRequest request,
                                            jakarta.servlet.http.HttpServletResponse response,
                                            jakarta.servlet.FilterChain filterChain)
                    throws jakarta.servlet.ServletException, IOException {

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

            private Bucket createNewBucket(String key) {
                Refill refill = Refill.greedy(refillTokens, Duration.ofMinutes(refillDuration));
                Bandwidth limit = Bandwidth.classic(capacity, refill);
                return Bucket.builder().addLimit(limit).build();
            }

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