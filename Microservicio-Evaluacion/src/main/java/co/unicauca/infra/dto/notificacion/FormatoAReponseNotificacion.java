package co.unicauca.infra.dto.notificacion;

import java.util.List;

public record FormatoAReponseNotificacion(
        Long id,
        String titulo,
        List<String>correosEstudiantes,
        List<String> correosDocentes
        ) { }
