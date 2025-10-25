package co.unicauca.infra.dto;

public record FormatoAUpdateResponse(
        Long id,
        String title,
        String state,
        String observations,
        int counter
) {}
