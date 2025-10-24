package co.unicauca.repository;

import co.unicauca.entity.ProyectoGrado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProyectoRepository extends JpaRepository<ProyectoGrado, Long> {
    /**
     * Consulta personalizada con @Query - JPQL
     * JPQL trabaja con NOMBRES DE CLASES y ATRIBUTOS, no con tablas y columnas
     */
    @Query("SELECT COUNT(p) > 0 FROM ProyectoGrado p WHERE :emailEstudiante MEMBER OF p.estudiantesEmail AND p.estado != :estado")
    boolean existsByEstudiantesEmailContainsAndEstadoNot(@Param("emailEstudiante") String emailEstudiante,
                                                         @Param("estado") String estado);
}
