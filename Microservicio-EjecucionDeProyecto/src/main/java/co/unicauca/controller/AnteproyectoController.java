package co.unicauca.controller;

import co.unicauca.infra.dto.AnteproyectoRequest;
import co.unicauca.infra.dto.AnteproyectoResponse;
import co.unicauca.service.AnteproyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/anteproyectos")
@CrossOrigin(origins = "*")
public class AnteproyectoController {

    @Autowired
    private AnteproyectoService anteproyectoService;

    @PostMapping("/crear")
    public ResponseEntity<?> crearAnteproyecto(@RequestBody AnteproyectoRequest request) {
        try {
            System.out.println("📨 Recibiendo petición para crear anteproyecto:");
            System.out.println("   Título: " + request.titulo());
            System.out.println("   Proyecto Grado ID: " + request.idProyectoGrado());

            AnteproyectoResponse response = anteproyectoService.crearAnteproyecto(request);

            System.out.println("✅ Anteproyecto creado exitosamente: " + response.id());
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            // Manejar error de "Proyecto no encontrado"
            if (e.getMessage() != null && e.getMessage().contains("Proyecto no encontrado")) {
                System.err.println("❌ ERROR: " + e.getMessage());
                return ResponseEntity.badRequest().body(
                        Map.of(
                                "error", "No se puede crear el anteproyecto",
                                "detalle", e.getMessage(),
                                "tipo", "PROYECTO_NO_ENCONTRADO"
                        )
                );
            }
            throw e;

        } catch (Exception e) {
            System.err.println("❌ ERROR inesperado: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(
                    Map.of("error", "Error interno del servidor")
            );
        }
    }

    // Puedes agregar otros endpoints si los necesitas
    @GetMapping("/{id}")
    public ResponseEntity<AnteproyectoResponse> obtenerPorId(@PathVariable Long id) {
        try {
            AnteproyectoResponse response = anteproyectoService.buscarPorId(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}