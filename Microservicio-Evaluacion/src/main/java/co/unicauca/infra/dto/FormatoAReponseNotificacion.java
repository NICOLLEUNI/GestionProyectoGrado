package co.unicauca.infra.dto;

import java.util.List;

public record FormatoAReponseNotificacion(
        Long id,
        String titulo,
        List<String>correosEstudiantes,
        List<String> correosDocentes
        ) { }
