package co.unicauca.repository;

import co.unicauca.entity.SeguimientoProyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SeguimientoProyectoRepository extends JpaRepository<SeguimientoProyecto,Long>{
    // Buscar seguimiento por el ID del proyecto (proveniente de Submission)
    Optional<SeguimientoProyecto> findByIdProyecto(Long idProyecto);
}
