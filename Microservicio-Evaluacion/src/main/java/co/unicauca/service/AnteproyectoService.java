package co.unicauca.service;

import co.unicauca.entity.Anteproyecto;
import co.unicauca.infra.dto.AnteproyectoRequest;
import co.unicauca.repository.AnteproyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AnteproyectoService {

    @Autowired
    private AnteproyectoRepository anteproyectoRepository;

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
}