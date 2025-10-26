package co.unicauca.controller;

import co.unicauca.entity.Anteproyecto;
import co.unicauca.service.AnteproyectoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/anteproyectos")
public class AnteproyectoController {

    private final AnteproyectoService anteproyectoService;

    @Autowired
    public AnteproyectoController(AnteproyectoService anteproyectoService) {
        this.anteproyectoService = anteproyectoService;
        System.out.println("✅ ANTEPROYECTO CONTROLLER INICIALIZADO");
    }

    @PostMapping
    public ResponseEntity<Anteproyecto> subirAnteproyecto(@Valid @RequestBody Anteproyecto anteproyecto) {
        System.out.println("📥 RECIBIDO POST /api/anteproyectos - Título: " + anteproyecto.getTitulo());

        try {
            Anteproyecto anteproyectoGuardado = anteproyectoService.subirAnteproyecto(anteproyecto);
            System.out.println("✅ ANTEPROYECTO GUARDADO: " + anteproyectoGuardado.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(anteproyectoGuardado);
        } catch (RuntimeException e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }
}