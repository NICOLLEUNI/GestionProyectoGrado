package co.unicauca.identity.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // ✅ MISMA COLA que el microservicio de Notificación
    public static final String USUARIO_QUEUE = "usuario.queue";

    /**
     * Declara la MISMA COLA que el microservicio de Notificación
     * Esto asegura que ambos microservicios trabajen con la misma cola
     */
    @Bean
    public Queue usuarioQueue() {
        return new Queue(USUARIO_QUEUE, true); // durable: true
    }

    /**
     * Convierte automáticamente los mensajes JSON a objetos Java (y viceversa)
     * MISMO convertidor que el microservicio de Notificación
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Configura RabbitTemplate para usar JSON
     * Compatible con la estructura del microservicio de Notificación
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());

        // Configuración adicional para robustez
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                System.out.println("✅ Mensaje entregado a RabbitMQ correctamente");
            } else {
                System.err.println("❌ Error enviando mensaje a RabbitMQ: " + cause);
            }
        });

        return rabbitTemplate;
    }
}