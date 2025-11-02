package co.unicauca.identity.messaging.dto;

import java.util.Set;

/**
 * DTO ESPECÍFICO para mensajes RabbitMQ
 * Compatible con TODOS los microservicios
 */
public record UsuarioMessage(
        Long id,
        String name,
        String lastname,
        String email,
        Set<String> roles,
        String department,  // ✅ department (igual que en Evaluación)
        String programa     // ✅ programa (igual que en Evaluación)
) {
    /**
     * Constructor desde entidad Persona
     */
    public static UsuarioMessage fromEntity(co.unicauca.identity.entity.Persona persona) {
        // Convertir roles Enum a Set<String>
        Set<String> rolesString = persona.getRoles().stream()
                .map(Enum::name)
                .collect(java.util.stream.Collectors.toSet());

        // Convertir departamento Enum a String (o null)
        String departmentString = persona.getDepartamento() != null ?
                persona.getDepartamento().name() : null;

        // Convertir programa Enum a String (o null)
        String programaString = persona.getPrograma() != null ?
                persona.getPrograma().name() : null;

        return new UsuarioMessage(
                persona.getIdUsuario(),
                persona.getName(),
                persona.getLastname(),
                persona.getEmail(),
                rolesString,
                departmentString,
                programaString
        );
    }
}