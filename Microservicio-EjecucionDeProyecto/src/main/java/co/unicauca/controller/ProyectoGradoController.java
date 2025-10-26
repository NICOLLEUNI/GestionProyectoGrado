package co.unicauca.controller;

import co.unicauca.infra.dto.ProyectoGradoRequest;
import co.unicauca.infra.dto.ProyectoGradoResponse;
import co.unicauca.service.facade.ProyectoGradoFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para manejar operaciones relacionadas con los Proyectos de Grado.
 */
@RestController
@RequestMapping("/api/proyectos-grado")
public class ProyectoGradoController {

    private final ProyectoGradoFacade proyectoGradoFacade;

    @Autowired
    public ProyectoGradoController(ProyectoGradoFacade proyectoGradoFacade) {
        this.proyectoGradoFacade = proyectoGradoFacade;
    }

    /**
     * 🔹 Crea un nuevo Proyecto de Grado.
     *
     * @param request DTO con la información del proyecto.
     * @return ProyectoGradoResponse con los datos guardados.
     */
    @PostMapping("/crear")
    public ResponseEntity<ProyectoGradoResponse> crearProyectoGrado(@RequestBody ProyectoGradoRequest request) {
        // Llamada al Facade con el DTO correcto
        ProyectoGradoResponse response = proyectoGradoFacade.crearProyecto(request);
        return ResponseEntity.ok(response);
    }
}


