package co.unicauca.infra.dto;


public record FormatoAUpdateRequest(
       Long id,
        String estado,
        String observaciones,
        int counter
){}