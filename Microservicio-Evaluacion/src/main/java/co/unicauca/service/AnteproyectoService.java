package co.unicauca.service;

import co.unicauca.entity.Anteproyecto;
import co.unicauca.entity.EnumRol;
import co.unicauca.entity.Persona;
import co.unicauca.infra.dto.AnteproyectoRequest;
import co.unicauca.repository.AnteproyectoRepository;
import co.unicauca.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AnteproyectoService {

    @Autowired
    private AnteproyectoRepository anteproyectoRepository;
    private final PersonaRepository personaRepository;

    public AnteproyectoService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    /**
     * Guarda un nuevo anteproyecto en la base de datos.
     * Si la fecha no viene en la solicitud, se asigna la actual.
     */
    public Anteproyecto guardarAnteproyecto(AnteproyectoRequest request) {
        Anteproyecto anteproyecto = new Anteproyecto();
        anteproyecto.setTitulo(request.titulo());
        anteproyecto.setEstado(request.estado() != null ? request.estado() : "PENDIENTE");
        anteproyecto.setIdProyectoGrado(request.idProyectoGrado());
        anteproyecto.setArchivoPDF("pendiente.pdf"); // 🔸 valor por defecto o temporal
        anteproyecto.setFechaCreacion(request.fecha() != null ? request.fecha() : LocalDate.now());

        return anteproyectoRepository.save(anteproyecto);
    }

    /**
     * Retorna todos los anteproyectos almacenados.
     */
    public List<Anteproyecto> listarAnteproyectos() {
        return anteproyectoRepository.findAll();
    }

    /**
     * Busca un anteproyecto por su ID.
     */
    public Anteproyecto buscarPorId(Long id) {
        return anteproyectoRepository.findById(id)
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
        Anteproyecto anteproyecto = anteproyectoRepository.findById(idAnteproyecto)
                .orElseThrow(() -> new RuntimeException("❌ Anteproyecto no encontrado con ID: " + idAnteproyecto));

        // 🔍 VALIDAR QUE LOS CORREOS EXISTAN EN PERSONA Y QUE SEAN DOCENTES
        Persona p1 = personaRepository.findByEmail(evaluador1).orElse(null);
        Persona p2 = personaRepository.findByEmail(evaluador2).orElse(null);

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
        anteproyecto.setEmailEvaluador1(evaluador1);
        anteproyecto.setEmailEvaluador2(evaluador2);
        anteproyecto.setEstado("ASIGNADO");
        // Guardar cambios
        return anteproyectoRepository.save(anteproyecto);
    }
}