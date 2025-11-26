package co.unicauca.controller;

import co.unicauca.entity.FormatoAVersion;
import co.unicauca.infra.dto.FormatoAVersionResponse;
import co.unicauca.infra.dto.ProyectoGradoRequest;
import co.unicauca.infra.dto.ProyectoGradoResponse;
import co.unicauca.service.ProyectoGradoService;

import co.unicauca.service.FormatoAVersionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/proyectos-grado")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(
        name = "Gestión de Proyectos de Grado",
        description = "Endpoints para crear, consultar, actualizar y administrar el historial de proyectos de grado."
)
public class ProyectoGradoController {

    private final ProyectoGradoService proyectoGradoService;
    private final FormatoAVersionService formatoAVersionService;

    // --------------------------------------------------------------------
    @Operation(
            summary = "Crear un nuevo proyecto de grado",
            description = "Registra un nuevo proyecto de grado a partir de la información enviada en el cuerpo de la solicitud."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Proyecto creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o error creando el proyecto")
    })
    @PostMapping
    public ResponseEntity<?> crearProyecto(@RequestBody ProyectoGradoRequest request) {
        try {
            ProyectoGradoResponse response = proyectoGradoService.crearProyecto(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Error al crear proyecto", "detalle", e.getMessage())
            );
        }
    }

    // --------------------------------------------------------------------
    @Operation(
            summary = "Obtener proyecto de grado por email del estudiante",
            description = "Retorna el proyecto asociado a un estudiante según su correo institucional."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Proyecto encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe un proyecto asociado al estudiante"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @GetMapping("/estudiante/{email}")
    public ResponseEntity<?> obtenerProyectoPorEstudiante(
            @Parameter(description = "Correo electrónico del estudiante") @PathVariable String email) {

        try {
            ProyectoGradoResponse proyecto = proyectoGradoService.buscarPorEmailEstudiante(email);
            return ResponseEntity.ok(proyecto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --------------------------------------------------------------------
    @Operation(
            summary = "Obtener proyecto por ID",
            description = "Permite obtener la información completa de un proyecto de grado utilizando su ID único."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Proyecto encontrado"),
            @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(
            @Parameter(description = "ID del proyecto a consultar") @PathVariable Long id) {

        try {
            ProyectoGradoResponse proyecto = proyectoGradoService.buscarPorId(id);
            return ResponseEntity.ok(proyecto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --------------------------------------------------------------------
    @Operation(
            summary = "Actualizar un proyecto de grado",
            description = "Actualiza los datos del proyecto con base en la información enviada y el ID especificado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Proyecto actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "El proyecto no existe"),
            @ApiResponse(responseCode = "500", description = "Error interno al actualizar")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProyecto(
            @Parameter(description = "ID del proyecto a actualizar") @PathVariable Long id,
            @RequestBody ProyectoGradoRequest request) {

        try {
            ProyectoGradoResponse response = proyectoGradoService.actualizarProyecto(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --------------------------------------------------------------------
    @Operation(
            summary = "Obtener historial del proyecto",
            description = "Devuelve la lista de estados del proyecto almacenados mediante el patrón Memento."
    )
    @GetMapping("/{id}/historial")
    public ResponseEntity<?> obtenerHistorial(
            @Parameter(description = "ID del proyecto") @PathVariable Long id) {

        try {
            var historial = proyectoGradoService.obtenerHistorialProyecto(id);
            return ResponseEntity.ok(historial);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Error al obtener historial")
            );
        }
    }

    // --------------------------------------------------------------------
    @Operation(
            summary = "Obtener una versión específica dentro del historial",
            description = "Consulta una versión puntual previamente almacenada del proyecto."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Versión encontrada en el historial"),
            @ApiResponse(responseCode = "404", description = "La versión no existe dentro del historial")
    })
    @GetMapping("/{id}/historial/{version}")
    public ResponseEntity<?> obtenerVersionHistorial(
            @Parameter(description = "ID del proyecto") @PathVariable Long id,
            @Parameter(description = "Número de versión dentro del historial") @PathVariable int version) {

        try {
            var memento = proyectoGradoService.obtenerEstadoProyectoVersion(id, version);
            return ResponseEntity.ok(memento);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --------------------------------------------------------------------
    @Operation(
            summary = "Obtener el Formato A asociado a un proyecto de grado",
            description = "Retorna la última versión del Formato A vinculada al proyecto especificado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Formato A encontrado"),
            @ApiResponse(responseCode = "404", description = "El proyecto no tiene Formato A asociado")
    })
    @GetMapping("/{proyectoId}/formato-a")
    public ResponseEntity<?> getFormatoAByProyecto(
            @Parameter(description = "ID del proyecto") @PathVariable Long proyectoId) {

        try {
            FormatoAVersion version = formatoAVersionService.findByProyectoId(proyectoId);
            FormatoAVersionResponse response = convertirFormatoAResponse(version);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // --------------------------------------------------------------------
    private FormatoAVersionResponse convertirFormatoAResponse(FormatoAVersion version) {
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
