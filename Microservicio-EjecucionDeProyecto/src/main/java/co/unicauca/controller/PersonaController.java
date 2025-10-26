package co.unicauca.controller;

import co.unicauca.infra.dto.PersonaRequest;
import co.unicauca.infra.dto.PersonaResponse;
import co.unicauca.service.facade.PersonaFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personas")
public class PersonaController {

    private final PersonaFacade personaFacade;

    @Autowired
    public PersonaController(PersonaFacade personaFacade) {
        this.personaFacade = personaFacade;
    }

    // ✅ Crear persona
    @PostMapping
    public PersonaResponse crearPersona(@RequestBody PersonaRequest request) {
        return personaFacade.crearPersona(request);
    }

    // ✅ Listar todas las personas
    @GetMapping
    public List<PersonaResponse> listarPersonas() {
        return personaFacade.listarTodas();
    }

}
