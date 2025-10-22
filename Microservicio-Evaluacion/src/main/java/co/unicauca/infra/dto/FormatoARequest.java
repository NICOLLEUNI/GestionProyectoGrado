package co.unicauca.infra.dto;

import java.util.List;

public record FormatoARequest(
        String title,
        String mode,
        String projectManager,
        String projectCoManager,
        String generalObjetive,
        String specificObjetives,
        String archivoPDF,
        String cartaLaboral,
        List<String> estudiantes
) {}