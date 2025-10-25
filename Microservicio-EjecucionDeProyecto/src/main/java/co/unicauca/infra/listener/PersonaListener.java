package co.unicauca.infra.listener;

import co.unicauca.entity.PersonaEntity;
import co.unicauca.infra.config.RabbitMQConfig;
import co.unicauca.repository.PersonaRepository;
import co.unicauca.infra.dto.PersonaResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class PersonaListener {


    private PersonaRepository personaRepository;

    @RabbitListener(queues = RabbitMQConfig.USUARIO_QUEUE) // Cola donde llega el mensaje de la creación de Persona
    public void handlePersonaResponse(PersonaResponse response) {
        // Crear una nueva PersonaEntity con los datos del response
        PersonaEntity persona = new PersonaEntity();
        persona.setId(Long.parseLong(response.id()));
        persona.setName(response.name());
        persona.setLastname(response.lastname());
        persona.setEmail(response.email());
        persona.setDepartment(response.department());
        persona.setRoles(response.roles());

        // Guardar la Persona en la base de datos
        personaRepository.save(persona);
    }
}
