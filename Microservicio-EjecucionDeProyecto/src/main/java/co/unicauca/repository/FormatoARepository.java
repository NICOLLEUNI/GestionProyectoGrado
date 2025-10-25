package co.unicauca.repository;

import co.unicauca.entity.FormatoAEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FormatoARepository extends JpaRepository<FormatoAEntity, Long> {

    Optional<FormatoAEntity> findById(Long id);
}
