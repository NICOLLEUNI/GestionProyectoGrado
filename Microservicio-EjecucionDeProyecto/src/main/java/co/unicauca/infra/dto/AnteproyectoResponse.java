package co.unicauca.infra.dto;

import java.time.LocalDate;

public record AnteproyectoResponse(
        Long id,
        String titulo,
        LocalDate fecha,
        String estado,
        String observaciones,
        Long idProyectoGrado
) {}