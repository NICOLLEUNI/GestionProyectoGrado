package co.unicauca.identity.dto.response;

/**
 * DTO para respuesta de login exitoso
 * Incluye datos de persona y token JWT
 */
public record LoginResponse(
        PersonaResponse persona,
        String token
) {
    // Builder para construcción fluida
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private PersonaResponse persona;
        private String token;

        public Builder persona(PersonaResponse persona) {
            this.persona = persona;
            return this;
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public LoginResponse build() {
            return new LoginResponse(persona, token);
        }
    }
}