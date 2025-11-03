package co.unicauca.repository;

import co.unicauca.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Optional<Persona> findByEmail(String email);
    List<Persona> findByDepartmentIgnoreCase(String department);

}
