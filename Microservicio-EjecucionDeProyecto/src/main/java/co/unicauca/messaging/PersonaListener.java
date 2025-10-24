package co.unicauca.messaging;

import co.unicauca.entity.PersonaEntity;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import co.unicauca.repository.PersonaRepository;
import org.springframework.stereotype.Component;
import co.unicauca.mapper.PersonaMapper;

import co.unicauca.dto.PersonaRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PersonaListener {

    private final PersonaRepository personaRepository;

    @RabbitListener(queues = "q.personas.ejecucion")
    public void onPersonaReceived(PersonaRequest personaRequest) {
        PersonaEntity entity = PersonaMapper.fromRequest(personaRequest);
        personaRepository.save(entity);
    }
}
