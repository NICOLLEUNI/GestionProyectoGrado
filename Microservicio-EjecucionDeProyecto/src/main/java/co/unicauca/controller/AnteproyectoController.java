package co.unicauca.controller;

import co.unicauca.infra.dto.AnteproyectoResponse;
import co.unicauca.service.AnteproyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para manejar las solicitudes relacionadas con Anteproyectos.
 */
@RestController
@RequestMapping("/api/anteproyecto")
public class AnteproyectoController {

    private final AnteproyectoService anteproyectoService;

    @Autowired
    public AnteproyectoController(AnteproyectoService anteproyectoService) {
        this.anteproyectoService = anteproyectoService;
    }

    /**
     * Endpoint para guardar un Anteproyecto.
     * Recibe un objeto AnteproyectoResponse con los datos del anteproyecto.
     *
     * @param response Objeto que contiene la información para crear el Anteproyecto
     * @return ResponseEntity con el estado de la operación
     */
    @PostMapping("/guardar")
    public ResponseEntity<String> guardarAnteproyecto(@RequestBody AnteproyectoResponse response) {
        try {
            anteproyectoService.saveInterno(response);
            return ResponseEntity.ok("Anteproyecto guardado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Error al guardar el anteproyecto: " + e.getMessage());
        }
    }
}
