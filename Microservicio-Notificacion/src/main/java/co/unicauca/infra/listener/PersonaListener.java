package co.unicauca.infra.listener;

import co.unicauca.infra.config.RabbitMQConfig;
import co.unicauca.infra.dto.PersonaRequest;
import co.unicauca.service.PersonaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class PersonaListener {

    private final PersonaService personaService;

    public PersonaListener(PersonaService personaService) {
        this.personaService = personaService;
    }

    /**
     * : Recibir PersonaRequest directamente, NO String
     */
    @RabbitListener(queues = RabbitMQConfig.USUARIO_QUEUE)
    public void recibirPersona(PersonaRequest personaRequest) {
        try {
            System.out.println("✅ PERSONA RECIBIDA DIRECTAMENTE: " + personaRequest.email());

            // Guardar la persona
            personaService.guardarPersona(personaRequest);

            System.out.println("✅ Persona procesada correctamente desde RabbitMQ: " + personaRequest.email());
        } catch (Exception e) {
            System.err.println("❌ Error procesando Persona: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
