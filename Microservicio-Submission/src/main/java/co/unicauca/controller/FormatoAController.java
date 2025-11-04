package co.unicauca.controller;

import co.unicauca.entity.FormatoA;
import co.unicauca.entity.Persona;
import co.unicauca.infra.dto.FormatoAEditRequest;
import co.unicauca.infra.dto.FormatoARequest;
import co.unicauca.repository.FormatoARepository;
import co.unicauca.service.FormatoAService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/formatoA")
public class FormatoAController {

    private final FormatoAService formatoAService;
    private final FormatoARepository formatoARepository;

    @Autowired
    public FormatoAController(FormatoAService formatoAService, FormatoARepository formatoARepository) {
        this.formatoAService = formatoAService;
        this.formatoARepository = formatoARepository;
    }

    @GetMapping
    public ResponseEntity<List<FormatoA>> listarFormatosA() {
        List<FormatoA> formatos = formatoAService.findAll();
        return ResponseEntity.ok(formatos);
    }
    /**
     * Elimina un FormatoA por ID.
     * Endpoint: DELETE /api/formatoA/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarFormatoA(@PathVariable Long id) {
        boolean eliminado = formatoAService.eliminarFormatoA(id);
        if (eliminado) {
            return ResponseEntity.ok("✅ FormatoA con id " + id + " eliminado correctamente, junto con proyecto y versiones asociadas.");
        } else {
            return ResponseEntity.status(404).body("❌ No se encontró FormatoA con id " + id);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormatoA> findById(@PathVariable Long id) {
        return formatoAService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    /**
     * Endpoint para subir/crear un nuevo Formato A
     */
    @PostMapping
    public ResponseEntity<FormatoA> subirFormatoA(@Valid @RequestBody FormatoA formatoA) {
        FormatoA formatoCreado = formatoAService.subirFormatoA(formatoA);

        System.out.println("🟢 FormatoA devuelto al frontend: " + formatoCreado);
        return ResponseEntity.status(HttpStatus.CREATED).body(formatoCreado);
    }

    @PostMapping("/{id}/publicar")
    public ResponseEntity<FormatoA> publicarFormatoA(@PathVariable Long id) {
        try {
            FormatoA formatoPublicado = formatoAService.publicarFormatoA(id);
            return ResponseEntity.ok(formatoPublicado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    // Lista formatos según docente logueado (por email)
    @GetMapping("/docente/{email}")
    public ResponseEntity<List<FormatoA>> getFormatosPorDocente(@PathVariable String email) {
        try {
            List<FormatoA> formatos = formatoAService.findByProjectManagerEmailOrProjectCoManagerEmail(email);
            return ResponseEntity.ok(formatos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/{id}/pdf")
    public ResponseEntity<String> subirPDF(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        boolean exito = formatoAService.saveFormatoAPDF(id, file);
        if (exito) {
            return ResponseEntity.ok("PDF subido correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir el PDF");
        }
    }

    @PostMapping("/{id}/carta-laboral")
    public ResponseEntity<String> subirCartaLaboral(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        boolean exito = formatoAService.saveCartaLaboral(id, file);
        if (exito) {
            return ResponseEntity.ok("Carta laboral subida correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir la carta laboral");
        }
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
    /*
    @GetMapping("/docente/{emailDocente}")
    public ResponseEntity<List<FormatoA>> listarFormatosPorDocente(
            @PathVariable String emailDocente) {

        List<FormatoA> formatos = formatoAService.listarFormatosAPorDocente(emailDocente);
        return ResponseEntity.ok(formatos);
    }*/
}