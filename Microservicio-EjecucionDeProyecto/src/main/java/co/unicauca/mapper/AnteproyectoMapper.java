package co.unicauca.mapper;

import co.unicauca.dto.AnteproyectoRequest;
import co.unicauca.entity.AnteproyectoEntity;
import co.unicauca.entity.ProyectoGradoEntity;

public class AnteproyectoMapper {

    public static AnteproyectoEntity fromRequest(AnteproyectoRequest dto, ProyectoGradoEntity proyecto) {
        return AnteproyectoEntity.builder()
                .id(dto.getId())
                .titulo(dto.getTitulo())
                .fecha(dto.getFecha())
                .estado(dto.getEstado())
                .observaciones(dto.getObservaciones())
                .proyectoGrado(proyecto)
                .build();
    }

}
