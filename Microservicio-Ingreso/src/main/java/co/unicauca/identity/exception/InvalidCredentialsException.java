package co.unicauca.identity.exception;

/**
 * Excepción lanzada cuando las credenciales de login son inválidas
 */
public class InvalidCredentialsException extends BusinessException {

    public InvalidCredentialsException() {
        super("Credenciales inválidas");
    }

    public InvalidCredentialsException(String email) {
        super(String.format("Credenciales inválidas para el email: %s", email));
    }
}