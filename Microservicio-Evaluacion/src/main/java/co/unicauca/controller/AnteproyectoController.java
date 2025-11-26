package co.unicauca.controller;

import co.unicauca.entity.Anteproyecto;
import co.unicauca.infra.dto.*;
import co.unicauca.infra.dto.notificacion.AnteproyectoResponseNotificacion;
import co.unicauca.service.facade.AnteproyectoFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar los anteproyectos.
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
     * Crea un nuevo anteproyecto.
     *
     * @param request datos del anteproyecto (DTO).
     * @return el anteproyecto creado.
     */
    @PostMapping
    public ResponseEntity<AnteproyectoResponse> crearAnteproyecto(@RequestBody AnteproyectoRequest request) {
        AnteproyectoResponse response = anteproyectoFacade.crearAnteproyecto(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene la lista de todos los anteproyectos.
     *
     * @return lista de anteproyectos en formato de respuesta.
     */
    @GetMapping
    public ResponseEntity<List<AnteproyectoResponse>> listarAnteproyectos() {
        List<AnteproyectoResponse> lista = anteproyectoFacade.listarAnteproyectos();
        return ResponseEntity.ok(lista);
    }

    /**
     * Busca un anteproyecto por su ID.
     *
     * @param id identificador del anteproyecto.
     * @return el anteproyecto encontrado o un error si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AnteproyectoResponse> buscarPorId(@PathVariable Long id) {
        AnteproyectoResponse response = anteproyectoFacade.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/evaluadores")
    public ResponseEntity<AnteproyectoResponseNotificacion> asignarEvaluadores(
            @PathVariable Long id,
            @RequestParam String email1,
            @RequestParam String email2
    ) {
        anteproyectoFacade.asignarEvaluadores(id, email1, email2);
        return ResponseEntity.ok().build();
    }
}