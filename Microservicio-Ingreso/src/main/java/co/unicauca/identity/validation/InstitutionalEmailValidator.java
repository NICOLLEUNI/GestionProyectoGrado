package co.unicauca.identity.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class InstitutionalEmailValidator implements ConstraintValidator<InstitutionalEmail, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }
        return value.toLowerCase().endsWith("@unicauca.edu.co");
    }
}
