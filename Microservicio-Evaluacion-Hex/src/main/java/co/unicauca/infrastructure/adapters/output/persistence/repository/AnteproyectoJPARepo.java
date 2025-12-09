package co.unicauca.infrastructure.adapters.output.persistence.repository;

import co.unicauca.infrastructure.adapters.output.persistence.entities.AnteproyectoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnteproyectoJPARepo extends JpaRepository<AnteproyectoEntity,Long> {
}
