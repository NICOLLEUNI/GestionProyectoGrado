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

    /**
     * Publica FormatoAVersion a la cola correspondiente
     */
    public void publishFormatoAVersionCreada(Object formatoVersionResponse) {
        try {
            logger.info("📤 Enviando FormatoAVersion a RabbitMQ: {}", formatoVersionResponse);
            rabbitTemplate.convertAndSend(RabbitMQConfig.COLA_FORMATO_A_VERSION, formatoVersionResponse);
            logger.info("✅ FormatoAVersion enviada a cola: {}", RabbitMQConfig.COLA_FORMATO_A_VERSION);
        } catch (Exception e) {
            logger.error("❌ Error enviando FormatoAVersion a RabbitMQ: {}", e.getMessage());
        }
    }

    /**
     * Publica Anteproyecto a la cola correspondiente
     */
    public void publishAnteproyectoCreado(Object anteproyectoResponse) {
        try {
            logger.info("📤 Enviando Anteproyecto a RabbitMQ: {}", anteproyectoResponse);
            rabbitTemplate.convertAndSend(RabbitMQConfig.COLA_ANTEPROYECTO, anteproyectoResponse);
            logger.info("✅ Anteproyecto enviado a cola: {}", RabbitMQConfig.COLA_ANTEPROYECTO);
        } catch (Exception e) {
            logger.error("❌ Error enviando Anteproyecto a RabbitMQ: {}", e.getMessage());
        }
    }

    /**
     * Publica ProyectoGrado a la cola correspondiente
     */
    public void publishProyectoGradoCreado(Object proyectoResponse) {
        try {
            logger.info("📤 Enviando ProyectoGrado a RabbitMQ: {}", proyectoResponse);
            rabbitTemplate.convertAndSend(RabbitMQConfig.COLA_PROYECTO_GRADO, proyectoResponse);
            logger.info("✅ ProyectoGrado enviado a cola: {}", RabbitMQConfig.COLA_PROYECTO_GRADO);
        } catch (Exception e) {
            logger.error("❌ Error enviando ProyectoGrado a RabbitMQ: {}", e.getMessage());
        }
    }

    /**
     * Publica FormatoA a la cola correspondiente
     */
    public void publishFormatoACreado(Object formatoAResponse) {
        try {
            logger.info("📤 Enviando FormatoA a RabbitMQ: {}", formatoAResponse);
            rabbitTemplate.convertAndSend(RabbitMQConfig.COLA_FORMATO_A, formatoAResponse);
            logger.info("✅ FormatoA enviado a cola: {}", RabbitMQConfig.COLA_FORMATO_A);
        } catch (Exception e) {
            logger.error("❌ Error enviando FormatoA a RabbitMQ: {}", e.getMessage());
        }
    }

    /**
     * Publica Usuario a la cola correspondiente
     */

    }
