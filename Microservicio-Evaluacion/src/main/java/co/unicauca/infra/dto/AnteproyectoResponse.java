package co.unicauca.infra.dto;

import java.time.LocalDate;

public record AnteproyectoResponse (
    Long id,
    String titulo,
    String estado,
    String observaciones,
    Long idProyectoGrado,
    LocalDate fecha
){}
