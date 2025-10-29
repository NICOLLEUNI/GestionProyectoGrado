package co.unicauca.identity.exception;

/**
 * Excepción lanzada cuando no se encuentra un usuario
 * SINGLE_TABLE: Busca en entidad Persona única
 */
public class UserNotFoundException extends BusinessException {

    public UserNotFoundException() {
        super("Usuario no encontrado");
    }

    public UserNotFoundException(Long userId) {
        super(String.format("Usuario con ID %d no encontrado", userId));
    }

    public UserNotFoundException(String email) {
        super(String.format("Usuario con email '%s' no encontrado", email));
    }
}