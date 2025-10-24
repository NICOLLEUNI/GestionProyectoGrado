package co.unicauca.service;

import co.unicauca.entity.AnteproyectoEntity;
import co.unicauca.repository.AnteproyectoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AnteproyectoService {

    private final AnteproyectoRepository anteproyectoRepository;

    public AnteproyectoService(AnteproyectoRepository anteproyectoRepository) {
        this.anteproyectoRepository = anteproyectoRepository;
    }

    /**
     * Guarda la entidad si es nueva o actualiza si ya existe.
     */
    public AnteproyectoEntity saveOrUpdate(AnteproyectoEntity entity) {
        if (entity.getId() != null) {
            Optional<AnteproyectoEntity> existing = anteproyectoRepository.findById(entity.getId());
            if (existing.isPresent()) {
                AnteproyectoEntity actual = existing.get();
                actual.setTitulo(entity.getTitulo());
                actual.setEstado(entity.getEstado());
                actual.setFecha(entity.getFecha());
                actual.setObservaciones(entity.getObservaciones());
                actual.setProyectoGrado(entity.getProyectoGrado());
                return anteproyectoRepository.save(actual);
            }
        }
        return anteproyectoRepository.save(entity);
    }

    /**
     * Buscar por ID
     */
    public AnteproyectoEntity findById(Long id) {
        return anteproyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Anteproyecto no encontrado con ID: " + id));
    }
}
