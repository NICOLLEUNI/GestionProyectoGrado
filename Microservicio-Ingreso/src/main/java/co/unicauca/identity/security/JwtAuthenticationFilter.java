package co.unicauca.identity.security;

import co.unicauca.identity.exception.InvalidTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro que intercepta TODAS las peticiones HTTP para:
 *
 *   • Extraer el token JWT del encabezado "Authorization"
 *   • Validar su firma, integridad y expiración
 *   • Extraer el email asociado al token
 *   • Cargar el usuario desde la base de datos
 *   • Construir el objeto Authentication con sus roles
 *   • Registrar al usuario autenticado en el SecurityContext
 *
 *  Este filtro es un componente crucial porque es el puente entre:
 *      1. El token que viene del cliente
 *      2. Spring Security (autorización de rutas)
 *
 *  Se ejecuta una sola vez por petición (OncePerRequestFilter).
 */

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final PersonaDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider,
                                   PersonaDetailsServiceImpl userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }



    /**
     *    *  MÉTODO PRINCIPAL DEL FILTRO: doFilterInternal
     * Este método:
     *   1. Lee el header Authorization.
     *   2. Extrae el token JWT si existe.
     *   3. Lo valida mediante JwtTokenProvider.
     *   4. Si es válido:
     *         • Obtiene el email del usuario.
     *         • Carga su información desde la base de datos.
     *         • Crea un Authentication con los roles del usuario.
     *         • Lo guarda en el SecurityContext.
     *
     *  Si el token es inválido o expirado → responde con 401.
     *  Si el usuario no existe → responde con 401.
     *
     *  Al final continúa con la cadena de filtros.
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        log.debug(">>> Header Authorization raw: {}", header);

        try {
            // Extrae token
            String jwt = getJwtFromRequest(request);
            log.debug(">>> JWT extraído: {}", jwt);

            if (!StringUtils.hasText(jwt)) {
                log.debug(">>> No hay token en la petición");
                filterChain.doFilter(request, response);
                return;
            }

            // Validar token (puede lanzar InvalidTokenException)
            boolean valid = tokenProvider.validateToken(jwt);
            log.debug(">>> validateToken result: {}", valid);

            // Extraer email desde token
            String userEmail = tokenProvider.getUserEmailFromToken(jwt);
            log.debug(">>> userEmail desde token: {}", userEmail);

            // Cargar UserDetails desde DB
            org.springframework.security.core.userdetails.UserDetails userDetails =
                    userDetailsService.loadUserByUsername(userEmail);
            log.debug(">>> userDetails cargado: username={}, authorities={}",
                    userDetails.getUsername(), userDetails.getAuthorities());

            // construye el objeto de autenticacion
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug(">>> SecurityContext seteado para: {}", userDetails.getUsername());

        } catch (co.unicauca.identity.exception.InvalidTokenException ex) {
            log.warn(">>> InvalidTokenException: {}", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"success\":false,\"message\":\"Token inválido o expirado\"}");
            return;
        } catch (org.springframework.security.core.userdetails.UsernameNotFoundException ex) {
            log.warn(">>> UsernameNotFoundException: {}", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"success\":false,\"message\":\"Usuario no encontrado\"}");
            return;
        } catch (Exception ex) {
            log.error(">>> Error inesperado en JwtAuthenticationFilter", ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"success\":false,\"message\":\"Error interno autenticación\"}");
            return;
        }

        // Continua con la cadena de filtros
        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el token del header Authorization.
     * Ejemplo del Header:
     *    Authorization: Bearer eyJhbGciOi...
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
}

}