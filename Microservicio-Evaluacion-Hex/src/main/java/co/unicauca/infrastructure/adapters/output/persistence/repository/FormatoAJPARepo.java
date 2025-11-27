package co.unicauca.infrastructure.adapters.output.persistence.repository;

import co.unicauca.infrastructure.adapters.output.persistence.entities.FormatoAEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormatoAJPARepo extends JpaRepository<FormatoAEntity, Long> {

}
