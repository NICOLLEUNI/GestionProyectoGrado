package co.unicauca.controller;

import co.unicauca.entity.Persona;
import co.unicauca.infra.dto.PersonaRequest;
import co.unicauca.service.PersonaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
@CrossOrigin(origins = "*")
@Tag(name = "Personas", description = "Operaciones CRUD relacionadas con personas")
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    @Operation(
            summary = "Guardar una nueva persona",
            description = "Crea una persona a partir de los datos enviados en el cuerpo de la petición."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Persona creada correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Persona.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos en la solicitud"
            )
    })
    @PostMapping
    public ResponseEntity<Persona> guardarPersona(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para crear la persona",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = PersonaRequest.class)
                    )
            )
            PersonaRequest request
    ) {
        Persona persona = personaService.guardarPersona(request);
        return ResponseEntity.ok(persona);
    }

    @Operation(
            summary = "Obtener todas las personas",
            description = "Retorna una lista con todas las personas almacenadas."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado obtenido correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Persona.class))
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<Persona>> obtenerTodas() {
        List<Persona> personas = personaService.findAll();
        return ResponseEntity.ok(personas);
    }
}
