package co.unicauca.controller;

import co.unicauca.infra.dto.FormatoAResponse;
import co.unicauca.service.FormatoAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para manejar las solicitudes relacionadas con FormatoA.
 */
@RestController
@RequestMapping("/api/formatoA")
public class FormatoAController {

    private final FormatoAService formatoAService;

    @Autowired
    public FormatoAController(FormatoAService formatoAService) {
        this.formatoAService = formatoAService;
    }

    /**
     * Endpoint para guardar un nuevo FormatoA.
     * Recibe un objeto FormatoAResponse con los datos para crear el FormatoA.
     *
     * @param response Objeto que contiene la información del FormatoA
     * @return ResponseEntity con el estado de la operación
     */
    @PostMapping("/guardar")
    public ResponseEntity<String> guardarFormatoA(@RequestBody FormatoAResponse response) {
        try {
            formatoAService.saveFormatoA(response);
            return ResponseEntity.ok("Formato A guardado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Error al guardar el Formato A: " + e.getMessage());
        }
    }


}
