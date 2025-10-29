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
        String departament,
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
        private String departament;
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

        public Builder departament(String departament) {
            this.departament = departament;
            return this;
        }

        public Builder programa(String programa) {
            this.programa = programa;
            return this;
        }

        public PersonaResponse build() {
            return new PersonaResponse(id, name, lastname, email, roles, departament, programa);
        }
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
            String departament,
            String programa
    ) {
        // ✅ LÓGICA MEJORADA: Determinar qué campos mostrar basado en los roles
        boolean esSoloEstudiante = roles != null &&
                roles.contains("ESTUDIANTE") &&
                roles.size() == 1;

        boolean esSoloRolConDepartamento = roles != null &&
                (roles.contains("DOCENTE") ||
                        roles.contains("COORDINADOR") ||
                        roles.contains("JEFE_DEPARTAMENTO")) &&
                roles.size() == 1;

        boolean esCombinacion = roles != null && roles.size() > 1;

        String departamentFinal;
        String programaFinal;

        if (esSoloEstudiante) {
            // ✅ Solo ESTUDIANTE: muestra programa, oculta departamento
            departamentFinal = null;
            programaFinal = programa;
        } else if (esSoloRolConDepartamento) {
            // ✅ Solo DOCENTE/COORDINADOR/JEFE: muestra departamento, oculta programa
            departamentFinal = departament;
            programaFinal = null;
        } else if (esCombinacion) {
            // ✅ COMBINACIÓN: muestra ambos campos
            departamentFinal = departament;
            programaFinal = programa;
        } else {
            // ✅ Caso por defecto: mostrar ambos (para seguridad)
            departamentFinal = departament;
            programaFinal = programa;
        }

        return new PersonaResponse(id, name, lastname, email, roles, departamentFinal, programaFinal);
    }
}