package co.unicauca.infrastructure.adapters.output.persistence.mapper;

import co.unicauca.domain.entities.Persona;
import co.unicauca.infrastructure.adapters.output.persistence.entities.PersonaEntity;

public class PersonaMapper {

    public static PersonaEntity toEntity(Persona persona) {
        if (persona == null) return null;

        PersonaEntity entity = new PersonaEntity();
        entity.setIdUsuario(persona.getIdUsuario());
        entity.setName(persona.getName());
        entity.setLastname(persona.getLastname());
        entity.setEmail(persona.getEmail());
        entity.setDepartment(persona.getDepartment());
        entity.setPrograma(persona.getPrograma());
        entity.setRoles(persona.getRoles());
        return entity;
    }

    public static Persona toDomain(PersonaEntity entity) {
        if (entity == null) return null;

        return new Persona(
                entity.getIdUsuario(),
                entity.getName(),
                entity.getLastname(),
                entity.getEmail(),
                entity.getDepartment(),
                entity.getPrograma(),
                entity.getRoles()
        );
    }

}
