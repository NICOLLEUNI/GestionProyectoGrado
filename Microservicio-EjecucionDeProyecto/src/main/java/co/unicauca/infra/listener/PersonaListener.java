package co.unicauca.infra.listener;

import co.unicauca.entity.PersonaEntity;
import co.unicauca.infra.dto.PersonaRequest;
import co.unicauca.service.PersonaService;
import co.unicauca.service.mapper.PersonaMapperService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PersonaListener {

    private final PersonaService service;
    private final PersonaMapperService mapper;

    public PersonaListener(PersonaService service, PersonaMapperService mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @RabbitListener(queues = "q.persona.created")
    public void recibirPersona(PersonaRequest request) {

        // 🔹 Mapear DTO a entidad
        PersonaEntity entity = mapper.mapFromRequest(request);

        // 🔹 Persistir (nuevo o actualización)
        service.saveOrUpdate(entity);

        System.out.println("Persona procesada y persistida: " + entity.getEmail());
    }
}
