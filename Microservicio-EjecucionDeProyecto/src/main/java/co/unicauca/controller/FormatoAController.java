package co.unicauca.controller;

import co.unicauca.infra.dto.FormatoAVersionRequest;
import co.unicauca.infra.dto.FormatoAVersionResponse;
import co.unicauca.service.FormatoAVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/formatos-a")
@CrossOrigin(origins = "*")
public class FormatoAController {

    @Autowired
    private FormatoAVersionService formatoAVersionService;

    /**
     * ✅ CREAR NUEVA VERSIÓN DE FORMATO A
     * - Crea la versión en la base de datos
     * - Publica evento a RabbitMQ
     */
    @PostMapping("/versiones/crear")
    public ResponseEntity<?> crearVersion(@RequestBody FormatoAVersionRequest request) {
        try {
            System.out.println("📨 Recibiendo petición para crear versión:");
            System.out.println("   Título: " + request.title());
            System.out.println("   FormatoA ID: " + request.IdFormatoA());
            System.out.println("   Versión: " + request.numVersion());

            FormatoAVersionResponse response = formatoAVersionService.crearVersion(request);

            System.out.println("✅ Versión creada exitosamente: " + response.id());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ ERROR creando versión: " + e.getMessage());
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Error al crear versión: " + e.getMessage())
            );
        }
    }

    /**
     * ✅ LISTAR TODAS LAS VERSIONES
     */
    @GetMapping("/versiones")
    public ResponseEntity<List<FormatoAVersionResponse>> listarTodas() {
        try {
            List<FormatoAVersionResponse> versiones = formatoAVersionService.listarTodas();
            return ResponseEntity.ok(versiones);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * ✅ OBTENER VERSIÓN POR ID
     */
    @GetMapping("/versiones/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            FormatoAVersionResponse version = formatoAVersionService.buscarPorId(id);
            return ResponseEntity.ok(version);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Error al buscar versión: " + e.getMessage())
            );
        }
    }

    /**
     * ✅ BUSCAR VERSIONES POR FORMATO A (ID externo)
     * Útil para encontrar versiones relacionadas a un proyecto específico
     */
    @GetMapping("/versiones/formato/{formatoAId}")
    public ResponseEntity<?> buscarPorFormatoA(@PathVariable Long formatoAId) {
        try {
            List<FormatoAVersionResponse> versiones = formatoAVersionService.buscarPorFormatoA(formatoAId);
            return ResponseEntity.ok(versiones);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Error al buscar versiones: " + e.getMessage())
            );
        }
    }

    /**
     * ✅ ACTUALIZAR VERSIÓN EXISTENTE
     */
    @PutMapping("/versiones/{id}")
    public ResponseEntity<?> actualizarVersion(@PathVariable Long id, @RequestBody FormatoAVersionRequest request) {
        try {
            FormatoAVersionResponse response = formatoAVersionService.actualizarVersion(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Error al actualizar versión: " + e.getMessage())
            );
        }
    }
}