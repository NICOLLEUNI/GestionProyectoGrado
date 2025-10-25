package co.unicauca.controller;

import co.unicauca.infra.dto.FormatoAUpdateResponse;
import co.unicauca.service.FormatoAVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para manejar las versiones de FormatoA.
 */
@RestController
@RequestMapping("/api/formatoA/version")
public class FormatoAVersionController {

    private final FormatoAVersionService formatoAVersionService;

    @Autowired
    public FormatoAVersionController(FormatoAVersionService formatoAVersionService) {
        this.formatoAVersionService = formatoAVersionService;
    }

    /**
     * Endpoint para guardar una nueva versión de FormatoA.
     * Recibe los datos de la nueva versión a través del FormatoAUpdateResponse.
     *
     * @param response Contiene la información necesaria para crear una nueva versión de FormatoA
     * @return ResponseEntity con el estado de la operación
     */
    @PostMapping("/guardar")
    public ResponseEntity<String> guardarVersion(@RequestBody FormatoAUpdateResponse response) {
        try {
            formatoAVersionService.saveInterno(response);
            return ResponseEntity.ok("Versión de FormatoA guardada exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Error al guardar la versión: " + e.getMessage());
        }
    }
}
