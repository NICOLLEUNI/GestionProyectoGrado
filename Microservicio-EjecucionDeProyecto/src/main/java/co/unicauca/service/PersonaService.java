package co.unicauca.service;

import co.unicauca.entity.EnumRol;
import co.unicauca.entity.PersonaEntity;
import co.unicauca.infra.dto.PersonaRequest;
import co.unicauca.infra.dto.PersonaResponse;
import co.unicauca.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;
import java.util.Set;

@Service
public class PersonaService {

    private static final Logger logger = LoggerFactory.getLogger(PersonaService.class);

    @Autowired
    private PersonaRepository personaRepository;

    // Método para mapear y guardar una persona desde un Request
    public PersonaEntity mapAndSavePersona(PersonaRequest request) {
        PersonaEntity personaEntity = new PersonaEntity();
        personaEntity.setId(Long.parseLong(request.id()));
        personaEntity.setName(request.name());
        personaEntity.setLastname(request.lastname());
        personaEntity.setEmail(request.email());
        personaEntity.setDepartment(request.department());
        personaEntity.setRoles(convertirRoles(request.roles()));

        return personaRepository.save(personaEntity);
    }

    // Nuevo método para guardar desde PersonaResponse (usado por el listener)
    public PersonaEntity saveInterno(PersonaResponse request) {
        try {
            logger.info("Guardando persona internamente con ID: {}", request.id());

            PersonaEntity personaEntity = new PersonaEntity();
            personaEntity.setId(Long.parseLong(request.id()));
            personaEntity.setName(request.name());
            personaEntity.setLastname(request.lastname());
            personaEntity.setEmail(request.email());
            personaEntity.setDepartment(request.department());
            personaEntity.setRoles(convertirRoles(request.roles()));

            PersonaEntity savedPersona = personaRepository.save(personaEntity);
            logger.info("Persona guardada exitosamente con ID: {}", savedPersona.getId());

            return savedPersona;

        } catch (NumberFormatException e) {
            logger.error("Error al parsear el ID: {}", request.id(), e);
            throw new IllegalArgumentException("ID inválido: " + request.id(), e);
        } catch (Exception e) {
            logger.error("Error al guardar persona internamente", e);
            throw new RuntimeException("Error al procesar persona desde mensaje", e);
        }
    }

    private EnumSet<EnumRol> convertirRoles(Set<String> rolesString) {
        if (rolesString == null || rolesString.isEmpty()) {
            return EnumSet.noneOf(EnumRol.class);
        }

        EnumSet<EnumRol> roles = EnumSet.noneOf(EnumRol.class);
        for (String rolStr : rolesString) {
            try {
                roles.add(EnumRol.valueOf(rolStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                logger.warn("⚠️ Rol no reconocido: {}", rolStr);
            }
        }
        return roles;
    }
}