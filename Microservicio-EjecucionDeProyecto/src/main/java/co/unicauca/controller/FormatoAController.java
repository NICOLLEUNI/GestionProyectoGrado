package co.unicauca.controller;

import co.unicauca.infra.dto.FormatoARequest;
import co.unicauca.infra.dto.FormatoAResponse;
import co.unicauca.service.FormatoAService;
import co.unicauca.service.facade.FormatoAFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para manejar las solicitudes relacionadas con FormatoA.
 */
@RestController
@RequestMapping("/api/formatoA")
public class FormatoAController {

    private final FormatoAFacade formatoAFacade;

    @Autowired
    public FormatoAController(FormatoAFacade formatoAFacade) {
        this.formatoAFacade = formatoAFacade;
    }


    /**
     * Endpoint para guardar un nuevo FormatoA.
     * Recibe un objeto FormatoAResponse con los datos para crear el FormatoA.
     *
     * @param request Objeto que contiene la información del FormatoA
     * @return ResponseEntity con el estado de la operación
     */
    @PostMapping("/guardar")
    public ResponseEntity<FormatoAResponse> crearFormatoA(@RequestBody FormatoARequest request) {
        FormatoAResponse response = formatoAFacade.crearFormatoA(request);
        return ResponseEntity.ok(response);
    }
    }



