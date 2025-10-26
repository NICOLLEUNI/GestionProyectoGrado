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

    // Enviar mensaje directamente a una cola
    private void publish(String queueName, Object message) {
        try {
            rabbitTemplate.convertAndSend(queueName, message);
            System.out.println("✅ Mensaje enviado → " + queueName);
        } catch (Exception e) {
            System.err.println("❌ Error al enviar mensaje: " + e.getMessage());
        }
    }

    public void publishFormatoA(Object formatoAResponse) {
        publish(RabbitMQConfig.FORMATOA_EVALUACION_QUEUE, formatoAResponse);
    }

    // 📤 Publicar evento cuando se evalúa un Formato A (se envía a dos colas)
    public void publishFormatoAEvaluado(Object formatoAResponse) {
        // Enviar a microservicio Submission
        publish(RabbitMQConfig.FORMATOA_EVALUADO_QUEUE, formatoAResponse);
    }

    // 📤 Publicar evento cuando se evalúa un Formato A (se envía a dos colas)
    public void publishFormatoAEvaluadoNotificacion(Object formatoAResponseNotificacion) {
        // Enviar a microservicio Notificación
        publish(RabbitMQConfig.FORMATOA_EVALUADO_NOTIFICATION_QUEUE, formatoAResponseNotificacion);
    }

    public void publishAnteproyecto(Object anteproyectoResponse) {
        publish(RabbitMQConfig.ANTEPROYECTO_EVALUACION_QUEUE, anteproyectoResponse);
    }

    public void publishUsuario(Object usuarioResponse) {
        publish(RabbitMQConfig.USUARIO_QUEUE, usuarioResponse);
    }
}