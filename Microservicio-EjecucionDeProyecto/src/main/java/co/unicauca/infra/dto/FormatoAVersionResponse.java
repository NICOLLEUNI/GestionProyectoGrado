package co.unicauca.infra.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.time.LocalDate;

public record FormatoAVersionResponse(
        Long id,
        int numVersion,
        LocalDate fecha,
        String titulo,
        String modalidad,
        String estado,
        String observaciones,
        int counter,
        @JsonAlias({"IdFormatoA"})
        Long idFormatoA



) {}
