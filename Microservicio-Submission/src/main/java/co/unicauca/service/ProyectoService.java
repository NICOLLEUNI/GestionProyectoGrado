package co.unicauca.service;

import co.unicauca.entity.FormatoA;
import co.unicauca.entity.FormatoAVersion;
import co.unicauca.entity.ProyectoGrado;
import co.unicauca.repository.ProyectoRepository;
import org.springframework.stereotype.Service;

@Service
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;
    public ProyectoService(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    public ProyectoGrado crearProyectoGrado(FormatoA formatoA, FormatoAVersion version1) {
        // Validar que no existe proyecto activo para estos estudiantes
        for (String emailEstudiante : formatoA.getEstudianteEmails()) {
            if (proyectoRepository.existsByEstudiantesEmailContainsAndEstadoNot(emailEstudiante, "CANCELADO")) {
                throw new RuntimeException("El estudiante " + emailEstudiante + " ya tiene un proyecto activo");
            }
        }

        ProyectoGrado proyectoGrado = new ProyectoGrado();
        proyectoGrado.setNombre(formatoA.getTitle());
        proyectoGrado.setFechaCreacion(java.time.LocalDateTime.now());
        proyectoGrado.setEstudiantesEmail(formatoA.getEstudianteEmails());
        proyectoGrado.setFormatoAActual(formatoA);
        proyectoGrado.setEstado("ACTIVO");
        proyectoGrado.setAnteproyecto(null);
        proyectoGrado.getHistorialFormatosA().add(version1);

        return proyectoRepository.save(proyectoGrado);
    }

}
