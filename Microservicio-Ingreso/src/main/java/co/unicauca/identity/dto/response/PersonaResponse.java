package co.unicauca.identity.dto.response;

import java.util.Set;

/**
 * DTO para respuesta con datos de persona
 * ✅ LÓGICA CORREGIDA: Maneja correctamente combinaciones de roles
 */
public record PersonaResponse(
        Long id,
        String name,
        String lastname,
        String email,
        Set<String> roles,
        String department,
        String programa

) {
    // Builder para construcción fluida
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String name;
        private String lastname;
        private String email;
        private Set<String> roles;
        private String department;
        private String programa;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder roles(Set<String> roles) {
            this.roles = roles;
            return this;
        }

        public Builder department(String departament) {
            this.department = departament;
            return this;
        }

        public Builder programa(String programa) {
            this.programa = programa;
            return this;
        }

        public PersonaResponse build() {
            return new PersonaResponse(id, name, lastname, email, roles, department, programa);
        }
    }

    /**
     * ✅ NUEVO: Factory method SIMPLIFICADO para RabbitMQ
     * Envía TODOS los datos sin lógica condicional
     */
    public static PersonaResponse createForRabbitMQ(
            Long id,
            String name,
            String lastname,
            String email,
            Set<String> roles,
            String department,
            String programa
    ) {
        // ✅ Para RabbitMQ, enviar TODOS los datos sin filtros
        return new PersonaResponse(id, name, lastname, email, roles, department, programa);
    }


    /**
     * ✅ LÓGICA CORREGIDA: Factory method con lógica condicional mejorada
     * - Si solo ESTUDIANTE → muestra programa, oculta departamento
     * - Si solo DOCENTE/COORDINADOR/JEFE → muestra departamento, oculta programa
     * - Si COMBINACIÓN → muestra ambos campos
     */
    public static PersonaResponse create(
            Long id,
            String name,
            String lastname,
            String email,
            Set<String> roles,
            String department,
            String programa
    ) {
        // ✅ LÓGICA MEJORADA: Basada en tipos de roles
        boolean tieneRolConPrograma = roles != null &&
                (roles.contains("ESTUDIANTE") || roles.contains("COORDINADOR"));
        boolean tieneRolConDepartamento = roles != null &&
                (roles.contains("DOCENTE") || roles.contains("JEFE_DEPARTAMENTO"));

        String departamentFinal;
        String programaFinal;

        if (tieneRolConPrograma && !tieneRolConDepartamento) {
            // ✅ Solo roles con programa: muestra programa, oculta departamento
            departamentFinal = null;
            programaFinal = programa;
        } else if (!tieneRolConPrograma && tieneRolConDepartamento) {
            // ✅ Solo roles con departamento: muestra departamento, oculta programa
            departamentFinal = department;
            programaFinal = null;
        } else if (tieneRolConPrograma && tieneRolConDepartamento) {
            // ✅ COMBINACIÓN: muestra ambos campos
            departamentFinal = department;
            programaFinal = programa;
        } else {
            // ✅ Caso por defecto: mostrar ambos (para seguridad)
            departamentFinal = department;
            programaFinal = programa;
        }

        return new PersonaResponse(id, name, lastname, email, roles, departamentFinal, programaFinal);
    }
}