package co.unicauca.infra.messaging;

import co.unicauca.infra.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class RabbitMQPublisher {

    private final RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQPublisher.class);

    public RabbitMQPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // Enviar mensaje directamente a una cola
    private void publish(String queueName, Object message) {
        try {
            rabbitTemplate.convertAndSend(queueName, message);
            logger.info("✅ Mensaje enviado a la cola '{}'.", queueName);
        } catch (Exception e) {
            logger.error("❌ Error al enviar mensaje a la cola '{}': {}", queueName, e.getMessage(), e);
            // Aquí podrías agregar lógica de reintentos si es necesario.
        }
    }

    public void publishFormatoA(Object formatoAResponse) {
        logger.debug("Enviando mensaje a formatoA: {}", formatoAResponse);
        publish(RabbitMQConfig.FORMATOA_QUEUE, formatoAResponse);
    }

    public void publishFormatoAEvaluado(Object formatoAResponse) {
        logger.debug("Enviando mensaje a formatoA evaluado: {}", formatoAResponse);
        publish(RabbitMQConfig.FORMATOA_EVALUADO_QUEUE, formatoAResponse);
    }

    public void publishAnteproyecto(Object anteproyectoResponse) {
        logger.debug("Enviando mensaje a anteproyecto: {}", anteproyectoResponse);
        publish(RabbitMQConfig.ANTEPROYECTO_QUEUE, anteproyectoResponse);
    }

    public void publishUsuario(Object usuarioResponse) {
        logger.debug("Enviando mensaje a usuario: {}", usuarioResponse);
        publish(RabbitMQConfig.USUARIO_QUEUE, usuarioResponse);
    }
}
