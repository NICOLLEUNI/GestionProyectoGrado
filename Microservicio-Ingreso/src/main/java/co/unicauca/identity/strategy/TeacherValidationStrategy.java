package co.unicauca.identity.strategy;

import co.unicauca.identity.enums.EnumDepartamento;
import co.unicauca.identity.enums.EnumPrograma;
import co.unicauca.identity.enums.enumRol;
import co.unicauca.identity.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Estrategia de validación para rol DOCENTE - SINGLE_TABLE optimized
 * Ahora permite que docentes también tengan otros roles con programa
 */
@Component
@Slf4j
public class TeacherValidationStrategy implements RoleValidationStrategy {

    @Override
    public enumRol getSupportedRole() {
        return enumRol.DOCENTE;
    }

    @Override
    public void validate(EnumPrograma programa, EnumDepartamento departamento) throws ValidationException {
        log.debug("Validando rol DOCENTE con departamento: {}", departamento);

        // ✅ SINGLE_TABLE: Solo validamos que tenga departamento, no nos importa el programa
        // porque el usuario podría tener otros roles que requieran programa
        if (departamento == null) {
            throw new ValidationException("El departamento es obligatorio para docentes");
        }

        // ✅ SINGLE_TABLE: NO validamos que no tenga programa
        // porque el usuario podría ser también estudiante

        validateDepartamentoDocente(departamento);
    }

    /**
     * Validaciones específicas del departamento para docentes
     */
    private void validateDepartamentoDocente(EnumDepartamento departamento) {
        // Validaciones específicas para docentes
        // Por ejemplo, restricciones por facultad o departamento
        log.debug("Departamento válido para docente: {}", departamento);

        // Ejemplo de validación futura:
        // if (!departamentoService.isDepartamentoActivo(departamento)) {
        //     throw new ValidationException("El departamento " + departamento + " no está activo");
        // }
    }
}