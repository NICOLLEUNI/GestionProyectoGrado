package co.unicauca.infrastructure.dto.request;

import java.util.List;

public record FormatoARequest(

        Long id,
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

) {
}
