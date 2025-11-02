package co.unicauca.identity.repository;

import co.unicauca.identity.entity.Persona;
import co.unicauca.identity.enums.EnumDepartamento;
import co.unicauca.identity.enums.EnumPrograma;
import co.unicauca.identity.enums.enumRol;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long>, JpaSpecificationExecutor<Persona> {

    // ✅ MÉTODOS BÁSICOS CORREGIDOS
    Optional<Persona> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);

    // ✅ CONSULTAS CORREGIDAS - SIN HQL COMPLEJO INICIALMENTE
    /**
     * Busca personas que tengan un rol específico
     * SOLUCIÓN TEMPORAL: Usar método derivado en lugar de HQL complejo
     */
    List<Persona> findByRolesContaining(enumRol rol);

    /**
     * Versión paginada - temporalmente sin HQL
     */
    Page<Persona> findByRolesContaining(enumRol rol, Pageable pageable);

    // ✅ MÉTODOS ESPECÍFICOS SIMPLIFICADOS
    /**
     * Busca estudiantes por programa académico - CONSULTA CORREGIDA
     */
    @Query("SELECT p FROM Persona p WHERE p.programa = :programa AND :rol MEMBER OF p.roles")
    List<Persona> findEstudiantesByPrograma(@Param("rol") enumRol rol, @Param("programa") EnumPrograma programa);

    /**
     * Busca por roles y departamento - CONSULTA COMPLETAMENTE CORREGIDA
     */
    @Query("SELECT DISTINCT p FROM Persona p JOIN p.roles r WHERE r IN :roles AND p.departamento = :departamento")
    List<Persona> findByRolesAndDepartamento(@Param("roles") Set<enumRol> roles, @Param("departamento") EnumDepartamento departamento);

    // ✅ MÉTODOS DEFAULT (sin cambios, están bien)
    default List<Persona> findEstudiantesByPrograma(EnumPrograma programa) {
        return findEstudiantesByPrograma(enumRol.ESTUDIANTE, programa);
    }

    default List<Persona> findDocentesByDepartamento(EnumDepartamento departamento) {
        return findByRolesAndDepartamento(Set.of(enumRol.DOCENTE), departamento);
    }

    default List<Persona> findCoordinadoresByDepartamento(EnumDepartamento departamento) {
        return findByRolesAndDepartamento(Set.of(enumRol.COORDINADOR), departamento);
    }

    // ✅ BÚSQUEDAS AVANZADAS - CONSULTAS CORREGIDAS
    @Query("SELECT p FROM Persona p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :nombre, '%')) OR LOWER(p.lastname) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Persona> findByNombreOrApellidoContainingIgnoreCase(@Param("nombre") String nombre);

    @Query("SELECT p FROM Persona p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :nombre, '%')) OR LOWER(p.lastname) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    Page<Persona> findByNombreOrApellidoContainingIgnoreCase(@Param("nombre") String nombre, Pageable pageable);

    // ✅ CONSULTA AVANZADA SIMPLIFICADA TEMPORALMENTE
    @Query("SELECT p FROM Persona p WHERE " +
            "(:query IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.lastname) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.email) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Persona> findByAdvancedSearch(@Param("query") String query,
                                       Pageable pageable);

    // ✅ CONSULTAS ESTADÍSTICAS CORREGIDAS
    @Query("SELECT COUNT(p) FROM Persona p WHERE :rol MEMBER OF p.roles")
    long countByRol(@Param("rol") enumRol rol);

    @Query("SELECT COUNT(p) FROM Persona p WHERE :rol MEMBER OF p.roles AND p.programa = :programa")
    long countEstudiantesByPrograma(@Param("rol") enumRol rol, @Param("programa") EnumPrograma programa);

    // ✅ CONSULTA COUNT CORREGIDA
    @Query("SELECT COUNT(DISTINCT p) FROM Persona p JOIN p.roles r WHERE r IN :roles AND p.departamento = :departamento")
    long countByRolesAndDepartamento(@Param("roles") Set<enumRol> roles, @Param("departamento") EnumDepartamento departamento);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Persona p WHERE :rol MEMBER OF p.roles AND p.departamento = :departamento")
    boolean existsByRolAndDepartamento(@Param("rol") enumRol rol, @Param("departamento") EnumDepartamento departamento);

    // ✅ VALIDACIONES CORREGIDAS
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Persona p WHERE p.email = :email AND p.idUsuario != :idUsuario")
    boolean existsByEmailAndIdNot(@Param("email") String email, @Param("idUsuario") Long idUsuario);

    @Query("SELECT p FROM Persona p WHERE :rol MEMBER OF p.roles AND p.departamento = :departamento")
    Optional<Persona> findCoordinadorByDepartamento(@Param("rol") enumRol rol, @Param("departamento") EnumDepartamento departamento);

    default Optional<Persona> findCoordinadorByDepartamento(EnumDepartamento departamento) {
        return findCoordinadorByDepartamento(enumRol.COORDINADOR, departamento);
    }

    default Optional<Persona> findJefeDepartamentoByDepartamento(EnumDepartamento departamento) {
        return findCoordinadorByDepartamento(enumRol.JEFE_DEPARTAMENTO, departamento);
    }
}