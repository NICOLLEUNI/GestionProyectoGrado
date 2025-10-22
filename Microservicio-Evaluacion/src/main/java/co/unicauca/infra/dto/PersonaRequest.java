package co.unicauca.infra.dto;

import java.util.Set;

public record PersonaRequest(
        int id,
        String name,
        String lastname,
        String email,
        String password,
        Set<String> roles,
        String department
) {}