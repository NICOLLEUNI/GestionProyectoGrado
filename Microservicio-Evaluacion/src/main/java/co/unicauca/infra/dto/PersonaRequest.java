package co.unicauca.infra.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Set;
@JsonIgnoreProperties(ignoreUnknown = true) //
public record PersonaRequest(
        Long id,
        String name,
        String lastname,
        String email,
        Set<String> roles,
        String department,
        String programa
) {}