package co.unicauca.infra.dto;

import java.util.List;

public record FormatoARequest(
        String id,
        String title,
        String mode,
        String projectManagerEmail,
        String projectCoManagerEmail,
        String generalObjetive,
        String specificObjetives,
        String archivoPDF,
        String cartaLaboral,
        List<String> estudiante,
        int counter
) {}