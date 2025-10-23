package co.unicauca.infra.messaging;

import co.unicauca.infra.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;



@Component
public class RabbitMQPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    private void publish(String exchange, String routingKey, Object message) {
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            System.out.println("✅ Mensaje enviado → " + routingKey);
        } catch (Exception e) {
            System.err.println("❌ Error al enviar mensaje: " + e.getMessage());
        }
    }

    public void publishFormatoA(Object formatoAResponse) {
        publish(
                RabbitMQConfig.EVALUACION_EXCHANGE,
                RabbitMQConfig.FORMATOA_ROUTING_KEY,
                formatoAResponse
        );
    }

    public void publishAnteproyecto(Object anteproyectoResponse) {
        publish(
                RabbitMQConfig.EVALUACION_EXCHANGE,
                RabbitMQConfig.ANTEPROYECTO_ROUTING_KEY,
                anteproyectoResponse
        );
    }
}