package co.unicauca.identity.strategy;

import co.unicauca.identity.enums.EnumDepartamento;
import co.unicauca.identity.enums.EnumPrograma;
import co.unicauca.identity.enums.enumRol;
import co.unicauca.identity.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Estrategia de validación para rol COORDINADOR - SINGLE_TABLE optimized
 * Ahora permite que coordinadores también tengan otros roles con programa
 */
@Component
@Slf4j
public class CoordinatorValidationStrategy implements RoleValidationStrategy {

    @Override
    public enumRol getSupportedRole() {
        return enumRol.COORDINADOR;
    }

    @Override
    public void validate(EnumPrograma programa, EnumDepartamento departamento) throws ValidationException {
        log.debug("Validando rol COORDINADOR con departamento: {}", departamento);

        // ✅ SINGLE_TABLE: Solo validamos que tenga departamento
        if (departamento == null) {
            throw new ValidationException("El departamento es obligatorio para coordinadores");
        }

        // ✅ SINGLE_TABLE: NO validamos que no tenga programa

        validateDepartamentoCoordinador(departamento);
    }

    /**
     * Validaciones específicas para coordinadores de departamento
     */
    private void validateDepartamentoCoordinador(EnumDepartamento departamento) {
        // Validaciones específicas para coordinadores
        // Por ejemplo, que el departamento exista o tenga programas activos
        log.debug("Departamento válido para coordinador: {}", departamento);

        // Ejemplo de validación futura:
        // if (!departamentoService.tieneProgramasActivos(departamento)) {
        //     throw new ValidationException("El departamento " + departamento + " no tiene programas activos");
        // }
    }
}