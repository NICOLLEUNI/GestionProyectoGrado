package co.unicauca.infra.dto;

import java.util.List;

public record FormatoACreado(
        Long formatoAId,
        String titulo,
        List<String> estudiantesEmails, // ← Para buscar programa académico
        String directorEmail // ←
) {
}
