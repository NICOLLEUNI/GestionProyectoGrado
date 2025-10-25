package co.unicauca.service;

import co.unicauca.entity.FormatoAEntity;
import co.unicauca.entity.FormatoAVersionEntity;
import co.unicauca.entity.ProyectoGradoEntity;
import co.unicauca.entity.PersonaEntity;
import co.unicauca.repository.ProyectoGradoRepository;
import co.unicauca.repository.FormatoARepository;
import co.unicauca.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EjecucionProyectoGradoService {

    @Autowired
    private ProyectoGradoRepository proyectoGradoRepository;

    @Autowired
    private FormatoARepository formatoARepository;

    @Autowired
    private PersonaRepository personaRepository;

    /**
     * Método para crear un ProyectoGrado a partir de un FormatoA y su versión
     * @param formatoAId ID de FormatoA
     * @param versionId ID de la versión de FormatoA
     * @return El ProyectoGrado creado
     */
    public ProyectoGradoEntity crearProyectoGrado(Long formatoAId, Long versionId) {
        // Obtener FormatoA y su versión desde la base de datos usando los IDs proporcionados
        FormatoAEntity formatoA = formatoARepository.findById(formatoAId)
                .orElseThrow(() -> new RuntimeException("Formato A no encontrado"));

        FormatoAVersionEntity version = formatoA.getVersiones().stream()
                .filter(v -> v.getId().equals(versionId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Versión de Formato A no encontrada"));

        // Validar que no exista un ProyectoGrado activo para estos estudiantes
        for (String emailEstudiante : formatoA.getEstudiantesEmails()) {
            if (proyectoGradoRepository.existsByEstudiantesEmailContainsAndEstadoNot(emailEstudiante, "CANCELADO")) {
                throw new RuntimeException("El estudiante " + emailEstudiante + " ya tiene un proyecto activo");
            }
        }

        // Obtener la lista de estudiantes de la base de datos usando los correos electrónicos
        List<PersonaEntity> estudiantes = personaRepository.findAllByEmailIn(formatoA.getEstudiantesEmails());

        // Crear un nuevo ProyectoGrado
        ProyectoGradoEntity proyectoGrado = new ProyectoGradoEntity();
        proyectoGrado.setTitulo(formatoA.getTitle());
        proyectoGrado.setFechaCreacion(java.time.LocalDateTime.now());
        proyectoGrado.setEstudiantesEmail(formatoA.getEstudiantesEmails()); // Establecer los correos electrónicos
        proyectoGrado.setEstudiantes(estudiantes); // Asociar los estudiantes a través de la relación ManyToMany
        proyectoGrado.setFormatoAActual(formatoA);
        proyectoGrado.setEstado("ACTIVO");
        proyectoGrado.setAnteproyecto(null);  // El anteproyecto aún no está asociado en este punto
        proyectoGrado.getHistorialFormatosA().add(version);  // Agregar la versión del formato al historial

        // Guardar el ProyectoGrado en la base de datos
        return proyectoGradoRepository.save(proyectoGrado);
    }
}
