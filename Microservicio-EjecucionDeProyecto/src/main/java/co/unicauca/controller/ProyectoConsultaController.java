package co.unicauca.controller;
import co.unicauca.dto.ProyectoGradoResponse;
import co.unicauca.entity.ProyectoGradoEntity;
import co.unicauca.mapper.ProyectoGradoMapper;
import co.unicauca.service.ConsultaProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/proyectos")
@RequiredArgsConstructor
public class ProyectoConsultaController {

    private final ConsultaProyectoService consultaProyectoService;

    /**
     * Devuelve un proyecto completo con sus asociados:
     * anteproyecto, formatos, versiones y personas.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProyectoGradoResponse> getProyectoCompleto(@PathVariable Long id) {
        ProyectoGradoEntity proyecto = consultaProyectoService.consultarProyectoCompleto(id);

        // Mapear a DTO para enviar al frontend
        ProyectoGradoResponse response = ProyectoGradoMapper.toResponse(proyecto);

        return ResponseEntity.ok(response);
    }
}