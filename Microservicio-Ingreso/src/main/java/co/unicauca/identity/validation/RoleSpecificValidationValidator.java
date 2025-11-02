package co.unicauca.identity.validation;

import co.unicauca.identity.dto.request.RegisterRequest;
import co.unicauca.identity.enums.enumRol;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoleSpecificValidationValidator implements ConstraintValidator<RoleSpecificValidation, RegisterRequest> {

    @Override
    public boolean isValid(RegisterRequest request, ConstraintValidatorContext context) {
        if (request.roles() == null || request.roles().isEmpty()) {
            return true; // Ya está validado por @Size(min=1)
        }

        boolean isValid = true;
        context.disableDefaultConstraintViolation();

        // ✅ CORREGIDO: Validar ESTUDIANTE requiere programa (incluso en combinaciones)
        if (request.roles().contains(enumRol.ESTUDIANTE) && request.programa() == null) {
            context.buildConstraintViolationWithTemplate("El programa académico es obligatorio para estudiantes")
                    .addPropertyNode("programa")
                    .addConstraintViolation();
            isValid = false;
        }

        // ✅ CORREGIDO: Validar DOCENTE/COORDINADOR/JEFE requiere departamento (incluso en combinaciones)
        boolean requiereDepto = request.roles().contains(enumRol.DOCENTE) ||
                request.roles().contains(enumRol.COORDINADOR) ||
                request.roles().contains(enumRol.JEFE_DEPARTAMENTO);

        if (requiereDepto && request.departamento() == null) {
            context.buildConstraintViolationWithTemplate("El departamento es obligatorio para docentes, coordinadores y jefes de departamento")
                    .addPropertyNode("departamento")
                    .addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}