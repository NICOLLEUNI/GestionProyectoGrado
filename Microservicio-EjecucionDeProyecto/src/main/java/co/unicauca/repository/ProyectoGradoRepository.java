package co.unicauca.repository;

import co.unicauca.entity.ProyectoGradoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProyectoGradoRepository extends JpaRepository<ProyectoGradoEntity, String> {

    @Query("""
        SELECT p FROM ProyectoGradoEntity p
        LEFT JOIN FETCH p.anteproyecto
        LEFT JOIN FETCH p.formatoAActual f
        LEFT JOIN FETCH f.versiones
        LEFT JOIN FETCH p.personas
        WHERE p.id = :id
    """)
    Optional<ProyectoGradoEntity> findByIdWithAllRelations(@Param("id") Long id);
}
