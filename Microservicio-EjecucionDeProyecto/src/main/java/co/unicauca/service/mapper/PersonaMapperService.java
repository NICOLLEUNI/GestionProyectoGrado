package co.unicauca.service.mapper;

import co.unicauca.entity.PersonaEntity;
import co.unicauca.infra.dto.PersonaRequest;
import co.unicauca.infra.dto.PersonaResponse;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class PersonaMapperService {

    /**
     * Convierte un PersonaRequest en PersonaEntity (para creación o actualización)
     */
    public PersonaEntity mapFromRequest(PersonaRequest request) {
        PersonaEntity entity = new PersonaEntity();

        // ID puede venir como String, convertir a Long si es necesario
        if (request.id() != null) {
            try {
                entity.setId(request.id());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("ID de PersonaRequest no es un número válido");
            }
        }

        entity.setName(request.name());
        entity.setLastname(request.lastname());
        entity.setEmail(request.email());
        entity.setDepartment(request.department());
        entity.setRoles(request.roles() != null ? request.roles() : new HashSet<>());

        return entity;
    }

    /**
     * Convierte un PersonaResponse en PersonaEntity (por ejemplo para actualizaciones)
     */
    public PersonaEntity mapFromResponse(PersonaResponse response) {
        PersonaEntity entity = new PersonaEntity();

        if (response.id() != null) {
            try {
                entity.setId(response.id());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("ID de PersonaResponse no es un número válido");
            }
        }

        entity.setName(response.name());
        entity.setLastname(response.lastname());
        entity.setEmail(response.email());
        entity.setDepartment(response.department());
        entity.setRoles(response.roles() != null ? response.roles() : new HashSet<>());

        return entity;
    }

    /**
     * Convierte una PersonaEntity en PersonaResponse (para exponer por API)
     */
    public PersonaResponse mapToResponse(PersonaEntity entity) {
        return new PersonaResponse(
                entity.getId() != null ? String.valueOf(entity.getId()) : null,
                entity.getName(),
                entity.getLastname(),
                entity.getEmail(),
                entity.getRoles(),
                entity.getDepartment()
        );
    }
}
