package co.unicauca.infra.dto;

import java.util.Set;

public record PersonaRequest (

        String id,
        String name,
        String lastname,
        String email,
        Set<String> roles,
        String department
){ }
