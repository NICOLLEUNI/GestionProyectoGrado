package co.unicauca.mapper;


import co.unicauca.dto.PersonaRequest;
import co.unicauca.entity.PersonaEntity;


public class PersonaMapper {

    public static PersonaEntity fromRequest(PersonaRequest dto) {
        return PersonaEntity.builder()
                .id(dto.getId())
                .nombreCompleto(dto.getName() + " " + dto.getLastname())
                .email(dto.getEmail())
                .roles(dto.getRoles())
                .departamento(dto.getDepartement())
                .activo(true)
                .build();
    }
}
