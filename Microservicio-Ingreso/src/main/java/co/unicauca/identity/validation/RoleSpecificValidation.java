package co.unicauca.identity.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RoleSpecificValidationValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleSpecificValidation {
    String message() default "Error en validación de campos específicos por rol";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}