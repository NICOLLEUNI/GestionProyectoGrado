package co.unicauca.controller;

import co.unicauca.infra.dto.AnteproyectoRequest;
import co.unicauca.infra.dto.AnteproyectoResponse;
import co.unicauca.service.facade.AnteproyectoFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para manejar las operaciones sobre los Anteproyectos.
 */
@RestController
@RequestMapping("/api/anteproyectos")
public class AnteproyectoController {

    private final AnteproyectoFacade anteproyectoFacade;

    @Autowired
    public AnteproyectoController(AnteproyectoFacade anteproyectoFacade) {
        this.anteproyectoFacade = anteproyectoFacade;
    }

    /**
     * Crear un nuevo Anteproyecto.
     *
     * @param request Datos del anteproyecto (en formato AnteproyectoRequest)
     * @return Anteproyecto creado
     */
    @PostMapping("/crear")
    public ResponseEntity<AnteproyectoResponse> crearAnteproyecto(@RequestBody AnteproyectoRequest request) {
        AnteproyectoResponse response = anteproyectoFacade.crearAnteproyecto(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtener todos los anteproyectos registrados.
     *
     * @return Lista de anteproyectos
     */


    /**
     * Obtener un anteproyecto por su ID.
     *
     * @param id Identificador del anteproyecto
     * @return Anteproyecto encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<AnteproyectoResponse> obtenerPorId(@PathVariable Long id) {
        AnteproyectoResponse anteproyecto = anteproyectoFacade.obtenerPorId(id);
        return ResponseEntity.ok(anteproyecto);
    }
}
