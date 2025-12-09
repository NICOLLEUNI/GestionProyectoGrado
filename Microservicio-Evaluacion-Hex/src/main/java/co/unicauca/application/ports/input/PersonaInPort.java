package co.unicauca.application.ports.input;

import co.unicauca.domain.entities.Persona;
import co.unicauca.infrastructure.dto.request.PersonaRequest;

import java.util.List;
import java.util.Optional;

public interface PersonaInPort {
    Persona guardarPersona(PersonaRequest request);
    List<Persona> findAll();
    Optional<Persona> findPersonaByEmail(String email);
    List<Persona> listarDocentesDisponiblesParaEvaluar(Long idFormatoA);
}