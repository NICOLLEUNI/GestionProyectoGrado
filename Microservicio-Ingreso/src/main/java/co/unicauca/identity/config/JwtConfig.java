package co.unicauca.identity.config;
import jakarta.annotation.PostConstruct;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuración centralizada para las propiedades del sistema de autenticación JWT.
 *
 * Esta clase permite cargar, validar y exponer los valores esenciales del JWT,
 * tales como:
 *  - La clave secreta usada para firmar los tokens.
 *  - El tiempo de expiración asignado a cada token.
 *
 * Los valores se obtienen desde el archivo application.properties o application.yml
 * mediante el uso de @ConfigurationProperties, lo que facilita modificar estos valores
 * sin tener que recompilar la aplicación. Esta configuración es utilizada por la clase
 * JwtTokenProvider para generar y validar tokens de forma manual, cumpliendo con el
 * requerimiento del proyecto de gestionar los JWT directamente desde la aplicación.
 */
@Component
@ConfigurationProperties(prefix = "jwt")

public class JwtConfig {
    // ✅ AGREGAR LOGGER TRADICIONAL
    private static final Logger log = LoggerFactory.getLogger(JwtConfig.class);
    private String secret = "default-secret-key-change-in-production-minimum-32-characters";
    private long expiration = 3600000; // 1 hora en milisegundos

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    // ✅ CORREGIDO: Método para verificar la configuración
    @PostConstruct
    public void validateConfig() {
        log.info("Configuración JWT cargada - Secret: {}, Expiration: {} ms",
                secret != null ? "CARGADO" : "NULL", expiration);

        if (secret == null || secret.trim().isEmpty()) {
            log.error("JWT secret no está configurado");
            throw new IllegalStateException("JWT secret no está configurado");
        }

        if (expiration <= 0) {
            log.error("JWT expiration debe ser mayor a 0");
            throw new IllegalStateException("JWT expiration debe ser mayor a 0");
        }
    }



    // Métodos de utilidad
    public boolean isSecretDefault() {
        return secret.startsWith("default-secret-key");
    }
}