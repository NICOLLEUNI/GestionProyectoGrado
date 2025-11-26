package co.unicauca.controller;

import co.unicauca.entity.FormatoAVersion;
import co.unicauca.infra.dto.FormatoAVersionRequest;
import co.unicauca.infra.dto.FormatoAVersionResponse;
import co.unicauca.service.FormatoAVersionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/formatos-a")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(
        name = "Gestión de Versiones de Formato A",
        description = "Endpoints para crear, consultar, actualizar y administrar el historial de versiones del Formato A."
)
public class FormatoAController {

    private final FormatoAVersionService formatoAVersionService;

    // -------------------------------------------------------------------------
    @Operation(
            summary = "Crear una nueva versión del Formato A",
            description = "Crea una nueva versión basada en los datos enviados en el cuerpo de la solicitud."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Versión creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o error en la creación")
    })
    @PostMapping("/versiones")
    public ResponseEntity<?> crearVersion(@RequestBody FormatoAVersionRequest request) {
        try {
            FormatoAVersion versionCreada = formatoAVersionService.crearVersion(request);
            return ResponseEntity.ok(versionCreada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Error al crear versión", "detalle", e.getMessage()));
        }
    }

    // -------------------------------------------------------------------------
    @Operation(
            summary = "Obtener versión de Formato A por ID",
            description = "Devuelve una versión específica usando el ID enviado como parámetro."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Versión encontrada"),
            @ApiResponse(responseCode = "404", description = "La versión no existe")
    })
    @GetMapping("/versiones/{id}")
    public ResponseEntity<?> obtenerVersion(
            @Parameter(description = "ID de la versión") @PathVariable Long id) {

        try {
            FormatoAVersionResponse response = formatoAVersionService.buscarPorId(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // -------------------------------------------------------------------------
    @Operation(
            summary = "Actualizar una versión existente",
            description = "Modifica los datos de una versión utilizando su ID y los nuevos campos enviados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Versión actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Error al actualizar la versión")
    })
    @PutMapping("/versiones/{id}")
    public ResponseEntity<?> actualizarVersion(
            @Parameter(description = "ID de la versión a actualizar") @PathVariable Long id,
            @RequestBody FormatoAVersionRequest request) {

        try {
            formatoAVersionService.procesarVersionRecibida(request);
            return ResponseEntity.ok(Map.of("mensaje", "Versión actualizada exitosamente", "id", id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // -------------------------------------------------------------------------
    @Operation(
            summary = "Obtener historial de versiones",
            description = "Obtiene todas las versiones guardadas en el patrón Memento para un Formato A."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Historial encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno al obtener historial")
    })
    @GetMapping("/versiones/{id}/historial")
    public ResponseEntity<?> obtenerHistorial(@PathVariable Long id) {
        try {
            var historial = formatoAVersionService.obtenerHistorialVersiones(id);
            return ResponseEntity.ok(historial);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error al obtener historial"));
        }
    }

    // -------------------------------------------------------------------------
    @Operation(
            summary = "Obtener una versión específica del historial",
            description = "Permite obtener el estado de una versión previa guardada en el historial."
    )
    @GetMapping("/versiones/{id}/historial/{version}")
    public ResponseEntity<?> obtenerVersionHistorial(
            @PathVariable Long id,
            @Parameter(description = "Número de versión en el historial") @PathVariable int version) {

        try {
            var memento = formatoAVersionService.obtenerEstadoVersion(id, version);
            return ResponseEntity.ok(memento);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // -------------------------------------------------------------------------
    @Operation(
            summary = "Restaurar versión",
            description = "Restaura una versión anterior del historial usando el patrón Memento."
    )
    @PostMapping("/versiones/{id}/restaurar/{version}")
    public ResponseEntity<?> restaurarVersion(
            @PathVariable Long id,
            @PathVariable int version) {

        try {
            var versionRestaurada = formatoAVersionService.restaurarAVersion(id, version);
            return ResponseEntity.ok(versionRestaurada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // -------------------------------------------------------------------------
    @Operation(
            summary = "Obtener el último estado registrado",
            description = "Devuelve la última versión almacenada del Formato A."
    )
    @GetMapping("/versiones/{id}/ultimo-estado")
    public ResponseEntity<?> obtenerUltimoEstado(@PathVariable Long id) {
        try {
            var ultimoEstado = formatoAVersionService.obtenerUltimoEstado(id);

            if (ultimoEstado != null) return ResponseEntity.ok(ultimoEstado);
            return ResponseEntity.notFound().build();

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error interno del servidor"));
        }
    }

    // -------------------------------------------------------------------------
    @Operation(
            summary = "Obtener todas las versiones de un Formato A",
            description = "Busca todas las versiones asociadas a un Formato A específico."
    )
    @GetMapping("/versiones/formato/{formatoAId}")
    public ResponseEntity<?> getVersionesByFormatoA(@PathVariable Long formatoAId) {
        try {
            List<FormatoAVersion> versiones = formatoAVersionService.buscarPorFormatoA(formatoAId);

            List<FormatoAVersionResponse> responses = versiones.stream()
                    .map(this::convertirAResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error al obtener versiones"));
        }
    }

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
