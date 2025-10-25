package co.unicauca.infra.listener;

import co.unicauca.infra.config.RabbitMQConfig;
import co.unicauca.infra.dto.PersonaResponse;
import co.unicauca.service.PersonaService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class PersonaListener {

    private static final Logger logger = LoggerFactory.getLogger(PersonaListener.class);

    @Autowired
    private PersonaService personaService;

    @RabbitListener(queues = RabbitMQConfig.USUARIO_QUEUE)
    public void handlePersonaResponse(PersonaResponse request) {
        logger.info("Recibido mensaje de persona desde RabbitMQ: {}", request.id());

        // Delegar toda la lógica al service
        personaService.saveInterno(request);
    }
}