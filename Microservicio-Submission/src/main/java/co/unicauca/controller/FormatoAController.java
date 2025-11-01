package co.unicauca.controller;

import co.unicauca.entity.FormatoA;
import co.unicauca.infra.dto.FormatoAEditRequest;
import co.unicauca.infra.dto.FormatoARequest;
import co.unicauca.service.FormatoAService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/formatoA")
public class FormatoAController {

    private final FormatoAService formatoAService;

    @Autowired
    public FormatoAController(FormatoAService formatoAService) {
        this.formatoAService = formatoAService;
    }

    /**
     * Endpoint para subir/crear un nuevo Formato A
     */
    @PostMapping
    public ResponseEntity<FormatoA> subirFormatoA(@Valid @RequestBody FormatoA formatoA) {
        FormatoA formatoCreado = formatoAService.subirFormatoA(formatoA);
        return ResponseEntity.status(HttpStatus.CREATED).body(formatoCreado);
    }

    /**
     * Endpoint para actualizar un Formato A cuando es evaluado
     * (viene desde RabbitMQ con la evaluación del coordinador)
     */
    @PutMapping("/evaluacion")
    public ResponseEntity<FormatoA> actualizarFormatoAEvaluado(@Valid @RequestBody FormatoARequest request) {
        FormatoA formatoActualizado = formatoAService.actualizarFormatoAEvaluado(request);
        return ResponseEntity.ok(formatoActualizado);
    }

    /**
     * Endpoint para actualizar un Formato A cuando es editado
     * (viene desde el frontend con la evaluación del coordinador)
     */
    @PutMapping("/reenviar-rechazado")
    public ResponseEntity<FormatoA> reenviarFormatoA(@RequestBody FormatoAEditRequest request) {
        FormatoA formatoActualizado = formatoAService.reenviarFormatoARechazado(request);
        return ResponseEntity.ok(formatoActualizado);
    }


    /**
     * Endpoint para listar todos los Formatos A asociados a un docente
     * (como director o codirector)
     */
    @GetMapping("/docente/{emailDocente}")
    public ResponseEntity<List<FormatoA>> listarFormatosPorDocente(
            @PathVariable String emailDocente) {

        List<FormatoA> formatos = formatoAService.listarFormatosAPorDocente(emailDocente);
        return ResponseEntity.ok(formatos);
    }
}