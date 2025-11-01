package co.unicauca.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import co.unicauca.entity.Anteproyecto;
import java.util.List;
import java.util.Optional;

public interface AnteproyectoRepository extends JpaRepository<Anteproyecto,Long> {

    // ✅ Para buscar UN solo anteproyecto (retorna Optional)
    Optional<Anteproyecto> findByProyectoGradoId(Long proyectoGradoId);

    // ✅ Para buscar TODOS los anteproyectos de un proyecto (nombre diferente)
    List<Anteproyecto> findAllByProyectoGradoId(Long proyectoGradoId);
}
