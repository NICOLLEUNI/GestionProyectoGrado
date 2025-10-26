package co.unicauca.service;

import co.unicauca.entity.Anteproyecto;
import co.unicauca.entity.EnumEstadoAnteproyecto;
import co.unicauca.entity.ProyectoGrado;
import co.unicauca.infra.dto.AnteproyectoRequest;
import co.unicauca.infra.dto.AnteproyectoResponse;
import co.unicauca.repository.AnteproyectoRepository;
import co.unicauca.repository.ProyectoGradoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnteproyectoService {

    private final AnteproyectoRepository anteproyectoRepository;
    private final ProyectoGradoRepository proyectoGradoRepository; // ✅ Usar Repository directamente

    @Transactional
    public AnteproyectoResponse crearAnteproyecto(AnteproyectoRequest request) {
        // Validar que el proyecto exista
        ProyectoGrado proyecto = proyectoGradoRepository.findById(request.idProyectoGrado())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + request.idProyectoGrado()));

        Anteproyecto anteproyecto = new Anteproyecto();
        // ✅ JPA generará automáticamente el ID si es null
        // if (request.id() != null) {
        //     anteproyecto.setId(request.id());
        // }
        anteproyecto.setTitulo(request.titulo());
        anteproyecto.setFechaCreacion(request.fecha());
        anteproyecto.setEstado(EnumEstadoAnteproyecto.valueOf(request.estado()));
        anteproyecto.setObservaciones(request.observaciones());

        proyecto.establecerAnteproyecto(anteproyecto);

        Anteproyecto guardado = anteproyectoRepository.save(anteproyecto);
        return convertirAResponse(guardado);
    }

    @Transactional(readOnly = true)
    public AnteproyectoResponse buscarPorId(Long id) {
        Anteproyecto anteproyecto = anteproyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Anteproyecto no encontrado"));
        return convertirAResponse(anteproyecto);
    }

    @Transactional
    public AnteproyectoResponse actualizarAnteproyecto(Long id, AnteproyectoRequest request) {
        Anteproyecto anteproyecto = anteproyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Anteproyecto no encontrado"));

        anteproyecto.setTitulo(request.titulo());
        anteproyecto.setFechaCreacion(request.fecha());
        anteproyecto.setEstado(EnumEstadoAnteproyecto.valueOf(request.estado()));
        anteproyecto.setObservaciones(request.observaciones());

        Anteproyecto actualizado = anteproyectoRepository.save(anteproyecto);
        return convertirAResponse(actualizado);
    }


    private AnteproyectoResponse convertirAResponse(Anteproyecto anteproyecto) {
        return new AnteproyectoResponse(
                anteproyecto.getId(),
                anteproyecto.getTitulo(),
                anteproyecto.getFechaCreacion(),
                anteproyecto.getEstado().name(),
                anteproyecto.getObservaciones(),
                anteproyecto.getProyectoGrado() != null ? anteproyecto.getProyectoGrado().getId() : null
        );
    }
    public List<AnteproyectoResponse> obtenerTodos() {
        List<Anteproyecto> anteproyectos = anteproyectoRepository.findAll();
        return anteproyectos.stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    public List<AnteproyectoResponse> buscarPorProyecto(Long proyectoId) {
        List<Anteproyecto> anteproyectos = anteproyectoRepository.findByProyectoGradoId(proyectoId);
        return anteproyectos.stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }




}