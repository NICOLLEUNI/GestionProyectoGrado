package co.unicauca.identity.strategy;

import co.unicauca.identity.enums.EnumDepartamento;
import co.unicauca.identity.enums.EnumPrograma;
import co.unicauca.identity.enums.enumRol;
import co.unicauca.identity.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Estrategia de validación para rol JEFE_DEPARTAMENTO - SINGLE_TABLE optimized
 * Ahora permite que jefes de departamento también tengan otros roles con programa
 */
@Component
@Slf4j
public class DepartmentHeadValidationStrategy implements RoleValidationStrategy {

    @Override
    public enumRol getSupportedRole() {
        return enumRol.JEFE_DEPARTAMENTO;
    }

    @Override
    public void validate(EnumPrograma programa, EnumDepartamento departamento) throws ValidationException {
        log.debug("Validando rol JEFE_DEPARTAMENTO con departamento: {}", departamento);

        // ✅ SINGLE_TABLE: Solo validamos que tenga departamento
        if (departamento == null) {
            throw new ValidationException("El departamento es obligatorio para jefes de departamento");
        }

        // ✅ SINGLE_TABLE: NO validamos que no tenga programa

        validateJefeDepartamento(departamento);
    }

    /**
     * Validaciones específicas para jefes de departamento
     */
    private void validateJefeDepartamento(EnumDepartamento departamento) {
        // Validaciones específicas para jefes de departamento
        // Por ejemplo, que solo pueda haber un jefe por departamento
        log.debug("Departamento válido para jefe: {}", departamento);

        // Ejemplo de validación futura (se implementaría en el servicio):
        // if (personaRepository.existsByRolAndDepartamento(enumRol.JEFE_DEPARTAMENTO, departamento)) {
        //     throw new ValidationException("Ya existe un jefe de departamento para " + departamento);
        // }
    }
}