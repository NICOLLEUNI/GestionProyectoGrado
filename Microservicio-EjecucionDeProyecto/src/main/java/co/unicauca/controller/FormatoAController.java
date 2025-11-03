package co.unicauca.controller;

import co.unicauca.entity.FormatoAVersion;
import co.unicauca.infra.dto.FormatoAVersionRequest;
import co.unicauca.infra.dto.FormatoAVersionResponse;
import co.unicauca.service.FormatoAVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import co.unicauca.infra.dto.FormatoAVersionResponse;

import java.util.Map;

@RestController
@RequestMapping("/api/formatos-a")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class FormatoAController {

    private final FormatoAVersionService formatoAVersionService;

    /**
     * ✅ CREAR NUEVA VERSIÓN DE FORMATO A
     */
    @PostMapping("/versiones")
    public ResponseEntity<?> crearVersion(@RequestBody FormatoAVersionRequest request) {
        try {
            System.out.println("📨 [CONTROLLER] Creando versión de Formato A:");
            System.out.println("   Título: " + request.titulo());
            System.out.println("   FormatoA ID: " + request.idFormatoA());
            System.out.println("   Versión: " + request.numVersion());
            System.out.println("   Estado: " + request.estado());
            System.out.println("   Modalidad: " + request.modalidad());

            // ✅ CORREGIDO: El service retorna FormatoAVersion, no FormatoAVersionRequest
            FormatoAVersion versionCreada = formatoAVersionService.crearVersion(request);

            System.out.println("✅ [CONTROLLER] Versión creada exitosamente - ID: " + versionCreada.getId());
            return ResponseEntity.ok(versionCreada);

        } catch (Exception e) {
            System.err.println("❌ [CONTROLLER] ERROR creando versión: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "error", "Error al crear versión",
                            "detalle", e.getMessage()
                    )
            );
        }
    }

    /**
     * ✅ OBTENER VERSIÓN POR ID
     */
    @GetMapping("/versiones/{id}")
    public ResponseEntity<?> obtenerVersion(@PathVariable Long id) {
        try {
            System.out.println("🔍 [CONTROLLER] Buscando versión por ID: " + id);

            // ✅ CORREGIDO: El service retorna FormatoAVersionResponse
            FormatoAVersionResponse response = formatoAVersionService.buscarPorId(id);

            System.out.println("✅ [CONTROLLER] Versión encontrada - ID: " + response.id());
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            System.err.println("❌ [CONTROLLER] Versión no encontrada - ID: " + id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("❌ [CONTROLLER] ERROR buscando versión: " + e.getMessage());
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Error interno del servidor")
            );
        }
    }

    /**
     * ✅ ACTUALIZAR VERSIÓN EXISTENTE
     */
    @PutMapping("/versiones/{id}")
    public ResponseEntity<?> actualizarVersion(@PathVariable Long id, @RequestBody FormatoAVersionRequest request) {
        try {
            System.out.println("✏️ [CONTROLLER] Actualizando versión - ID: " + id);
            System.out.println("   Nuevo título: " + request.titulo());
            System.out.println("   Nuevo estado: " + request.estado());
            System.out.println("   Nuevo counter: " + request.counter());

            // ✅ Usar procesarVersionRecibida para actualizar
            formatoAVersionService.procesarVersionRecibida(request);

            System.out.println("✅ [CONTROLLER] Versión actualizada - ID: " + id);
            return ResponseEntity.ok().body(
                    Map.of("mensaje", "Versión actualizada exitosamente", "id", id)
            );

        } catch (RuntimeException e) {
            System.err.println("❌ [CONTROLLER] ERROR actualizando versión: " + e.getMessage());
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "error", "Error al actualizar versión",
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

    /**
     * ✅ ENDPOINTS PARA HISTORIAL MEMENTO
     */
    @GetMapping("/versiones/{id}/historial")
    public ResponseEntity<?> obtenerHistorial(@PathVariable Long id) {
        try {
            System.out.println("📊 [CONTROLLER] Obteniendo historial de versión: " + id);

            var historial = formatoAVersionService.obtenerHistorialVersiones(id);

            System.out.println("✅ [CONTROLLER] Historial obtenido - Versiones: " + historial.size());
            return ResponseEntity.ok(historial);

        } catch (Exception e) {
            System.err.println("❌ [CONTROLLER] ERROR obteniendo historial: " + e.getMessage());
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Error al obtener historial")
            );
        }
    }

    @GetMapping("/versiones/{id}/historial/{version}")
    public ResponseEntity<?> obtenerVersionHistorial(@PathVariable Long id, @PathVariable int version) {
        try {
            System.out.println("🔍 [CONTROLLER] Obteniendo versión " + version + " del historial: " + id);

            var memento = formatoAVersionService.obtenerEstadoVersion(id, version);

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

    @PostMapping("/versiones/{id}/restaurar/{version}")
    public ResponseEntity<?> restaurarVersion(@PathVariable Long id, @PathVariable int version) {
        try {
            System.out.println("⏪ [CONTROLLER] Restaurando versión " + id + " a versión: " + version);

            var versionRestaurada = formatoAVersionService.restaurarAVersion(id, version);

            System.out.println("✅ [CONTROLLER] Versión restaurada - Nueva ID: " + versionRestaurada.getId());
            return ResponseEntity.ok(versionRestaurada);

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

    /**
     * ✅ OBTENER ÚLTIMO ESTADO
     */
    @GetMapping("/versiones/{id}/ultimo-estado")
    public ResponseEntity<?> obtenerUltimoEstado(@PathVariable Long id) {
        try {
            System.out.println("📈 [CONTROLLER] Obteniendo último estado de versión: " + id);

            var ultimoEstado = formatoAVersionService.obtenerUltimoEstado(id);

            if (ultimoEstado != null) {
                System.out.println("✅ [CONTROLLER] Último estado obtenido - Versión: " + ultimoEstado.getVersion());
                return ResponseEntity.ok(ultimoEstado);
            } else {
                System.out.println("ℹ️ [CONTROLLER] No hay historial para versión: " + id);
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            System.err.println("❌ [CONTROLLER] ERROR obteniendo último estado: " + e.getMessage());
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Error interno del servidor")
            );
        }
    }

    /**
     * ✅ OBTENER TODAS LAS VERSIONES DE UN FORMATO A
     */
    @GetMapping("/versiones/formato/{formatoAId}")
    public ResponseEntity<?> getVersionesByFormatoA(@PathVariable Long formatoAId) {
        try {
            System.out.println("🔍 [CONTROLLER] Buscando versiones por FormatoA ID: " + formatoAId);

            List<FormatoAVersion> versiones = formatoAVersionService.buscarPorFormatoA(formatoAId);

            if (versiones != null && !versiones.isEmpty()) {
                System.out.println("✅ [CONTROLLER] Versiones encontradas: " + versiones.size());

                // Convertir a Response
                List<FormatoAVersionResponse> responses = versiones.stream()
                        .map(this::convertirAResponse)
                        .collect(Collectors.toList());

                return ResponseEntity.ok(responses);
            } else {
                System.out.println("⚠️ [CONTROLLER] No hay versiones para FormatoA: " + formatoAId);
                return ResponseEntity.ok(List.of()); // Devolver array vacío
            }
        } catch (Exception e) {
            System.err.println("❌ [CONTROLLER] ERROR buscando versiones: " + e.getMessage());
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Error al obtener versiones del formato A")
            );
        }
    }

    // ✅ AGREGAR ESTE MÉTODO DE CONVERSIÓN
    private FormatoAVersionResponse convertirAResponse(FormatoAVersion version) {
        return new FormatoAVersionResponse(
                version.getId(),
                version.getNumeroVersion(),
                version.getFecha(),
                version.getTitle(),
                version.getMode().name(),
                version.getState().name(),
                version.getObservations(),
                version.getCounter(),
                version.getIdFormatoA()
        );
    }



}