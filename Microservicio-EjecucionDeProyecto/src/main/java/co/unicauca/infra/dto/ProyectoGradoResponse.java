package co.unicauca.infra.dto;

import java.time.LocalDate;
import java.util.List;

public record ProyectoGradoResponse(
        Long id,
        String title,
        LocalDate fecha,
        List<String> estudiantesEmail,
        Long idFormatoA,
        Long idAnteproyecto
) {}
