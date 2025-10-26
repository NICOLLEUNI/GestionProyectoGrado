package co.unicauca.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import co.unicauca.entity.Anteproyecto;
import java.util.List;

public interface AnteproyectoRepository extends JpaRepository<Anteproyecto,Long> {
    // En AnteproyectoRepository
    List<Anteproyecto> findByProyectoGradoId(Long proyectoGradoId);
}
