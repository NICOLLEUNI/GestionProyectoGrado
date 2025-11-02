package co.unicauca;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Clase principal del Microservicio de Login y Registro
 * SINGLE_TABLE: Configurado para el modelo Persona único
 */
@SpringBootApplication
//@EnableJpaAuditing
@Slf4j
public class IdentityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdentityServiceApplication.class, args);

        log.info("""
                🚀 Identity Service (Login-Signin) iniciado correctamente
                📚 Documentación API: http://localhost:8080/swagger-ui.html
                🔐 Endpoints: /api/auth/register, /api/auth/login, /api/auth/profile
                💾 Modelo: SINGLE_TABLE (Persona con múltiples roles)
                🗄️  BD: PostgreSQL con Flyway migrations
                👥 Roles: ESTUDIANTE, DOCENTE, COORDINADOR, JEFE_DEPARTAMENTO
                """);
    }
}