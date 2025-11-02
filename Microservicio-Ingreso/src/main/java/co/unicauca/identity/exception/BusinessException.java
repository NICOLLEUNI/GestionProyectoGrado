package co.unicauca.identity.exception;

/**
 * Excepción base para todos los errores de negocio en el microservicio de identidad
 * SINGLE_TABLE: Adaptada para el modelo actual
 */
public class BusinessException extends RuntimeException {

    // ✅ ELIMINAR campos problemáticos que causaban conflictos
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    // ✅ MANTENER constructor simple para compatibilidad
    public BusinessException(String message, String errorCode) {
        super(message); // Ignorar errorCode para simplificar
    }

    public BusinessException(String message, String errorCode, String details) {
        super(message + (details != null ? ": " + details : ""));
    }
}