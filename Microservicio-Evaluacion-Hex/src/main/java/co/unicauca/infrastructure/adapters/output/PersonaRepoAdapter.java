package co.unicauca.infrastructure.adapters.output;

import co.unicauca.application.ports.output.PersonaRepoOutPort;
import co.unicauca.domain.entities.Persona;
import co.unicauca.infrastructure.adapters.output.persistence.entities.PersonaEntity;
import co.unicauca.infrastructure.adapters.output.persistence.mapper.PersonaMapper;
import co.unicauca.infrastructure.adapters.output.persistence.repository.PersonaJPARepo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PersonaRepoAdapter implements PersonaRepoOutPort {
    // 👆 IMPLEMENTA el contrato del dominio

    private final PersonaJPARepo personaJPARepo;

    public PersonaRepoAdapter(PersonaJPARepo personaJPARepo) {
        this.personaJPARepo = personaJPARepo;
    }

    @Override
    public Persona save(Persona persona) {
        // Convierte Persona(dominio) → PersonaEntity(JPA)
        PersonaEntity entity = PersonaMapper.toEntity(persona);
        // Usa el repositorio JPA para guardar
        PersonaEntity savedEntity = personaJPARepo.save(entity);
        // Convierte de vuelta PersonaEntity(JPA) → Persona(dominio)
        return PersonaMapper.toDomain(savedEntity);
    }

    @Override
    public List<Persona> findAll() {
        return personaJPARepo.findAll()
                .stream()
                .map(PersonaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Persona> findById(Long id) {
        return personaJPARepo.findById(id)
                .map(PersonaMapper::toDomain);
    }

    @Override
    public Optional<Persona> findByEmail(String email) {
        return personaJPARepo.findByEmail(email)
                .map(PersonaMapper::toDomain);
    }

    @Override
    public List<Persona> findByDepartmentIgnoreCase(String department) {
        return personaJPARepo.findByDepartmentIgnoreCase(department)
                .stream()
                .map(PersonaMapper::toDomain)
                .collect(Collectors.toList());
    }
}