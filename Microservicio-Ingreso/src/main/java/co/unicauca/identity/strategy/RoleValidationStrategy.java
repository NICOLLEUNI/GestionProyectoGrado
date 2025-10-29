package co.unicauca.identity.strategy;

import co.unicauca.identity.enums.EnumDepartamento;
import co.unicauca.identity.enums.EnumPrograma;
import co.unicauca.identity.enums.enumRol;
import co.unicauca.identity.exception.ValidationException;

/**
 * Interfaz para estrategias de validación específicas por rol - Strategy Pattern
 * NO CAMBIA con SINGLE_TABLE - Misma interfaz
 */
public interface RoleValidationStrategy {

    /**
     * Obtiene el rol que esta estrategia soporta
     */
    enumRol getSupportedRole();

    /**
     * Valida los campos específicos para el rol
     * SINGLE_TABLE: Ahora permite combinaciones de programa/departamento para múltiples roles
     */
    void validate(EnumPrograma programa, EnumDepartamento departamento) throws ValidationException;
}