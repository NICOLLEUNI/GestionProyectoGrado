package co.unicauca.controller;

import co.unicauca.infra.dto.AnteproyectoRequest;
import co.unicauca.infra.dto.AnteproyectoResponse;
import co.unicauca.service.AnteproyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/anteproyectos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AnteproyectoController {

    private final AnteproyectoService anteproyectoService;

    @PostMapping
    public ResponseEntity<?> crearAnteproyecto(@RequestBody AnteproyectoRequest request) {
        try {
            System.out.println("📨 [CONTROLLER] Creando anteproyecto:");
            System.out.println("   Título: " + request.titulo());
            System.out.println("   Proyecto Grado ID: " + request.idProyectoGrado());
            System.out.println("   Estado: " + request.estado());

            AnteproyectoResponse response = anteproyectoService.crearAnteproyecto(request);

            System.out.println("✅ [CONTROLLER] Anteproyecto creado exitosamente - ID: " + response.id());
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            System.err.println("❌ [CONTROLLER] ERROR: " + e.getMessage());

            if (e.getMessage() != null && e.getMessage().contains("Proyecto no encontrado")) {
                return ResponseEntity.badRequest().body(
                        Map.of(
                                "error", "No se puede crear el anteproyecto",
                                "detalle", e.getMessage(),
                                "tipo", "PROYECTO_NO_ENCONTRADO"
                        )
                );
            }

            return ResponseEntity.badRequest().body(
                    Map.of(
                            "error", "Error al crear anteproyecto",
                            "detalle", e.getMessage()
                    )
            );

        } catch (Exception e) {
            System.err.println("❌ [CONTROLLER] ERROR inesperado: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Error interno del servidor")
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            System.out.println("🔍 [CONTROLLER] Buscando anteproyecto por ID: " + id);

            AnteproyectoResponse response = anteproyectoService.buscarPorId(id);

            System.out.println("✅ [CONTROLLER] Anteproyecto encontrado - ID: " + response.id());
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            System.err.println("❌ [CONTROLLER] Anteproyecto no encontrado - ID: " + id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("❌ [CONTROLLER] ERROR buscando anteproyecto: " + e.getMessage());
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Error interno del servidor")
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarAnteproyecto(@PathVariable Long id, @RequestBody AnteproyectoRequest request) {
        try {
            System.out.println("✏️ [CONTROLLER] Actualizando anteproyecto - ID: " + id);
            System.out.println("   Nuevo título: " + request.titulo());
            System.out.println("   Nuevo estado: " + request.estado());

            AnteproyectoResponse response = anteproyectoService.actualizarAnteproyecto(id, request);

            System.out.println("✅ [CONTROLLER] Anteproyecto actualizado - ID: " + response.id());
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            System.err.println("❌ [CONTROLLER] ERROR actualizando anteproyecto: " + e.getMessage());

            if (e.getMessage() != null && e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.badRequest().body(
                    Map.of(
                            "error", "Error al actualizar anteproyecto",
                            "detalle", e.getMessage()
                    )
            );

        } catch (Exception e) {
            System.err.println("❌ [CONTROLLER] ERROR inesperado actualizando: " + e.getMessage());
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Error interno del servidor")
            );
        }
    }

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            System.out.println("📋 [CONTROLLER] Listando todos los anteproyectos");

            List<AnteproyectoResponse> anteproyectos = anteproyectoService.obtenerTodos();

            System.out.println("✅ [CONTROLLER] Anteproyectos encontrados: " + anteproyectos.size());
            return ResponseEntity.ok(anteproyectos);

        } catch (Exception e) {
            System.err.println("❌ [CONTROLLER] ERROR listando anteproyectos: " + e.getMessage());
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Error interno del servidor")
            );
        }
    }

    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<?> buscarPorProyecto(@PathVariable Long proyectoId) {
        try {
            System.out.println("🔍 [CONTROLLER] Buscando anteproyectos por proyecto: " + proyectoId);

            List<AnteproyectoResponse> anteproyectos = anteproyectoService.buscarPorProyecto(proyectoId);

            System.out.println("✅ [CONTROLLER] Anteproyectos encontrados para proyecto " + proyectoId + ": " + anteproyectos.size());
            return ResponseEntity.ok(anteproyectos);

        } catch (Exception e) {
            System.err.println("❌ [CONTROLLER] ERROR buscando anteproyectos por proyecto: " + e.getMessage());
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Error interno del servidor")
            );
        }
    }

    // ========== ENDPOINTS DE RELACIONES ==========

    @GetMapping("/{id}/mostrar-relacion")
    public ResponseEntity<String> mostrarRelacionCompleta(@PathVariable Long id) {
        try {
            System.out.println("🔗 [CONTROLLER] Mostrando relación completa para anteproyecto: " + id);
            anteproyectoService.mostrarRelacionCompleta(id);
            return ResponseEntity.ok("Relación mostrada en consola para anteproyecto: " + id);
        } catch (RuntimeException e) {
            System.err.println("❌ [CONTROLLER] ERROR mostrando relación: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error mostrando relación: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ [CONTROLLER] ERROR inesperado mostrando relación: " + e.getMessage());
            return ResponseEntity.internalServerError().body("Error interno mostrando relación");
        }
    }

    @GetMapping("/proyecto/{proyectoId}/mostrar-relacion")
    public ResponseEntity<String> mostrarRelacionPorProyecto(@PathVariable Long proyectoId) {
        try {
            System.out.println("🔗 [CONTROLLER] Mostrando relación por proyecto: " + proyectoId);
            anteproyectoService.mostrarRelacionPorProyecto(proyectoId);
            return ResponseEntity.ok("Relación mostrada en consola para proyecto: " + proyectoId);
        } catch (RuntimeException e) {
            System.err.println("❌ [CONTROLLER] ERROR mostrando relación por proyecto: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error mostrando relación: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ [CONTROLLER] ERROR inesperado mostrando relación: " + e.getMessage());
            return ResponseEntity.internalServerError().body("Error interno mostrando relación");
        }
    }

    @GetMapping("/mostrar-todas-relaciones")
    public ResponseEntity<String> mostrarTodasLasRelaciones() {
        try {
            System.out.println("🔗 [CONTROLLER] Mostrando todas las relaciones");
            anteproyectoService.listarTodasLasRelaciones();
            return ResponseEntity.ok("Todas las relaciones mostradas en consola");
        } catch (Exception e) {
            System.err.println("❌ [CONTROLLER] ERROR mostrando todas las relaciones: " + e.getMessage());
            return ResponseEntity.internalServerError().body("Error interno mostrando relaciones");
        }
    }

    // ========== ENDPOINTS PARA EL HISTORIAL MEMENTO ==========

    @GetMapping("/{id}/historial")
    public ResponseEntity<?> obtenerHistorial(@PathVariable Long id) {
        try {
            System.out.println("📊 [CONTROLLER] Obteniendo historial del anteproyecto: " + id);

            var historial = anteproyectoService.obtenerHistorialAnteproyecto(id);

            System.out.println("✅ [CONTROLLER] Historial obtenido - Versiones: " + historial.size());
            return ResponseEntity.ok(historial);

        } catch (Exception e) {
            System.err.println("❌ [CONTROLLER] ERROR obteniendo historial: " + e.getMessage());
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Error al obtener historial")
            );
        }
    }

    @PostMapping("/{id}/restaurar/{version}")
    public ResponseEntity<?> restaurarVersion(@PathVariable Long id, @PathVariable int version) {
        try {
            System.out.println("⏪ [CONTROLLER] Restaurando anteproyecto " + id + " a versión: " + version);

            var anteproyectoRestaurado = anteproyectoService.restaurarAnteproyectoAVersion(id, version);

            System.out.println("✅ [CONTROLLER] Anteproyecto restaurado - Nueva ID: " + anteproyectoRestaurado.getId());
            return ResponseEntity.ok(anteproyectoRestaurado);

        } catch (RuntimeException e) {
            System.err.println("❌ [CONTROLLER] ERROR restaurando versión: " + e.getMessage());
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        } catch (Exception e) {
            System.err.println("❌ [CONTROLLER] ERROR inesperado restaurando: " + e.getMessage());
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Error interno del servidor")
            );
        }
    }
}