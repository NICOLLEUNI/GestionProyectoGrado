package co.unicauca.repository;

import co.unicauca.entity.EnumEstado;
import co.unicauca.entity.ProyectoGrado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProyectoRepository extends JpaRepository<ProyectoGrado, Long> {
    /**
     * Consulta personalizada con @Query - JPQL
     * JPQL trabaja con NOMBRES DE CLASES y ATRIBUTOS, no con tablas y columnas
     */
    @Query("SELECT COUNT(p) > 0 FROM ProyectoGrado p JOIN p.estudiantesEmail e WHERE e = :emailEstudiante AND p.estado <> :estado")
    boolean existsByEstudianteEmailAndEstadoNot(@Param("emailEstudiante") String emailEstudiante,
                                                @Param("estado") String estado);

    //Buscar proyecto por ID del FormatoA actual
    @Query("SELECT p FROM ProyectoGrado p WHERE p.formatoAActual.id = :formatoAId")
    Optional<ProyectoGrado> findByFormatoAActualId(@Param("formatoAId") Long formatoAId);

    // Buscar por título del FormatoA y estado APROBADO
    @Query("SELECT p FROM ProyectoGrado p WHERE p.formatoAActual.title = :titulo AND p.formatoAActual.state = :estado")
    Optional<ProyectoGrado> findByFormatoAActualTitleAndFormatoAActualState(
            @Param("titulo") String titulo,
            @Param("estado") EnumEstado estado
    );

    // En ProyectoRepository.java - AGREGAR ESTE MÉTODO
    Optional<ProyectoGrado> findByFormatoAActualTitle(String title);



}
