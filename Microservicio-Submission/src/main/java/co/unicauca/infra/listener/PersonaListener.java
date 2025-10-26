package co.unicauca.infra.listener;

import co.unicauca.infra.config.RabbitMQConfig;
import co.unicauca.infra.dto.PersonaRequest;
import co.unicauca.service.PersonaService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class PersonaListener {

    private final PersonaService personaService;

    public PersonaListener(PersonaService personaService) {
        this.personaService = personaService;
    }

    /**
     * Escucha actualizaciones de usuarios para mantener cache local
     */
    @RabbitListener(queues = RabbitMQConfig.USUARIO_QUEUE)
    public void recibirUsuarioActualizado(PersonaRequest persona) {
        System.out.println("📥 Recibida actualización de usuario: " + persona.email());
        personaService.guardarPersona(persona);
    }

}
