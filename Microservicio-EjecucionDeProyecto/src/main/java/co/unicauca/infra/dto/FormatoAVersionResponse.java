package co.unicauca.infra.dto;

import java.time.LocalDate;

public record FormatoAVersionResponse(
        String id,
        int numVersion,
        LocalDate fecha,
        String titulo,
        String modalidad,
        String estado,
        String observaciones,
        int counter,
        String idFormatoA
) {}
