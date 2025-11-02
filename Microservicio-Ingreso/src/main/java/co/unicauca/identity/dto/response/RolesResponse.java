package co.unicauca.identity.dto.response;

import co.unicauca.identity.enums.EnumDepartamento;
import co.unicauca.identity.enums.EnumPrograma;
import co.unicauca.identity.enums.enumRol;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO para respuesta con roles y programas disponibles
 */
public record RolesResponse(
        List<String> roles,           // Nombres de roles disponibles
        List<String> programas,       // Nombres de programas disponibles
        List<String> departamentos    // Nombres de departamentos disponibles
) {
    // Builder para construcción fluida
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<String> roles;
        private List<String> programas;
        private List<String> departamentos;

        public Builder roles(List<String> roles) {
            this.roles = roles;
            return this;
        }

        public Builder programas(List<String> programas) {
            this.programas = programas;
            return this;
        }

        public Builder departamentos(List<String> departamentos) {
            this.departamentos = departamentos;
            return this;
        }

        public RolesResponse build() {
            return new RolesResponse(roles, programas, departamentos);
        }
    }

    /**
     * Factory method para crear response con todos los valores disponibles
     */
    public static RolesResponse createAll() {
        return new RolesResponse(
                Arrays.stream(enumRol.values())
                        .map(Enum::name)
                        .collect(Collectors.toList()),
                Arrays.stream(EnumPrograma.values())
                        .map(Enum::name)
                        .collect(Collectors.toList()),
                Arrays.stream(EnumDepartamento.values())
                        .map(Enum::name)
                        .collect(Collectors.toList())
        );
    }
}