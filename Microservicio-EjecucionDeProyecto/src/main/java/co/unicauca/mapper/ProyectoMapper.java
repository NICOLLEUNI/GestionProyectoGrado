package co.unicauca.mapper;

import co.unicauca.dto.ProyectoGradoRequest;
import co.unicauca.entity.ProyectoGradoEntity;

public class ProyectoMapper {

    public static ProyectoGradoEntity fromRequest(ProyectoGradoRequest dto) {
        return ProyectoGradoEntity.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .fecha(dto.getFecha())
                .estudiantesEmail(dto.getEstudiantesEmail())
                .idFormatoA(dto.getIdFormatoA())
                .idAnteproyecto(dto.getIdAnteproyecto())
                .estado("EN_EJECUCION") // valor por defecto al crearse
                .versionActiva("1.0")
                .build();
    }
}
