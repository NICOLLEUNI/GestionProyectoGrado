package co.unicauca.controller;

import co.unicauca.infra.dto.ProyectoGradoRequest;
import co.unicauca.infra.dto.ProyectoGradoResponse;
import co.unicauca.service.ProyectoGradoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/proyectos-grado")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProyectoGradoController {

    private final ProyectoGradoService proyectoGradoService;

    /**
     * ✅ CREAR NUEVO PROYECTO DE GRADO
     */
    @PostMapping
    public ResponseEntity<?> crearProyecto(@RequestBody ProyectoGradoRequest request) {
        try {
            System.out.println("📨 [CONTROLLER] Creando proyecto de grado:");
            System.out.println("   Nombre: " + request.nombre());
            System.out.println("   FormatoA ID: " + request.IdFormatoA());
            System.out.println("   Estudiantes: " + (request.estudiantesEmail() != null ? request.estudiantesEmail().size() : 0));

            ProyectoGradoResponse response = proyectoGradoService.crearProyecto(request);

            System.out.println("✅ [CONTROLLER] Proyecto creado exitosamente - ID: " + response.id());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ [CONTROLLER] ERROR creando proyecto: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "error", "Error al crear proyecto",
                            "detalle", e.getMessage()
                    )
            );
        }
    }

    @GetMapping("/estudiante/{email}")
    public ResponseEntity<?> obtenerProyectoPorEstudiante(@PathVariable String email) {
        try {
            System.out.println("🔍 [CONTROLLER] Buscando proyecto de grado del estudiante: " + email);

            ProyectoGradoResponse proyecto = proyectoGradoService.buscarPorEmailEstudiante(email);
            return ResponseEntity.ok(proyecto);

        } catch (RuntimeException e) {
            System.err.println("❌ [CONTROLLER] No se encontró proyecto para el estudiante: " + email);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("❌ [CONTROLLER] ERROR buscando proyecto del estudiante: " + e.getMessage());
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Error interno al buscar proyecto del estudiante")
            );
        }
    }




    /**
     * ✅ OBTENER PROYECTO POR ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            System.out.println("🔍 [CONTROLLER] Buscando proyecto por ID: " + id);

            ProyectoGradoResponse proyecto = proyectoGradoService.buscarPorId(id);

            System.out.println("✅ [CONTROLLER] Proyecto encontrado - ID: " + proyecto.id());
            return ResponseEntity.ok(proyecto);

        } catch (RuntimeException e) {
            System.err.println("❌ [CONTROLLER] Proyecto no encontrado - ID: " + id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("❌ [CONTROLLER] ERROR buscando proyecto: " + e.getMessage());
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Error interno del servidor")
            );
        }
    }

    /**
     * ✅ ACTUALIZAR PROYECTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProyecto(@PathVariable Long id, @RequestBody ProyectoGradoRequest request) {
        try {
            System.out.println("✏️ [CONTROLLER] Actualizando proyecto - ID: " + id);
            System.out.println("   Nuevo nombre: " + request.nombre());
            System.out.println("   Nuevo FormatoA: " + request.IdFormatoA());

            ProyectoGradoResponse response = proyectoGradoService.actualizarProyecto(id, request);

            System.out.println("✅ [CONTROLLER] Proyecto actualizado - ID: " + response.id());
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            System.err.println("❌ [CONTROLLER] ERROR actualizando proyecto: " + e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("❌ [CONTROLLER] ERROR inesperado actualizando: " + e.getMessage());
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Error interno del servidor")
            );
        }
    }

    /**
     * ✅ ENDPOINTS PARA HISTORIAL MEMENTO
     */

    @GetMapping("/{id}/historial")
    public ResponseEntity<?> obtenerHistorial(@PathVariable Long id) {
        try {
            System.out.println("📊 [CONTROLLER] Obteniendo historial del proyecto: " + id);

            var historial = proyectoGradoService.obtenerHistorialProyecto(id);

            System.out.println("✅ [CONTROLLER] Historial obtenido - Versiones: " + historial.size());
            return ResponseEntity.ok(historial);

        } catch (Exception e) {
            System.err.println("❌ [CONTROLLER] ERROR obteniendo historial: " + e.getMessage());
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Error al obtener historial")
            );
        }
    }

    @GetMapping("/{id}/historial/{version}")
    public ResponseEntity<?> obtenerVersionHistorial(@PathVariable Long id, @PathVariable int version) {
        try {
            System.out.println("🔍 [CONTROLLER] Obteniendo versión " + version + " del historial: " + id);

            var memento = proyectoGradoService.obtenerEstadoProyectoVersion(id, version);

            System.out.println("✅ [CONTROLLER] Versión del historial obtenida - Estado: " + memento.getEstado());
            return ResponseEntity.ok(memento);

        } catch (RuntimeException e) {
            System.err.println("❌ [CONTROLLER] Versión no encontrada en historial: " + e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("❌ [CONTROLLER] ERROR obteniendo versión del historial: " + e.getMessage());
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Error interno del servidor")
            );
        }
    }
}