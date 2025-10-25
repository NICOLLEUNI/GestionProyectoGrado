package co.unicauca.infra.dto;


public record FormatoAUpdateRequest(
        Long id,
        String title,
        String state,
        String observations,
        int counter
){}