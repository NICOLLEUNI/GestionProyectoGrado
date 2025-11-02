package co.unicauca.identity.security;

import co.unicauca.identity.entity.Persona;
import co.unicauca.identity.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Proveedor de tokens JWT para autenticación - Factory Pattern
 * SINGLE_TABLE: Adaptado para la entidad Persona única
 */
@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret:default-secret-key-change-in-production-minimum-32-characters}")
    private String jwtSecret;

    @Value("${jwt.expiration:3600000}")
    private long jwtExpirationInMs;

    /**
     * Genera un token JWT para una persona autenticada
     * SINGLE_TABLE: Usa campos directos de Persona sin casting
     */

    /**
     * Genera un token JWT para una persona autenticada
     */
    public String generateToken(Persona persona) {
        try {
            log.debug("Generando token para usuario: {}", persona.getEmail());

            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

            // ✅ VERIFICAR: Log de los claims que se incluirán
            log.debug("Claims - Email: {}, UserId: {}, Roles: {}",
                    persona.getEmail(), persona.getIdUsuario(), persona.getRoles());

            String token = Jwts.builder()
                    .subject(persona.getEmail())
                    .claim("userId", persona.getIdUsuario())
                    .claim("roles", persona.getRoles().stream()
                            .map(Enum::name)
                            .collect(Collectors.toList()))
                    .claim("programa", extractPrograma(persona))
                    .claim("departamento", extractDepartamento(persona))
                    .claim("name", persona.getName())
                    .claim("lastname", persona.getLastname())
                    .issuedAt(now)
                    .expiration(expiryDate)
                    .signWith(getSigningKey())
                    .compact();

            log.debug("Token generado exitosamente para: {}", persona.getEmail());
            return token;

        } catch (Exception e) {
            log.error("Error al generar token JWT para usuario {}: {}",
                    persona.getEmail(), e.getMessage(), e);
            throw new RuntimeException("Error al generar token JWT", e);
        }
    }
    /**
     * Valida un token JWT
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException e) {
            log.error("Firma JWT inválida: {}", e.getMessage());
            throw new InvalidTokenException("Firma del token inválida");
        } catch (MalformedJwtException e) {
            log.error("Token JWT malformado: {}", e.getMessage());
            throw new InvalidTokenException("Token malformado");
        } catch (ExpiredJwtException e) {
            log.error("Token JWT expirado: {}", e.getMessage());
            throw new InvalidTokenException("Token expirado");
        } catch (UnsupportedJwtException e) {
            log.error("Token JWT no soportado: {}", e.getMessage());
            throw new InvalidTokenException("Token no soportado");
        } catch (IllegalArgumentException e) {
            log.error("Claims vacías en el token JWT: {}", e.getMessage());
            throw new InvalidTokenException("Token vacío");
        } catch (JwtException e) {
            log.error("Error JWT general: {}", e.getMessage());
            throw new InvalidTokenException("Token inválido");
        }
    }

    /**
     * Extrae el email del usuario del token JWT
     */
    public String getUserEmailFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.getSubject();
    }

    /**
     * Extrae el ID de usuario del token JWT
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("userId", Long.class);
    }

    /**
     * Extrae los roles del usuario del token JWT
     */
    @SuppressWarnings("unchecked")
    public java.util.List<String> getUserRolesFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("roles", java.util.List.class);
    }

    /**
     * Extrae todas las claims del token JWT
     */
    public Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            // En caso de token expirado, aún podemos leer las claims
            log.warn("Token expirado pero claims legibles");
            return e.getClaims();
        }
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * SINGLE_TABLE: Extrae programa directamente de Persona
     */
    private String extractPrograma(Persona persona) {
        return persona.getPrograma() != null ? persona.getPrograma().name() : null;
    }

    /**
     * SINGLE_TABLE: Extrae departamento directamente de Persona
     */
    private String extractDepartamento(Persona persona) {
        return persona.getDepartamento() != null ? persona.getDepartamento().name() : null;
    }

    /**
     * Verifica si el token está a punto de expirar (útil para refresh)
     */
    public boolean isTokenExpiringSoon(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            Date now = new Date();
            long timeUntilExpiration = expiration.getTime() - now.getTime();

            // Considerar "pronto" si expira en menos de 5 minutos
            return timeUntilExpiration < (5 * 60 * 1000);
        } catch (Exception e) {
            log.warn("No se pudo verificar expiración del token: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene el tiempo restante hasta la expiración del token
     */
    public long getTimeUntilExpiration(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            Date now = new Date();
            return expiration.getTime() - now.getTime();
        } catch (Exception e) {
            log.warn("No se pudo obtener tiempo de expiración: {}", e.getMessage());
            return -1;
        }
    }
}