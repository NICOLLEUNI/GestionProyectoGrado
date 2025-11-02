package co.unicauca.identity.strategy;

import co.unicauca.identity.enums.EnumDepartamento;
import co.unicauca.identity.enums.EnumPrograma;
import co.unicauca.identity.enums.enumRol;
import co.unicauca.identity.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * Contexto para orquestar las estrategias de validación - Strategy Pattern
 * SINGLE_TABLE: Maneja validaciones para múltiples roles simultáneos
 */
@Component
@Slf4j
public class RoleValidationContext {

    private final Map<enumRol, RoleValidationStrategy> strategies;

    public RoleValidationContext(Set<RoleValidationStrategy> strategySet) {
        this.strategies = new EnumMap<>(enumRol.class);
        strategySet.forEach(strategy ->
                strategies.put(strategy.getSupportedRole(), strategy)
        );
        log.info("Contexto de validación inicializado con {} estrategias", strategies.size());
    }

    /**
     * Valida todos los campos específicos según los roles proporcionados
     * SINGLE_TABLE: Permite validar múltiples roles en una sola entidad
     */
    public void validateRoles(Set<enumRol> roles, EnumPrograma programa, EnumDepartamento departamento) {
        if (roles == null || roles.isEmpty()) {
            throw new ValidationException("Debe seleccionar al menos un rol");
        }

        log.debug("Validando roles: {} con programa: {} y departamento: {}", roles, programa, departamento);

        // Validar cada rol individualmente
        for (enumRol rol : roles) {
            RoleValidationStrategy strategy = strategies.get(rol);
            if (strategy != null) {
                strategy.validate(programa, departamento);
            } else {
                log.warn("No se encontró estrategia de validación para el rol: {}", rol);
            }
        }

        // Validaciones cruzadas entre roles
        validateCrossRoleConstraints(roles, programa, departamento);

        log.debug("Validaciones de rol exitosas para: {}", roles);
    }

    /**
     * Valida restricciones entre múltiples roles
     * SINGLE_TABLE: Valida combinaciones permitidas de roles
     */
    private void validateCrossRoleConstraints(Set<enumRol> roles, EnumPrograma programa, EnumDepartamento departamento) {
        // ✅ CORREGIDO: Validar que si es estudiante (solo o en combinación), tenga programa
        if (roles.contains(enumRol.ESTUDIANTE) && programa == null) {
            throw new ValidationException("El programa es obligatorio cuando se selecciona el rol ESTUDIANTE, incluso en combinación con otros roles");
        }

        // ✅ CORREGIDO: Validar que si es docente, coordinador o jefe (solo o en combinación), tenga departamento
        boolean requiereDepartamento = roles.contains(enumRol.DOCENTE) ||
                roles.contains(enumRol.COORDINADOR) ||
                roles.contains(enumRol.JEFE_DEPARTAMENTO);

        if (requiereDepartamento && departamento == null) {
            throw new ValidationException("El departamento es obligatorio cuando se seleccionan roles de DOCENTE, COORDINADOR o JEFE_DEPARTAMENTO, incluso en combinación con otros roles");
        }

        // ✅ CORREGIDO: Validación adicional para combinaciones específicas
        validateSpecificRoleCombinations(roles, programa, departamento);
    }

    /**
     * Valida combinaciones específicas de roles (puede extenderse según reglas de negocio)
     */
    private void validateSpecificRoleCombinations(Set<enumRol> roles, EnumPrograma programa, EnumDepartamento departamento) {
        // Validar que en combinaciones, ambos campos estén presentes cuando corresponda
        boolean tieneEstudiante = roles.contains(enumRol.ESTUDIANTE);
        boolean tieneRolConDepartamento = roles.contains(enumRol.DOCENTE) ||
                roles.contains(enumRol.COORDINADOR) ||
                roles.contains(enumRol.JEFE_DEPARTAMENTO);

        // Si tiene ambos tipos de roles, ambos campos deben estar presentes
        if (tieneEstudiante && tieneRolConDepartamento) {
            if (programa == null && departamento == null) {
                throw new ValidationException("Para la combinación de roles seleccionada, ambos campos (programa y departamento) son obligatorios");
            } else if (programa == null) {
                throw new ValidationException("El programa es obligatorio cuando se combina el rol ESTUDIANTE con otros roles");
            } else if (departamento == null) {
                throw new ValidationException("El departamento es obligatorio cuando se combinan roles de docente/coordinador/jefe con otros roles");
            }
        }

        // Log para debugging
        log.debug("Combinación de roles válida - Estudiante: {}, Roles con Depto: {}, Programa: {}, Departamento: {}",
                tieneEstudiante, tieneRolConDepartamento, programa, departamento);
    }




    /**
     * Obtiene la estrategia para un rol específico
     */
    public RoleValidationStrategy getStrategy(enumRol rol) {
        return strategies.get(rol);
    }

    /**
     * Verifica si existe estrategia para un rol
     */
    public boolean hasStrategy(enumRol rol) {
        return strategies.containsKey(rol);
    }
}