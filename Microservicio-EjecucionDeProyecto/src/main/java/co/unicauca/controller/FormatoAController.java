package co.unicauca.controller;

import co.unicauca.infra.dto.FormatoAVersionRequest;
import co.unicauca.infra.dto.FormatoAVersionResponse;
import co.unicauca.service.facade.FormatoAVersionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/formatos-version")
public class FormatoAController {

    private final FormatoAVersionFacade formatoAVersionFacade;

    @Autowired
    public FormatoAController(FormatoAVersionFacade formatoAVersionFacade) {
        this.formatoAVersionFacade = formatoAVersionFacade;
    }

    /**
     * Crear una nueva versión de FormatoA
     */
    @PostMapping
    public ResponseEntity<FormatoAVersionResponse> crearVersion(@RequestBody FormatoAVersionRequest request) {
        FormatoAVersionResponse response = formatoAVersionFacade.crearVersion(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtener una versión de FormatoA por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FormatoAVersionResponse> obtenerPorId(@PathVariable Long id) {
        FormatoAVersionResponse formato = formatoAVersionFacade.obtenerPorId(id);
        return ResponseEntity.ok(formato);
    }

    /**
     * Listar todas las versiones de FormatoA.
     */
    @GetMapping
    public ResponseEntity<List<FormatoAVersionResponse>> listarTodos() {
        List<FormatoAVersionResponse> formatos = formatoAVersionFacade.listarTodas();
        return ResponseEntity.ok(formatos);
    }


    /**
     * Actualizar una versión existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FormatoAVersionResponse> actualizarVersion(
            @PathVariable Long id,
            @RequestBody FormatoAVersionRequest request) {
        FormatoAVersionResponse response = formatoAVersionFacade.actualizarVersion(id, request);
        return ResponseEntity.ok(response);
    }
}