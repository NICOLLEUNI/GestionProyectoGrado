package co.unicauca.controller;

import co.unicauca.infra.dto.ProyectoGradoRequest;
import co.unicauca.infra.dto.ProyectoGradoResponse;
import co.unicauca.service.ProyectoGradoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/proyectos-grado")
@CrossOrigin(origins = "*")
public class ProyectoGradoController {

    @Autowired
    private ProyectoGradoService proyectoGradoService;

    @PostMapping("/crear")
    public ResponseEntity<?> crearProyecto(@RequestBody ProyectoGradoRequest request) {
        try {
            System.out.println("📨 Recibiendo petición para crear proyecto:");
            System.out.println("   Nombre: " + request.nombre());
            System.out.println("   FormatoA ID: " + request.IdFormatoA());

            ProyectoGradoResponse response = proyectoGradoService.crearProyecto(request);

            System.out.println("✅ Proyecto creado exitosamente: " + response.id());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ ERROR creando proyecto: " + e.getMessage());
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Error al crear proyecto: " + e.getMessage())
            );
        }
    }

    @GetMapping
    public ResponseEntity<List<ProyectoGradoResponse>> listarTodos() {
        try {
            List<ProyectoGradoResponse> proyectos = proyectoGradoService.listarTodos();
            return ResponseEntity.ok(proyectos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            ProyectoGradoResponse proyecto = proyectoGradoService.buscarPorId(id);
            return ResponseEntity.ok(proyecto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Error al buscar proyecto: " + e.getMessage())
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProyecto(@PathVariable Long id, @RequestBody ProyectoGradoRequest request) {
        try {
            ProyectoGradoResponse response = proyectoGradoService.actualizarProyecto(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Error al actualizar proyecto: " + e.getMessage())
            );
        }
    }

    @PostMapping("/{id}/sincronizar-formato")
    public ResponseEntity<?> sincronizarFormatoA(@PathVariable Long id, @RequestBody Map<String, Long> request) {
        try {
            Long idFormatoA = request.get("idFormatoA");
            if (idFormatoA == null) {
                return ResponseEntity.badRequest().body(
                        Map.of("error", "El campo 'idFormatoA' es requerido")
                );
            }

            proyectoGradoService.sincronizarFormatoA(id, idFormatoA);
            return ResponseEntity.ok().body(
                    Map.of("message", "FormatoA sincronizado exitosamente")
            );
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Error al sincronizar FormatoA: " + e.getMessage())
            );
        }
    }

    @GetMapping("/{id}/versiones")
    public ResponseEntity<?> obtenerVersiones(@PathVariable Long id) {
        try {
            var versiones = proyectoGradoService.buscarVersionesPorProyecto(id);
            return ResponseEntity.ok(versiones);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Error al obtener versiones: " + e.getMessage())
            );
        }
    }
}