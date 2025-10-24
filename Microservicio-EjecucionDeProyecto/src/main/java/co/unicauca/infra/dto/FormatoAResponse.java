package co.unicauca.infra.dto;

import java.util.List;

public record FormatoAResponse(
        String id,
        String titulo,
        String mode,
        String estado,
        String observaciones,
        int counter,
        Long idProyectoGrado,
        List<Long> versionesIds
) {}
