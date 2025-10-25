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
    private final ObjectMapper objectMapper;

    public PersonaListener(PersonaService personaService, ObjectMapper objectMapper) {
        this.personaService = personaService;
        this.objectMapper = objectMapper;
    }

    /**
     * Escucha la cola de usuarios definida en RabbitMQConfig.
     * El mensaje llega en formato JSON, se convierte en PersonaRequest y se guarda.
     */
    @RabbitListener(queues = RabbitMQConfig.USUARIO_QUEUE)
    public void recibirPersona(String message) {
        try {
            // 🟢 Convertir el mensaje JSON en un objeto PersonaRequest
            PersonaRequest personaRequest = objectMapper.readValue(message, PersonaRequest.class);

            // 🟢 Guardar o actualizar la persona en la base de datos local
            personaService.guardarPersona(personaRequest);

            System.out.println("✅ Persona procesada correctamente desde RabbitMQ: " + personaRequest.email());
        } catch (Exception e) {
            System.err.println("❌ Error procesando mensaje de Persona: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
