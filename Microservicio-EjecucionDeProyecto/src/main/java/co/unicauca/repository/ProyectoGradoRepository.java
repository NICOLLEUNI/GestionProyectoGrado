package co.unicauca.repository;

import co.unicauca.entity.ProyectoGrado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProyectoGradoRepository extends JpaRepository<ProyectoGrado, Long> {

    boolean existsByNombre(String nombre);

    // ✅ CORREGIDO: Usa el nombre correcto del campo
    Optional<ProyectoGrado> findByIdFormatoA(Long idFormatoA);

    // ✅ Buscar por estado
    @Query("SELECT p FROM ProyectoGrado p WHERE p.estado = :estado")
    List<ProyectoGrado> findByEstado(@Param("estado") String estado);

    @Query("SELECT DISTINCT p FROM ProyectoGrado p LEFT JOIN FETCH p.estudiantesEmail")
    List<ProyectoGrado> findAllWithEstudiantes();

    Optional<ProyectoGrado> findByEstudiantesEmailContaining(String email);
}