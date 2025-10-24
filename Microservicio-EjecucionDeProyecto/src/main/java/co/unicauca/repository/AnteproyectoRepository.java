package co.unicauca.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import co.unicauca.entity.AnteproyectoEntity;
import java.util.List;

public interface AnteproyectoRepository extends JpaRepository<AnteproyectoEntity,Long> {
    List<AnteproyectoEntity> findByProyectoGrado_Id(Long proyectoId);
}
