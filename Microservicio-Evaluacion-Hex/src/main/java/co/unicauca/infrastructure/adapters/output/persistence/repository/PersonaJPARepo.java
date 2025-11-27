package co.unicauca.infrastructure.adapters.output.persistence.repository;

import co.unicauca.infrastructure.adapters.output.persistence.entities.PersonaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonaJPARepo extends JpaRepository<PersonaEntity,Long> {
    Optional<PersonaEntity> findByEmail(String email);
    List<PersonaEntity> findByDepartmentIgnoreCase(String department);
}
