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
     * Publica un mensaje cuando se crea un Formato A.
     */
    public void publishFormatoACreado(Object formatoAResponse) {
        logger.debug("📤 Enviando mensaje de Formato A creado: {}", formatoAResponse);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.FORMATOA_CREADO_EXCHANGE,
                RabbitMQConfig.FORMATOA_CREADO_ROUTING_KEY,
                formatoAResponse
        );
    }

    /**
     * Publica un mensaje cuando se crea un Anteproyecto.
     */
    public void publishAnteproyectoCreado(Object anteproyectoResponse) {
        logger.debug("📤 Enviando mensaje de Anteproyecto creado: {}", anteproyectoResponse);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ANTEPROYECTO_CREADO_EXCHANGE,
                RabbitMQConfig.ANTEPROYECTO_CREADO_ROUTING_KEY,
                anteproyectoResponse
        );
    }

    /**
     * Publica un mensaje cuando se crea una FormatoAVersion.
     */
    public void publishFormatoAVersionCreada(Object formatoVersionResponse) {
        logger.debug("📤 Enviando mensaje de FormatoAVersion creada: {}", formatoVersionResponse);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.FORMATOAVERSION_CREADA_EXCHANGE,
                RabbitMQConfig.FORMATOAVERSION_CREADA_ROUTING_KEY,
                formatoVersionResponse
        );
    }

    /**
     * Publica un mensaje cuando se crea un Proyecto de Grado.
     */
    public void publishProyectoGradoCreado(Object proyectoResponse) {
        logger.debug("📤 Enviando mensaje de Proyecto de Grado creado: {}", proyectoResponse);
        rabbitTemplate.convertAndSend(RabbitMQConfig.PROYECTO_GRADO_CREADO_QUEUE, proyectoResponse);
    }

    /**
     * Publica un mensaje cuando se crea un Usuario.
     */
    public void publishUsuarioCreado(Object usuarioResponse) {
        logger.debug("📤 Enviando mensaje de Usuario creado: {}", usuarioResponse);
        rabbitTemplate.convertAndSend(RabbitMQConfig.USUARIO_QUEUE, usuarioResponse);
    }
}