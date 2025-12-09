package co.unicauca.infrastructure.dto.notification;

import java.util.List;

public record FormatoAResponseNotification(
        Long id,
        String titulo,
        List<String>correosEstudiantes,
        List<String> correosDocentes
) { }

