package co.unicauca.infra.dto;

import java.util.Set;

public record PersonaRequest (
        //aquí recibo el objeto persona de la cola
        //Record - No se modifica
        Long id,
        String name,
        String lastname,
        String email,
        Set<String> roles,
        String department
){ }
