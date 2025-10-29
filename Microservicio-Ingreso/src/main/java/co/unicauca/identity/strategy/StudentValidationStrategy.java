package co.unicauca.identity.strategy;

import co.unicauca.identity.enums.EnumDepartamento;
import co.unicauca.identity.enums.EnumPrograma;
import co.unicauca.identity.enums.enumRol;
import co.unicauca.identity.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Estrategia de validación para rol ESTUDIANTE - SINGLE_TABLE optimized
 * Ahora permite que estudiantes también tengan otros roles con departamento
 */
@Component
@Slf4j
public class StudentValidationStrategy implements RoleValidationStrategy {

    @Override
    public enumRol getSupportedRole() {
        return enumRol.ESTUDIANTE;
    }

    @Override
    public void validate(EnumPrograma programa, EnumDepartamento departamento) throws ValidationException {
        log.debug("Validando rol ESTUDIANTE con programa: {}", programa);

        // ✅ SINGLE_TABLE: Solo validamos que tenga programa, no nos importa el departamento
        // porque el usuario podría tener otros roles que requieran departamento
        if (programa == null) {
            throw new ValidationException("El programa académico es obligatorio para estudiantes");
        }

        // ✅ SINGLE_TABLE: NO validamos que no tenga departamento
        // porque el usuario podría ser también docente/coordinador/jefe

        validateProgramaEstudiante(programa);
    }

    /**
     * Validaciones específicas del programa para estudiantes
     */
    private void validateProgramaEstudiante(EnumPrograma programa) {
        // Aquí se pueden agregar validaciones específicas del programa
        // Por ejemplo, verificar que el programa esté activo, tenga cupos, etc.
        log.debug("Programa válido para estudiante: {}", programa);

        // Ejemplo de validación futura:
        // if (!programaService.isProgramaActivo(programa)) {
        //     throw new ValidationException("El programa " + programa + " no está activo");
        // }
    }
}