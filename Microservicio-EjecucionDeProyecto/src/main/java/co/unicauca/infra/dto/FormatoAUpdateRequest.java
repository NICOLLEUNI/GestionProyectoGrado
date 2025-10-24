package co.unicauca.infra.dto;


public record FormatoAUpdateRequest(
        String id,
        String estado,
        String observaciones,
        int counter
){}