package co.unicauca.infra.dto;

import java.util.List;

public record FormatoEvaluadoRequest (
    Long id,
    String titulo,
    List<String>correosEstudiantes,
    List<String> correosDocentes
)
{}
