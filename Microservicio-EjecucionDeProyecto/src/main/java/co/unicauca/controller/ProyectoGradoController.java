package co.unicauca.controller;

import co.unicauca.infra.dto.ProyectoGradoResponse;
import co.unicauca.entity.ProyectoGradoEntity;
import co.unicauca.service.ProyectoGradoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos-grado")
public class ProyectoGradoController {

    @Autowired
    private ProyectoGradoService proyectoGradoService;

    /**
     * Endpoint para crear un nuevo ProyectoGrado
     *
     * @param response DTO con los datos del ProyectoGrado
     * @return ResponseEntity con el estado de la operación
     */
    @PostMapping
    public ResponseEntity<String> crearProyectoGrado(@RequestBody ProyectoGradoResponse response) {
        try {
            // Llamamos al servicio para guardar el ProyectoGrado internamente
            proyectoGradoService.saveInterno(response);
            return new ResponseEntity<>("Proyecto de Grado creado exitosamente", HttpStatus.CREATED);
        } catch (Exception e) {
            // Si ocurre un error, retornamos un error 500 (Internal Server Error)
            return new ResponseEntity<>("Error al crear el Proyecto de Grado: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}