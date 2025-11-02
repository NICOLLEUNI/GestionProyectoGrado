package co.unicauca.identity.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StrongPasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StrongPassword {
    String message() default "La contraseña debe tener al menos 6 caracteres, 1 mayúscula, 1 número y 1 carácter especial";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}