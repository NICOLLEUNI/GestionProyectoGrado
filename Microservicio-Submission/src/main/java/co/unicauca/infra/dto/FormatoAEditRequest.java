package co.unicauca.infra.dto;

public record FormatoAEditRequest (
        Long id,
        String archivoPDF,
        String cartaLaboral,
        String generalObjetive,
        String specificObjetives
){ }
