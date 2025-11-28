package co.unicauca.application.services;

import co.unicauca.application.ports.output.AnteproyectoRepoOutPort;
import co.unicauca.application.ports.output.PersonaRepoOutPort;
import co.unicauca.domain.entities.Anteproyecto;
import co.unicauca.domain.entities.EnumRol;
import co.unicauca.domain.entities.Persona;
import co.unicauca.infrastructure.dto.request.AnteproyectoRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDate;

@Service
public class AnteproyectoService {

    private AnteproyectoRepoOutPort anteproyectoRepoOutPort;
    private PersonaRepoOutPort personaRepoOutPort;

    public Anteproyecto guardarAnteproyecto(AnteproyectoRequest request) {

        Anteproyecto anteproyecto = new Anteproyecto();

        anteproyecto.asignarTitulo(request.titulo());
        anteproyecto.asignarEstado(request.estado() != null ? request.estado() : "PENDIENTE");
        anteproyecto.asignarIdProyecto(request.idProyectoGrado());
        anteproyecto.asignarArchivoPDF("pendiente.pdf");
        anteproyecto.asignarFechaCreacion(request.fecha() != null ? request.fecha() : LocalDate.now());

        return anteproyectoRepoOutPort.save(anteproyecto);
    }

    public List<Anteproyecto> listarAnteproyectos() {
        return anteproyectoRepoOutPort.findAll();
    }

    public Anteproyecto buscarPorId(Long id) {
        return anteproyectoRepoOutPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Anteproyecto no encontrado con ID: " + id));
    }

    /**
     * Asigna los evaluadores a un anteproyecto ya existente.
     */
    public Anteproyecto asignarEvaluadores(Long idAnteproyecto, String evaluador1, String evaluador2) {

        // Validación: no pueden venir vacíos
        if (evaluador1 == null || evaluador1.isBlank() ||
                evaluador2 == null || evaluador2.isBlank()) {
            throw new RuntimeException("❌ Debe proporcionar ambos emails de evaluadores.");
        }

        // Validación: no pueden ser el mismo correo
        if (evaluador1.equalsIgnoreCase(evaluador2)) {
            throw new RuntimeException("❌ Los evaluadores no pueden ser el mismo correo.");
        }

        // Buscar anteproyecto
        Anteproyecto anteproyecto = anteproyectoRepoOutPort.findById(idAnteproyecto)
                .orElseThrow(() -> new RuntimeException("❌ Anteproyecto no encontrado con ID: " + idAnteproyecto));

        // 🔍 VALIDAR QUE LOS CORREOS EXISTAN EN PERSONA Y QUE SEAN DOCENTES
        Persona p1 = personaRepoOutPort.findByEmail(evaluador1).orElse(null);
        Persona p2 = personaRepoOutPort.findByEmail(evaluador2).orElse(null);

        if (p1 == null) {
            throw new RuntimeException("❌ El correo " + evaluador1 + " no está registrado como persona.");
        }
        if (p2 == null) {
            throw new RuntimeException("❌ El correo " + evaluador2 + " no está registrado como persona.");
        }

        // Validar que sí sean docentes
        if (!p1.tieneRol(EnumRol.DOCENTE)) {
            throw new RuntimeException("❌ El usuario " + evaluador1 + " no tiene rol DOCENTE.");
        }
        if (!p2.tieneRol(EnumRol.DOCENTE)) {
            throw new RuntimeException("❌ El usuario " + evaluador2 + " no tiene rol DOCENTE.");
        }

        // Si todo es válido, asignar
        anteproyecto.asignarEmailEvaluador(evaluador1,"Evaluador 1");
        anteproyecto.asignarEmailEvaluador(evaluador2,"Evaluador 2");
        anteproyecto.asignar();
        // Guardar cambios
        return anteproyectoRepoOutPort.save(anteproyecto);
    }

}
