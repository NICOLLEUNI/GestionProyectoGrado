package co.unicauca.controller;


import co.unicauca.entity.*;
import co.unicauca.infra.dto.*;
import co.unicauca.service.facade.FormatoAFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/formatoA")
public class FormatoAController {

    private final FormatoAFacade formatoAFacade;

    @Autowired
    public FormatoAController(FormatoAFacade formatoAFacade) {
        this.formatoAFacade = formatoAFacade;
    }

    /**
     * Endpoint para crear un nuevo FormatoA.
     * Recibe un FormatoARequest desde otro microservicio o el frontend.
     *
     * @param request datos del formato A
     * @return FormatoAResponse con la información almacenada
     */
    @PostMapping
    public ResponseEntity<FormatoAResponse> crearFormatoA(@RequestBody FormatoARequest request) {
        FormatoAResponse response = formatoAFacade.crearFormatoA(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista todos los formatos A existentes en la base de datos.
     *
     * @return lista de FormatoAResponse
     */
    @GetMapping
    public ResponseEntity<List<FormatoA>> listarFormatosA() {
        List<FormatoA> formatos = formatoAFacade.listarFormatosA();
        return ResponseEntity.ok(formatos);
    }
    /**
     * Actualiza el estado (por ejemplo: APROBADO o RECHAZADO) de un FormatoA.
     *
     * @param id identificador del FormatoA
     * @param nuevoEstado nuevo estado (EnumEstado)
     * @param observaciones comentarios del coordinador
     * @return FormatoAResponse actualizado
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<FormatoAResponse> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EnumEstado nuevoEstado,
            @RequestParam(required = false) String observaciones
    ) {
        FormatoAResponse response = formatoAFacade.actualizarEstado(id, nuevoEstado, observaciones);
        return ResponseEntity.ok(response);
    }
}