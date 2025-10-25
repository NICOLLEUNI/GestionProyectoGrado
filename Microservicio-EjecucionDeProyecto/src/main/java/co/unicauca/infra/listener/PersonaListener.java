package co.unicauca.infra.listener;

import co.unicauca.entity.PersonaEntity;
import co.unicauca.infra.config.RabbitMQConfig;
import co.unicauca.infra.dto.PersonaResponse;
import co.unicauca.service.FormatoAService;
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

    public PersonaListener(PersonaService personaService) {
        this.personaService = personaService;
    }

    @RabbitListener(queues = RabbitMQConfig.USUARIO_QUEUE)
    public void handlePersonaResponse(PersonaResponse request) {
        System.out.println("📩 Mensaje recibido en usuario.queue: " + request);
        PersonaEntity persona = personaService.saveInterno(request);
        System.out.println("✅ Formato A guardado con ID: " + persona.getId());


    }
}