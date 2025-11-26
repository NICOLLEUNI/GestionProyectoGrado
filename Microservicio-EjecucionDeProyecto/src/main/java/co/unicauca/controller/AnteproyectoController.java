package co.unicauca.controller;

import co.unicauca.infra.dto.AnteproyectoRequest;
import co.unicauca.infra.dto.AnteproyectoResponse;
import co.unicauca.service.AnteproyectoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/anteproyectos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Anteproyectos", description = "Operaciones relacionadas con la gestión de anteproyectos")
public class AnteproyectoController {

    private final AnteproyectoService anteproyectoService;

    // ===========================================================
    //  POST - CREAR
    // ===========================================================
    @Operation(
            summary = "Crear un anteproyecto",
            description = "Crea un anteproyecto nuevo a partir de los datos enviados."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Anteproyecto creado exitosamente",
                    content = @Content(schema = @Schema(implementation = AnteproyectoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error en los datos enviados"
            )
    })
    @PostMapping
    public ResponseEntity<?> crearAnteproyecto(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para crear el anteproyecto",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AnteproyectoRequest.class))
            )
            @RequestBody AnteproyectoRequest request
    ) {
        try {
            AnteproyectoResponse response = anteproyectoService.crearAnteproyecto(request);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "No se puede crear el anteproyecto", "detalle", e.getMessage())
            );

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Error interno del servidor")
            );
        }
    }

    // ===========================================================
    //  GET - BUSCAR POR ID
    // ===========================================================
    @Operation(
            summary = "Obtener anteproyecto por ID",
            description = "Retorna un anteproyecto específico mediante su identificador."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Anteproyecto encontrado",
                    content = @Content(schema = @Schema(implementation = AnteproyectoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Anteproyecto no encontrado"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            AnteproyectoResponse response = anteproyectoService.buscarPorId(id);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Error interno del servidor")
            );
        }
    }

    // ===========================================================
    //  PUT - ACTUALIZAR
    // ===========================================================
    @Operation(
            summary = "Actualizar un anteproyecto",
            description = "Modifica los datos de un anteproyecto existente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Anteproyecto actualizado",
                    content = @Content(schema = @Schema(implementation = AnteproyectoResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error en la actualización"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Anteproyecto no encontrado"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarAnteproyecto(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nuevos datos del anteproyecto",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AnteproyectoRequest.class))
            )
            @RequestBody AnteproyectoRequest request
    ) {
        try {
            AnteproyectoResponse response = anteproyectoService.actualizarAnteproyecto(id, request);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Error al actualizar anteproyecto", "detalle", e.getMessage())
            );

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Error interno del servidor")
            );
        }
    }

    // ===========================================================
    //  GET - LISTAR TODOS
    // ===========================================================
    @Operation(
            summary = "Listar todos los anteproyectos",
            description = "Retorna una lista con todos los anteproyectos registrados."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista obtenida correctamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AnteproyectoResponse.class)))
            )
    })
    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            List<AnteproyectoResponse> anteproyectos = anteproyectoService.obtenerTodos();
            return ResponseEntity.ok(anteproyectos);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Error interno del servidor")
            );
        }
    }

    // ===========================================================
    //  GET - BUSCAR POR PROYECTO
    // ===========================================================
    @Operation(
            summary = "Buscar anteproyectos por proyecto",
            description = "Obtiene todos los anteproyectos asociados a un proyecto específico."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado obtenido exitosamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AnteproyectoResponse.class)))
            )
    })
    @GetMapping("/proyecto/{proyectoId}")
    public ResponseEntity<?> buscarPorProyecto(@PathVariable Long proyectoId) {
        try {
            List<AnteproyectoResponse> anteproyectos = anteproyectoService.buscarPorProyecto(proyectoId);
            return ResponseEntity.ok(anteproyectos);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Error interno del servidor")
            );
        }
    }

    // ===========================================================
    //  GET - RELACIONES
    // ===========================================================
    @Operation(
            summary = "Mostrar relaciones completas del anteproyecto",
            description = "Imprime en consola todas las relaciones del anteproyecto."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Relación mostrada correctamente"),
            @ApiResponse(responseCode = "400", description = "No fue posible mostrar la relación")
    })
    @GetMapping("/{id}/mostrar-relacion")
    public ResponseEntity<String> mostrarRelacionCompleta(@PathVariable Long id) {
        try {
            anteproyectoService.mostrarRelacionCompleta(id);
            return ResponseEntity.ok("Relación mostrada en consola para anteproyecto: " + id);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error mostrando relación: " + e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error interno mostrando relación");
        }
    }

    @Operation(
            summary = "Mostrar relación por proyecto",
            description = "Imprime en consola todas las relaciones asociadas a un proyecto."
    )
    @GetMapping("/proyecto/{proyectoId}/mostrar-relacion")
    public ResponseEntity<String> mostrarRelacionPorProyecto(@PathVariable Long proyectoId) {
        try {
            anteproyectoService.mostrarRelacionPorProyecto(proyectoId);
            return ResponseEntity.ok("Relación mostrada en consola para proyecto: " + proyectoId);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error mostrando relación: " + e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error interno mostrando relación");
        }
    }

    @Operation(
            summary = "Mostrar todas las relaciones",
            description = "Imprime en consola todas las relaciones de todos los anteproyectos."
    )
    @GetMapping("/mostrar-todas-relaciones")
    public ResponseEntity<String> mostrarTodasLasRelaciones() {
        try {
            anteproyectoService.listarTodasLasRelaciones();
            return ResponseEntity.ok("Todas las relaciones mostradas en consola");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error interno mostrando relaciones");
        }
    }

    // ===========================================================
    //  HISTORIAL - MEMENTO
    // ===========================================================
    @Operation(
            summary = "Obtener historial del anteproyecto",
            description = "Retorna todas las versiones anteriores del anteproyecto."
    )
    @GetMapping("/{id}/historial")
    public ResponseEntity<?> obtenerHistorial(@PathVariable Long id) {
        try {
            var historial = anteproyectoService.obtenerHistorialAnteproyecto(id);
            return ResponseEntity.ok(historial);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Error al obtener historial")
            );
        }
    }

    @Operation(
            summary = "Restaurar versión del anteproyecto",
            description = "Restaura el anteproyecto a una versión previa almacenada en el historial."
    )
    @PostMapping("/{id}/restaurar/{version}")
    public ResponseEntity<?> restaurarVersion(@PathVariable Long id, @PathVariable int version) {
        try {
            var anteproyectoRestaurado = anteproyectoService.restaurarAnteproyectoAVersion(id, version);
            return ResponseEntity.ok(anteproyectoRestaurado);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Error interno del servidor")
            );
        }
    }
}
