package co.unicauca.service;

import co.unicauca.entity.PersonaEntity;
import co.unicauca.infra.dto.PersonaRequest;
import co.unicauca.service.mapper.PersonaMapperService;
import co.unicauca.repository.PersonaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonaService {

    private final PersonaRepository personaRepository;
    private final PersonaMapperService personaMapper;

    public PersonaService(PersonaRepository personaRepository, PersonaMapperService personaMapper) {
        this.personaRepository = personaRepository;
        this.personaMapper = personaMapper;
    }

    /**
     * Guarda una persona nueva o actualiza si ya existe.
     */
    @Transactional
    public PersonaEntity saveOrUpdate(PersonaEntity entity) {
        if (entity.getId() != null) {
            Optional<PersonaEntity> existing = personaRepository.findById(Long.valueOf(entity.getId()));
            if (existing.isPresent()) {
                PersonaEntity actual = existing.get();
                actual.setName(entity.getName());
                actual.setLastname(entity.getLastname());
                actual.setEmail(entity.getEmail());
                actual.setDepartment(entity.getDepartment());
                actual.setRoles(entity.getRoles());
                return personaRepository.save(actual);
            }
        }
        return personaRepository.save(entity);
    }

    /**
     * Guardar directamente desde request
     */
    @Transactional
    public PersonaEntity guardarPersona(PersonaRequest request) {
        PersonaEntity entity = personaMapper.mapFromRequest(request);
        return saveOrUpdate(entity);
    }

    /**
     * Buscar IDs de personas a partir de una lista de emails.
     */
    @Transactional(readOnly = true)
    public List<Long> buscarIdsPorEmails(List<String> emails) {
        return personaRepository.findByEmailIn(emails)
                .stream()
                .map(persona -> Long.valueOf(persona.getId()))
                .collect(Collectors.toList());
    }
}
