package co.unicauca.infra.dto;

import java.util.List;

public record DtoFormatoVersion(
        Long versionId,
        Long formatoAId,
        int numeroVersion,
        String estado,
        List<String> estudiantesEmails, // ← Para buscar programa académico
        String directorEmail
) {
}
