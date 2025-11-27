package co.unicauca.application.ports.output;


import co.unicauca.domain.entities.Persona;
import java.util.List;
import java.util.Optional;

public interface PersonaRepoOutPort {
    Persona save(Persona persona);
    List<Persona> findAll();
    Optional<Persona> findById(Long id);
    Optional<Persona> findByEmail(String email);
    List<Persona> findByDepartmentIgnoreCase(String department);
}
