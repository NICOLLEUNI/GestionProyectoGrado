package co.unicauca.identity.exception;

/**
 * Excepción lanzada cuando un token JWT es inválido, expirado o malformado
 */
public class InvalidTokenException extends BusinessException {

    public InvalidTokenException() {
        super("Token JWT inválido o expirado");
    }

    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}