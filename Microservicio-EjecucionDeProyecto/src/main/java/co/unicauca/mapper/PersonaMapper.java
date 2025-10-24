package co.unicauca.mapper;

import co.unicauca.dto.PersonaRequest;
import co.unicauca.entity.PersonaEntity;

public class PersonaMapper {

    public static PersonaEntity fromRequest(PersonaRequest dto) {
        if (dto == null) return null;

        return PersonaEntity.builder()
                .id(dto.getId())
                .nombre(dto.getName() + " " + dto.getLastname())
                .correo(dto.getEmail())
                .roles(dto.getRoles())
                .departamento(dto.getDepartement())
                .activo(true)
                .build();
    }
}
