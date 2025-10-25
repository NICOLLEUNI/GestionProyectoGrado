package co.unicauca.controller;

import co.unicauca.entity.ProyectoGradoEntity;
import co.unicauca.service.EjecucionProyectoGradoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para gestionar la ejecución de proyectos de grado
 */
@RestController
@RequestMapping("/api/ejecucionProyecto")
public class EjecucionProyectoController {

    private final EjecucionProyectoGradoService ejecucionProyectoGradoService;

    @Autowired
    public EjecucionProyectoController(EjecucionProyectoGradoService ejecucionProyectoGradoService) {
        this.ejecucionProyectoGradoService = ejecucionProyectoGradoService;
    }

    /**
     * Endpoint para crear un nuevo ProyectoGrado a partir de un FormatoA y su versión.
     * Recibe los IDs de FormatoA y la versión para crear el ProyectoGrado.
     *
     * @param formatoAId ID del FormatoA
     * @param versionId ID de la versión de FormatoA
     * @return ProyectoGradoEntity con la información del proyecto recién creado
     */
    @PostMapping("/crear")
    public ResponseEntity<ProyectoGradoEntity> crearProyectoGrado(
            @RequestParam Long formatoAId,
            @RequestParam Long versionId) {

        // Llamamos al servicio para crear el ProyectoGrado
        ProyectoGradoEntity proyectoGrado = ejecucionProyectoGradoService.crearProyectoGrado(formatoAId, versionId);

        // Devolvemos una respuesta con el proyecto creado
        return ResponseEntity.ok(proyectoGrado);
    }
}
