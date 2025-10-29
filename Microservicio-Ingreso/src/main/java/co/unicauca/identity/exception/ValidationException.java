package co.unicauca.identity.exception;

import java.util.List;

/**
 * Excepción lanzada cuando hay errores de validación en los datos de entrada
 * SINGLE_TABLE: Para validaciones del modelo Persona
 */
public class ValidationException extends BusinessException {

    private final List<String> validationErrors;

    public ValidationException(List<String> validationErrors) {
        super("Error de validación en los datos de entrada: " +
                String.join(", ", validationErrors));
        this.validationErrors = validationErrors;
    }

    public ValidationException(String message) {
        super(message);
        this.validationErrors = List.of(message);
    }

    public ValidationException(String message, List<String> validationErrors) {
        super(message + ": " + String.join(", ", validationErrors));
        this.validationErrors = validationErrors;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }
}