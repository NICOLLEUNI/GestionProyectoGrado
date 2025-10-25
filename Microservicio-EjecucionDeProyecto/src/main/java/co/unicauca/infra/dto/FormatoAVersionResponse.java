package co.unicauca.infra.dto;

import java.time.LocalDate;

public record FormatoAVersionResponse(
        Long id,
        int numVersion,
        LocalDate fecha,
        String title,
        String mode,
        String state,
        String observations,
        int counter,
        String idFormatoA
) {}
