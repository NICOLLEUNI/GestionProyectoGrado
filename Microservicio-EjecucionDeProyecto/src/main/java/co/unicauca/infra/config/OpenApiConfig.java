package co.unicauca.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        Info info = new Info()
                .title("Microservicio Ejecución de Proyecto")
                .version("1.0.0")
                .description("API REST del microservicio encargado de gestionar la ejecución de un proyecto académico. "
                        + "Incluye endpoints para manejar personas, tareas, y otros recursos asociados.")
                .contact(new Contact()
                        .name("Anthony Ortega")
                        .email("anthony.ortega@example.com")
                        .url("https://www.unicauca.edu.co")
                );

        Server localServer = new Server()
                .url("http://localhost:8083")
                .description("Servidor local de desarrollo");

        return new OpenAPI()
                .info(info)
                .addServersItem(localServer);
    }
}

