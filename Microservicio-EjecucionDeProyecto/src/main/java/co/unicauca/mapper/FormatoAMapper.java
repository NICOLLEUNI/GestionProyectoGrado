package co.unicauca.mapper;

import co.unicauca.dto.FormatoARequest;
import co.unicauca.entity.FormatoAEntity;
import co.unicauca.entity.ProyectoGradoEntity;

public class FormatoAMapper {

    /**
     * Convierte DTO de entrada en Entity
     * @param dto FormatoARequest
     * @param proyecto ProyectoGradoEntity asociado
     * @return FormatoAEntity listo para persistir
     */
    public static FormatoAEntity fromRequest(FormatoARequest dto, ProyectoGradoEntity proyecto) {
        if (dto == null) return null;

        return FormatoAEntity.builder()
                .id(dto.getId())                 // ya es Long
                .titulo(dto.getTitle())
                .modalidad(dto.getMode())
                .estado("EN_PROCESO")
                .proyectoGrado(proyecto)
                .counter(dto.getCounter())       // agregamos el counter
                .build();
    }

}
