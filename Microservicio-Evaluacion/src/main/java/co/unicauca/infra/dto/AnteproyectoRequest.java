package co.unicauca.infra.dto;

import java.time.LocalDate;

public record AnteproyectoRequest(
        Long id,
        String titulo,
        LocalDate fecha,
        String estado,
        String observaciones,
        Long idProyectoGrado
) {
}
