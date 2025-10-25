package co.unicauca.service;

import co.unicauca.entity.PersonaEntity;
import co.unicauca.infra.dto.PersonaRequest;
import co.unicauca.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    // Método para mapear y guardar una persona
    public PersonaEntity mapAndSavePersona(PersonaRequest request) {
        // Crear la entidad PersonaEntity a partir del DTO PersonaRequest
        PersonaEntity personaEntity = new PersonaEntity();
        personaEntity.setId(Long.parseLong(request.id()));
        personaEntity.setName(request.name());
        personaEntity.setLastname(request.lastname());
        personaEntity.setEmail(request.email());
        personaEntity.setDepartment(request.department());

        // Convertir el Set de roles de String a Set de roles (puede ser Set<String>)
        Set<String> roles = new HashSet<>(request.roles());
        personaEntity.setRoles(roles);

        // Guardar la persona en la base de datos
        return personaRepository.save(personaEntity);
    }
}
