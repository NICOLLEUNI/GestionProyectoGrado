package co.unicauca.repository;

import co.unicauca.entity.PersonaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonaRepository extends JpaRepository<PersonaEntity, Long> {
    List<PersonaEntity> findByEmailIn(List<String> emails);

}
