package co.unicauca.identity.exception;

/**
 * Excepción lanzada cuando se intenta registrar un usuario con un email ya existente
 * SINGLE_TABLE: Adaptada para el modelo Persona único
 */
public class EmailAlreadyExistsException extends BusinessException {

    public EmailAlreadyExistsException() {
        super("El email ya está registrado");
    }

    public EmailAlreadyExistsException(String email) {
        super(String.format("El email '%s' ya está registrado en el sistema", email));
    }
}