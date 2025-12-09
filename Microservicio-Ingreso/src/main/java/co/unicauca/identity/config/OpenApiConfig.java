package co.unicauca.identity.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;



/**
 * Configuración centralizada de OpenAPI (Swagger) para el microservicio de identidad.
 *
 * <p>Este componente define la especificación OpenAPI utilizada para documentar
 * los endpoints del servicio, incluyendo la configuración del esquema de seguridad
 * basado en JWT. Gracias a esta clase, Swagger UI puede interpretar correctamente
 * los tokens y permitir su uso en pruebas interactivas.</p>
 *
 * <p>Además, establece los metadatos principales de la API (título, versión, contacto,
 * licencia) y define los servidores disponibles durante el desarrollo.</p>
 */



@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Identity Service API",
                version = "1.0.0",
                description = "Microservicio de autenticación y gestión de identidad"
        ),
        security = {
                @SecurityRequirement(name = "bearer-key")
        }
)
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    /**
     * Construye la especificación OpenAPI utilizada por Swagger UI.
     *
     * <p>Registra el esquema de seguridad Bearer JWT, configura la URL base del servidor de desarrollo
     * y establece los metadatos principales de la API.</p>
     *
     * @return instancia configurada de {@link OpenAPI}
     */



    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()

                // Aplicación global del esquema de seguridad JWT a las operaciones protegidas
                .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement()
                        .addList("bearer-key")                 // ⬅⬅⬅ AÑADIR ESTO TAMBIÉN
                )
                // Configuración del servidor local para pruebas
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Servidor de Desarrollo")
                ))
                // Registro del esquema de seguridad Bearer JWT
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                )
                // Metadatos adicionales de la API
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Identity Service API")
                        .description("Gestión de identidad y autenticación FIET")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo - FIET")
                                .email("soporte.fiet@unicauca.edu.co")
                                .url("https://fiet.unicauca.edu.co")
                        )
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")
                        )
                );
    }
}