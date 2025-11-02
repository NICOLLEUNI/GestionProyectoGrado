package co.unicauca.identity.dto.request;

import co.unicauca.identity.enums.EnumDepartamento;
import co.unicauca.identity.enums.EnumPrograma;
import co.unicauca.identity.enums.enumRol;
import co.unicauca.identity.validation.InstitutionalEmail;
import co.unicauca.identity.validation.StrongPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import co.unicauca.identity.validation.RoleSpecificValidation; // Importación agregada
import java.util.Set;

/**
 * DTO para la solicitud de registro de usuario
 * Maneja múltiples roles y campos condicionales por rol
 */
@RoleSpecificValidation
public record RegisterRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{2,}$",
                message = "El nombre debe contener solo letras y tener al menos 2 caracteres")
        String name,

        @NotBlank(message = "Los apellidos son obligatorios")
        @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{2,}$",
                message = "Los apellidos deben contener solo letras y tener al menos 2 caracteres")
        String lastname,

        @Pattern(regexp = "^[0-9]{10}$",
                message = "El celular debe tener 10 dígitos numéricos")
        String phone,

        @NotNull(message = "Los roles son obligatorios")
        @Size(min = 1, message = "Debe seleccionar al menos un rol")
        Set<enumRol> roles,

        // Campo condicional: obligatorio si roles contiene ESTUDIANTE
        EnumPrograma programa,

        // Campo condicional: obligatorio si roles contiene DOCENTE, COORDINADOR o JEFE_DEPARTAMENTO
        EnumDepartamento departamento,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato del email no es válido")
        @InstitutionalEmail
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        @StrongPassword
        String password
) {
    // Builder para construcción fluida
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String lastname;
        private String phone;
        private Set<enumRol> roles;
        private EnumPrograma programa;
        private EnumDepartamento departamento;
        private String email;
        private String password;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder roles(Set<enumRol> roles) {
            this.roles = roles;
            return this;
        }

        public Builder programa(EnumPrograma programa) {
            this.programa = programa;
            return this;
        }

        public Builder departamento(EnumDepartamento departamento) {
            this.departamento = departamento;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public RegisterRequest build() {
            return new RegisterRequest(name, lastname, phone, roles, programa, departamento, email, password);
        }
    }

    /**
     * Método de utilidad para verificar si el registro contiene el rol ESTUDIANTE
     */
    public boolean tieneRolEstudiante() {
        return roles != null && roles.contains(enumRol.ESTUDIANTE);
    }

    /**
     * Método de utilidad para verificar si el registro contiene roles que requieren departamento
     */
    public boolean requiereDepartamento() {
        return roles != null && (
                roles.contains(enumRol.DOCENTE) ||
                        roles.contains(enumRol.COORDINADOR) ||
                        roles.contains(enumRol.JEFE_DEPARTAMENTO)
        );
    }
}