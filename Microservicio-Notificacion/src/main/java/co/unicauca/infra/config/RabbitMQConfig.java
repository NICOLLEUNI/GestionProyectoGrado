package co.unicauca.infra.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    // El nombre de la cola debe coincidir con el del microservicio Submission
    public static final String FORMATOA_EVALUADO_NOTIFICATION_QUEUE = "formatoa.evaluado.notification.queue";
    public static final String USUARIO_QUEUE = "usuario.queue";

    // Declara la cola para que Spring pueda escucharla
    @Bean
    public Queue anteproyectoQueue() {
        return new Queue(FORMATOA_EVALUADO_NOTIFICATION_QUEUE, true);
    }
    @Bean
    public Queue usuarioQueue() {
        return new Queue(USUARIO_QUEUE, true);
    }

    // Convierte automáticamente los mensajes JSON a objetos Java (y viceversa)
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
