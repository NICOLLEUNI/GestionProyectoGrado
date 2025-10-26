package co.unicauca.controller;

import co.unicauca.entity.Persona;
import co.unicauca.infra.dto.PersonaRequest;
import co.unicauca.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
@CrossOrigin(origins = "*")
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    @PostMapping
    public ResponseEntity<Persona> guardarPersona(@RequestBody PersonaRequest request) {
        Persona persona = personaService.guardarPersona(request);
        return ResponseEntity.ok(persona);
    }

    @GetMapping
    public ResponseEntity<List<Persona>> obtenerTodas() {
        List<Persona> personas = personaService.findAll();
        return ResponseEntity.ok(personas);
    }
}