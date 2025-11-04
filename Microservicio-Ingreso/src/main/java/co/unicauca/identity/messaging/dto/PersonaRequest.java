package co.unicauca.identity.messaging.dto;

import java.util.Set;

/**
 * DTO EXACTAMENTE IGUAL al del microservicio de Notificación
 * Para garantizar compatibilidad total en la serialización/deserialización
 */
public record PersonaRequest(
        Long id,
        String name,
        String lastname,
        String email,
        Set<String> roles,
        String department,
        String programa,
        String phone
) {
    /**
     * Constructor desde entidad Persona del microservicio Login/Signin
     */
    public static PersonaRequest fromEntity(co.unicauca.identity.entity.Persona persona) {
        // Convertir roles Enum a Set<String> (igual que en el microservicio Notificación)
        Set<String> rolesString = persona.getRoles().stream()
                .map(Enum::name)
                .collect(java.util.stream.Collectors.toSet());

        // Convertir departamento Enum a String (o null)
        String departmentString = persona.getDepartamento() != null ?
                persona.getDepartamento().name() : null;

        // Convertir programa Enum a String (o null)
        String programaString = persona.getPrograma() != null ?
                persona.getPrograma().name() : null;
        String phoneString = persona.getPhone(); // Puede ser null
        return new PersonaRequest(
                persona.getIdUsuario(),
                persona.getName(),
                persona.getLastname(),
                persona.getEmail(),
                rolesString,
                departmentString,
                programaString,
                phoneString
        );
    }
}