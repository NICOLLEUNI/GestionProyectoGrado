package co.unicauca.service;

import co.unicauca.entity.FormatoA;
import co.unicauca.entity.FormatoAVersion;
import co.unicauca.entity.ProyectoGrado;
import co.unicauca.infra.dto.ProyectoGradoResponse;
import co.unicauca.infra.messaging.RabbitMQPublisher;
import co.unicauca.repository.ProyectoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final RabbitMQPublisher rabbitMQPublisher;
    public ProyectoService(ProyectoRepository proyectoRepository, RabbitMQPublisher rabbitMQPublisher) {
        this.proyectoRepository = proyectoRepository;
        this.rabbitMQPublisher = rabbitMQPublisher;
    }

    public ProyectoGrado crearProyectoGrado(FormatoA formatoA, FormatoAVersion version1) {
        // Validar que no existe proyecto activo para estos estudiantes
        for (String emailEstudiante : formatoA.getEstudianteEmails()) {
            if (proyectoRepository.existsByEstudianteEmailAndEstadoNot(emailEstudiante, "CANCELADO")) {
                throw new RuntimeException("El estudiante " + emailEstudiante + " ya tiene un proyecto activo");
            }
        }

        ProyectoGrado proyectoGrado = new ProyectoGrado();
        proyectoGrado.setNombre(formatoA.getTitle());
        proyectoGrado.setFechaCreacion(java.time.LocalDate.now());

        // ⭐⭐ CREAR NUEVA LISTA - NO COMPARTIR LA REFERENCIA ⭐⭐
        proyectoGrado.setEstudiantesEmail(new ArrayList<>(formatoA.getEstudianteEmails()));

        proyectoGrado.setFormatoAActual(formatoA);
        proyectoGrado.setEstado("ACTIVO");
        proyectoGrado.setAnteproyecto(null);
        proyectoGrado.getHistorialFormatosA().add(version1);

        ProyectoGrado proyectoGuardado = proyectoRepository.save(proyectoGrado);

        // Publicar evento
        ProyectoGradoResponse response = convertirAProyectoGradoResponse(proyectoGuardado);
        rabbitMQPublisher.publicarProyectoGradoCreado(response);

        return proyectoGuardado;
    }



    /**
     * Convierte ProyectoGrado entity a ProyectoGradoResponse DTO
     */
    private ProyectoGradoResponse convertirAProyectoGradoResponse(ProyectoGrado proyectoGrado) {
        return new ProyectoGradoResponse(
                proyectoGrado.getId(),
                proyectoGrado.getNombre(),
                proyectoGrado.getFechaCreacion(), // ← Convertir LocalDateTime a LocalDate
                proyectoGrado.getEstudiantesEmail(),
                proyectoGrado.getFormatoAActual().getId()
        );
    }


    public ProyectoGrado agregarVersionAProyectoGrado(FormatoA formatoA, FormatoAVersion nuevaVersion) {
        // Buscar el proyecto que tiene este FormatoA como formatoAActual
        Optional<ProyectoGrado> proyectoOpt = proyectoRepository.findByFormatoAActualId(formatoA.getId());

        if (proyectoOpt.isPresent()) {
            ProyectoGrado proyecto = proyectoOpt.get();
            proyecto.getHistorialFormatosA().add(nuevaVersion);
            return proyectoRepository.save(proyecto);
        } else {
            throw new RuntimeException("No se encontró proyecto asociado al FormatoA: " + formatoA.getId());
        }
    }
    @Transactional
    public void eliminarProyectoPorFormatoA(Long formatoAId) {
        ProyectoGrado proyecto = proyectoRepository.findByFormatoAActualId(formatoAId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado para FormatoA id: " + formatoAId));
        proyectoRepository.delete(proyecto);
    }
}


