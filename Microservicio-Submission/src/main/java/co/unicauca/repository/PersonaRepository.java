package co.unicauca.repository;

import co.unicauca.entity.EnumRol;
import co.unicauca.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Optional<Persona> findByEmail(String email);

    Optional<Persona> findByRoles(Set<EnumRol> roles);

    List<Persona> findByRolesContaining(EnumRol rol);
    @Query("SELECT p FROM Persona p WHERE :rol MEMBER OF p.roles " +
            "AND p.email NOT IN (SELECT e FROM FormatoA f JOIN f.estudianteEmails e)")
    List<Persona> findEstudiantesNoAsociados(EnumRol rol);


}
