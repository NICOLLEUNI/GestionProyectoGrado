package co.unicauca.mapper;

import co.unicauca.dto.FormatoAVersionRequest;
import co.unicauca.entity.FormatoAVersionEntity;
import co.unicauca.entity.FormatoAEntity;

public class FormatoAVersionMapper {

    public static FormatoAVersionEntity fromRequest(FormatoAVersionRequest dto, FormatoAEntity formatoA) {
        return FormatoAVersionEntity.builder()
                .id(dto.getId())
                .numVersion(dto.getNumVersion())
                .fecha(dto.getFecha())
                .titulo(dto.getTitulo())
                .modalidad(dto.getModalidad())
                .estado(dto.getEstado())
                .observaciones(dto.getObservaciones())
                .counter(dto.getCounter())
                .formatoA(formatoA) // vínculo con el formato principal
                .build();
    }
}
