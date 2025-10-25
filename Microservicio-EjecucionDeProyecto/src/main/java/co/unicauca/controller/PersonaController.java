package co.unicauca.controller;

import co.unicauca.infra.dto.PersonaRequest;
import co.unicauca.entity.PersonaEntity;
import co.unicauca.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    // Endpoint para crear una nueva persona
    @PostMapping
    public ResponseEntity<PersonaEntity> crearPersona(@RequestBody PersonaRequest request) {
        try {
            // Llamamos al servicio para mapear y guardar la persona
            PersonaEntity persona = personaService.mapAndSavePersona(request);
            return new ResponseEntity<>(persona, HttpStatus.CREATED); // Retorna 201 Created
        } catch (Exception e) {
            // Si ocurre un error, retornamos un error 500 (Internal Server Error)
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
