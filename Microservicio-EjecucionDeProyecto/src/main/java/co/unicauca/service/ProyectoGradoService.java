package co.unicauca.service;

import co.unicauca.entity.*;
import co.unicauca.infra.dto.*;
import co.unicauca.repository.ProyectoGradoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProyectoGradoService {

    private final ProyectoGradoRepository proyectoRepository;

    @Transactional
    public ProyectoGradoResponse crearProyecto(ProyectoGradoRequest request) {
        ProyectoGrado proyecto = new ProyectoGrado();
        proyecto.setNombre(request.nombre());
        proyecto.setFechaCreacion(request.fecha().atStartOfDay());
        proyecto.setEstudiantesEmail(request.estudiantesEmail());
        proyecto.setEstado("ENTREGADO");
        proyecto.setIdFormatoA(request.idFormatoA());

        ProyectoGrado guardado = proyectoRepository.save(proyecto);
        return convertirAResponse(guardado);
    }

    @Transactional
    public ProyectoGradoResponse agregarVersionFormato(Long proyectoId, FormatoAVersionRequest versionRequest) {
        ProyectoGrado proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + proyectoId));

        // ✅ VALIDAR que el formatoAExternoId del proyecto coincida con el idFormatoA de la versión
        if (proyecto.getIdFormatoA() == null) {
            throw new RuntimeException("El proyecto no tiene FormatoA asociado");
        }

        if (!proyecto.getIdFormatoA().equals(versionRequest.IdFormatoA())) {
            throw new RuntimeException(
                    "El FormatoA de la versión (" + versionRequest.IdFormatoA() +
                            ") no coincide con el FormatoA del proyecto (" + proyecto.getIdFormatoA() + ")"
            );
        }

        // ✅ La versión se guarda INDEPENDIENTEMENTE en FormatoAVersionService
        // Este método solo valida la relación y consistencia de datos

        System.out.println("✅ Versión " + versionRequest.numVersion() +
                " validada para el proyecto " + proyectoId +
                " via FormatoA: " + versionRequest.IdFormatoA());

        return convertirAResponse(proyecto);
    }

    @Transactional(readOnly = true)
    public ProyectoGradoResponse buscarPorId(Long id) {
        ProyectoGrado proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + id));
        return convertirAResponse(proyecto);
    }

    @Transactional(readOnly = true)
    public List<ProyectoGradoResponse> listarTodos() {
        return proyectoRepository.findAll().stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional
    public ProyectoGradoResponse actualizarProyecto(Long id, ProyectoGradoRequest request) {
        ProyectoGrado proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + id));

        proyecto.setNombre(request.nombre());
        proyecto.setEstudiantesEmail(request.estudiantesEmail());
        proyecto.setIdFormatoA(request.idFormatoA());

        ProyectoGrado actualizado = proyectoRepository.save(proyecto);
        return convertirAResponse(actualizado);
    }

    @Transactional
    public void sincronizarFormatoA(Long proyectoId, Long idFormatoAExterno) {
        ProyectoGrado proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + proyectoId));
        proyecto.setIdFormatoA(idFormatoAExterno);
        proyectoRepository.save(proyecto);

        System.out.println("✅ FormatoA sincronizado: Proyecto " + proyectoId + " → FormatoA " + idFormatoAExterno);
    }

    /**
     * Buscar proyecto por formatoAExternoId (para el Listener)
     */
    @Transactional(readOnly = true)
    public Optional<ProyectoGrado> buscarPorFormatoAExternoId(Long idFormatoA) {
        return proyectoRepository.findByIdFormatoA(idFormatoA);
    }

    /**
     * Buscar versiones relacionadas a un proyecto (via formatoAExternoId)
     */
    @Transactional(readOnly = true)
    public List<FormatoAVersionResponse> buscarVersionesPorProyecto(Long proyectoId) {
        ProyectoGrado proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + proyectoId));

        if (proyecto.getIdFormatoA() == null) {
            throw new RuntimeException("El proyecto no tiene FormatoA asociado");
        }

        // Este método necesita ser implementado en FormatoAVersionService
        // return formatoAVersionService.buscarPorFormatoAExternoId(proyecto.getFormatoAExternoId());

        System.out.println("⚠️ buscarVersionesPorProyecto necesita implementación en FormatoAVersionService");
        return List.of();
    }

    // Método interno para uso de otros servicios
    public ProyectoGrado buscarEntidadPorId(Long id) {
        return proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + id));
    }

    private ProyectoGradoResponse convertirAResponse(ProyectoGrado proyecto) {
        Long idAnteproyecto = proyecto.getAnteproyecto() != null ? proyecto.getAnteproyecto().getId() : null;

        return new ProyectoGradoResponse(
                proyecto.getId(),
                proyecto.getNombre(),
                proyecto.getFechaCreacion().toLocalDate(),
                proyecto.getEstudiantesEmail(),
                proyecto.getIdFormatoA(),
                idAnteproyecto
        );
    }

    public List<ProyectoGradoResponse> obtenerTodosConRelaciones() {
        List<ProyectoGrado> proyectos = proyectoRepository.findAllWithEstudiantes();
        return proyectos.stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }
}