package co.unicauca.infrastructure.adapters.input.Listener;

import co.unicauca.application.ports.input.PersonaInPort;
import co.unicauca.infrastructure.config.RabbitMQConfig;
import co.unicauca.infrastructure.dto.request.PersonaRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class PersonaListener {

    private final PersonaInPort personaInputPort;

    public PersonaListener(PersonaInPort personaInputPort) {
        this.personaInputPort = personaInputPort;
    }

    @RabbitListener(queues = RabbitMQConfig.AUDITORIA_QUEUE)
    public void recibirPersona(PersonaRequest personaRequest) {
        try {
            System.out.println("✅ PERSONA RECIBIDA DIRECTAMENTE: " + personaRequest.email());

            // Guardar la persona usando el PUERTO de entrada
            personaInputPort.guardarPersona(personaRequest);

            System.out.println("✅ Persona procesada correctamente desde RabbitMQ: " + personaRequest.email());
        } catch (Exception e) {
            System.err.println("❌ Error procesando Persona: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
