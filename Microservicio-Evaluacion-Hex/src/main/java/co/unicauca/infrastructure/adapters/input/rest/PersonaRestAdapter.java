package co.unicauca.infrastructure.adapters.input.rest;

import co.unicauca.application.ports.input.PersonaInPort;
import co.unicauca.domain.entities.Persona;
import co.unicauca.infrastructure.dto.request.PersonaRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
public class PersonaRestAdapter {
    private final PersonaInPort personaInPort;

    public PersonaRestAdapter(PersonaInPort personaInPort) {
        this.personaInPort = personaInPort;
    }

    @PostMapping
    public ResponseEntity<Persona> guardarPersona(@RequestBody PersonaRequest request) {
        Persona personaGuardada = personaInPort.guardarPersona(request);
        return ResponseEntity.ok(personaGuardada);
    }

    @GetMapping
    public ResponseEntity<List<Persona>> listarPersona() {
        List<Persona> personas = personaInPort.findAll();
        return ResponseEntity.ok(personas);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Persona> findByEmail(@PathVariable String email) {
        return personaInPort.findPersonaByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/docentesDisponibles/{idFormatoA}")
    public ResponseEntity<List<Persona>> listarDocentesDisponibles(@PathVariable Long idFormatoA) {
        List<Persona> docentes = personaInPort.listarDocentesDisponiblesParaEvaluar(idFormatoA);
        return ResponseEntity.ok(docentes);
    }
}
