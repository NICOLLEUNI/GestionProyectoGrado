package co.unicauca.infra.dto.notification;

import java.util.List;

public record VersionNotification(
        Long versionId,
        Long formatoAId,
        int numeroVersion,
        String estado,
        List<String> estudiantesEmails, // ← Para buscar programa académico
        String directorEmail // ← Para buscar departamento
) {
}
