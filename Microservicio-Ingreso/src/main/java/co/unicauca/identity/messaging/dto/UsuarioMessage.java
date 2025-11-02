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
        String department,
        String programa
) {

    public static UsuarioMessage fromEntity(co.unicauca.identity.entity.Persona persona) {
        // Convertir roles Enum a Set<String>
        Set<String> rolesString = persona.getRoles().stream()
                .map(Enum::name)
                .collect(java.util.stream.Collectors.toSet());

        // Obtener valores originales
        String departmentOriginal = persona.getDepartamento() != null ?
                persona.getDepartamento().name() : null;
        String programaOriginal = persona.getPrograma() != null ?
                persona.getPrograma().name() : null;

        // ✅ APLICAR LA MISMA LÓGICA CONDICIONAL MEJORADA
        boolean esSoloEstudiante = rolesString != null &&
                rolesString.contains("ESTUDIANTE") &&
                rolesString.size() == 1;

        boolean esSoloRolConDepartamento = rolesString != null &&
                (rolesString.contains("DOCENTE") ||
                        rolesString.contains("COORDINADOR") ||
                        rolesString.contains("JEFE_DEPARTAMENTO")) &&
                rolesString.size() == 1;

        // ✅ NUEVA VALIDACIÓN: Combinación de roles de departamento SIN estudiante
        boolean esCombinacionRolesDepartamento = rolesString != null &&
                (rolesString.contains("DOCENTE") || rolesString.contains("COORDINADOR") || rolesString.contains("JEFE_DEPARTAMENTO")) &&
                !rolesString.contains("ESTUDIANTE") &&
                rolesString.size() >= 1;

        boolean esCombinacionMixta = rolesString != null &&
                rolesString.contains("ESTUDIANTE") &&
                (rolesString.contains("DOCENTE") || rolesString.contains("COORDINADOR") || rolesString.contains("JEFE_DEPARTAMENTO"));

        String departmentFinal;
        String programaFinal;

        if (esSoloEstudiante) {
            departmentFinal = null;
            programaFinal = programaOriginal;
        } else if (esSoloRolConDepartamento || esCombinacionRolesDepartamento) {
            departmentFinal = departmentOriginal;
            programaFinal = null;
        } else if (esCombinacionMixta) {
            departmentFinal = departmentOriginal;
            programaFinal = programaOriginal;
        } else {
            departmentFinal = departmentOriginal;
            programaFinal = programaOriginal;
        }

        return new UsuarioMessage(
                persona.getIdUsuario(),
                persona.getName(),
                persona.getLastname(),
                persona.getEmail(),
                rolesString,
                departmentFinal,
                programaFinal
        );
    }
}