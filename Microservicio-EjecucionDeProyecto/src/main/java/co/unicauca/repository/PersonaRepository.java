package co.unicauca.repository;

import co.unicauca.entity.PersonaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonaRepository extends JpaRepository<PersonaEntity, Long> {
    // Método que permite buscar personas por una lista de correos electrónicos
    List<PersonaEntity> findAllByEmailIn(List<String> emails);

}
