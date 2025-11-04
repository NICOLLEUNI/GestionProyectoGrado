package co.unicauca.controller;


import co.unicauca.entity.EnumRol;
import co.unicauca.entity.FormatoA;
import co.unicauca.entity.Persona;
import co.unicauca.infra.dto.PersonaRequest;
import co.unicauca.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar operaciones sobre Personas.
 * Actualmente permite registrar o actualizar información recibida desde otros microservicios.
 */
@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    private final PersonaService personaService;

    @Autowired
    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    /**
     * Endpoint para registrar o actualizar una Persona.
     * Si la persona ya existe, se actualizan sus datos.
     *
     * @param request Datos de la persona recibidos desde otro microservicio.
     * @return La persona almacenada en la base de datos.
     */
    @PostMapping
    public ResponseEntity<Persona> guardarPersona(@RequestBody PersonaRequest request) {
        Persona personaGuardada = personaService.guardarPersona(request);
        return ResponseEntity.ok(personaGuardada);
    }

    @GetMapping
    public ResponseEntity<List<Persona>> listarPersona() {
        List<Persona> personas = personaService.findAll();
        return ResponseEntity.ok(personas);
    }

    // 🔹 NUEVO: Listar personas según el rol (EnumRol)
    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<Persona>> listarPorRol(@PathVariable EnumRol rol) {
        List<Persona> personas = personaService.findByRol(rol);
        return ResponseEntity.ok(personas);
    }
    @GetMapping("/email/{email}")
    public ResponseEntity<Persona> findByEmail(@PathVariable String email) {
        Persona persona = personaService.findPersonaByEmail(email);
        if (persona == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(persona);
    }
    @GetMapping("/estudiantes/sin-formatoA")
    public ResponseEntity<List<Persona>> listarEstudiantesSinFormatoA() {
        List<Persona> estudiantes = personaService.findEstudiantesSinFormatoA();
        return ResponseEntity.ok(estudiantes);
    }

}