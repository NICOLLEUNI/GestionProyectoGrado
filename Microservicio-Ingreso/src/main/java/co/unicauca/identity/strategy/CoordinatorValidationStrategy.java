package co.unicauca.identity.strategy;

import co.unicauca.identity.enums.EnumDepartamento;
import co.unicauca.identity.enums.EnumPrograma;
import co.unicauca.identity.enums.enumRol;
import co.unicauca.identity.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Estrategia de validación para rol COORDINADOR - SINGLE_TABLE optimized
 * ✅ CORREGIDO: Coordinador ahora requiere PROGRAMA, no departamento
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
        log.debug("Validando rol COORDINADOR con programa: {}", programa);

        // ✅ CORREGIDO: Coordinador ahora requiere PROGRAMA, no departamento
        if (programa == null) {
            throw new ValidationException("El programa es obligatorio para coordinadores");
        }

        // ✅ CORREGIDO: NO validamos que no tenga departamento
        // porque el usuario podría tener otros roles que requieran departamento

        validateProgramaCoordinador(programa);
    }

    /**
     * Validaciones específicas para coordinadores de programa
     */
    private void validateProgramaCoordinador(EnumPrograma programa) {
        // Validaciones específicas para coordinadores
        // Por ejemplo, que el programa exista o esté activo
        log.debug("Programa válido para coordinador: {}", programa);

        // Ejemplo de validación futura:
        // if (!programaService.tieneCoordinadorActivo(programa)) {
        //     throw new ValidationException("El programa " + programa + " ya tiene un coordinador activo");
        // }
    }
}