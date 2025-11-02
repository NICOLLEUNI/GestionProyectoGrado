package co.unicauca.identity.util;

import co.unicauca.identity.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * Utilidad para realizar validaciones comunes
 */
@Component
@Slf4j
public class ValidationUtil {

    private static final String EMAIL_REGEX = "^[^\\s@]+@unicauca\\.edu\\.co$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{6,}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    private static final String NAME_REGEX = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{2,}$";
    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);

    private static final String PHONE_REGEX = "^[0-9]{10}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    /**
     * Valida que un email sea institucional (@unicauca.edu.co)
     */
    public boolean isValidInstitutionalEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Valida que una contraseña cumpla con los requisitos de seguridad
     */
    public boolean isValidStrongPassword(String password) {
        if (password == null || password.isBlank()) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * Valida que un nombre o apellido contenga solo letras y espacios
     */
    public boolean isValidName(String name) {
        if (name == null || name.isBlank()) {
            return false;
        }
        return NAME_PATTERN.matcher(name).matches();
    }

    /**
     * Valida que un número de teléfono contenga 10 dígitos numéricos
     */
    public boolean isValidPhone(String phone) {
        if (phone == null || phone.isBlank()) {
            return true; // El teléfono es opcional
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Valida que una cadena no sea null o vacía
     */
    public void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new BusinessException("El campo " + fieldName + " no puede estar vacío");
        }
    }

    /**
     * Valida que un objeto no sea null
     */
    public void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new BusinessException("El campo " + fieldName + " no puede ser nulo");
        }
    }

    /**
     * Valida que una cadena tenga longitud mínima
     */
    public void validateMinLength(String value, int minLength, String fieldName) {
        if (value != null && value.length() < minLength) {
            throw new BusinessException("El campo " + fieldName + " debe tener al menos " + minLength + " caracteres");
        }
    }

    /**
     * Valida que una cadena tenga longitud máxima
     */
    public void validateMaxLength(String value, int maxLength, String fieldName) {
        if (value != null && value.length() > maxLength) {
            throw new BusinessException("El campo " + fieldName + " no puede tener más de " + maxLength + " caracteres");
        }
    }
}