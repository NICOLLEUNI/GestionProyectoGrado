package co.unicauca.service;

import co.unicauca.entity.*;
import co.unicauca.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProyectoGradoService {

    private final ProyectoGradoRepository proyectoRepository;
    private final FormatoARepository formatoARepository;
    private final FormatoAVersionRepository versionRepository;
    private final AnteproyectoRepository anteproyectoRepository;
    private final PersonaRepository personaRepository;

    public ProyectoGradoService(
            ProyectoGradoRepository proyectoRepository,
            FormatoARepository formatoARepository,
            FormatoAVersionRepository versionRepository,
            AnteproyectoRepository anteproyectoRepository,
            PersonaRepository personaRepository) {
        this.proyectoRepository = proyectoRepository;
        this.formatoARepository = formatoARepository;
        this.versionRepository = versionRepository;
        this.anteproyectoRepository = anteproyectoRepository;
        this.personaRepository = personaRepository;
    }

    /**
     * Reconstruye y persiste un ProyectoGrado completo a partir de las piezas.
     */
    @Transactional
    public ProyectoGradoEntity reconstruirProyecto(
            Long formatoAId,
            Long versionId,
            Long anteproyectoId,
            List<Long> estudiantesIds
    ) {
        // 🔹 Buscar entidades por ID
        FormatoAEntity formatoA = formatoARepository.findById(Long.valueOf(String.valueOf(formatoAId)))
                .orElseThrow(() -> new RuntimeException("FormatoA no encontrado: " + formatoAId));

        FormatoAVersionEntity version = versionRepository.findById(versionId)
                .orElseThrow(() -> new RuntimeException("Version FormatoA no encontrada: " + versionId));

        AnteproyectoEntity anteproyecto = anteproyectoRepository.findById(anteproyectoId)
                .orElseThrow(() -> new RuntimeException("Anteproyecto no encontrado: " + anteproyectoId));

        List<PersonaEntity> estudiantes = personaRepository.findAllById(estudiantesIds);

        // 🔹 Asociar la versión al FormatoA si no está ya
        if (!formatoA.getVersiones().contains(version)) {
            formatoA.getVersiones().add(version);
            version.setFormatoA(formatoA);
        }

        // 🔹 Crear ProyectoGradoEntity
        ProyectoGradoEntity proyecto = new ProyectoGradoEntity();
        proyecto.setFormatoAActual(formatoA);
        proyecto.setAnteproyecto(anteproyecto);
        proyecto.setPersonas(estudiantes);
        proyecto.setTitulo(formatoA.getTitulo());
        proyecto.setEstado("ACTIVO");

        // 🔹 Persistir proyecto completo
        return proyectoRepository.save(proyecto);
    }
}
