package co.unicauca.service;

import co.unicauca.entity.FormatoAEntity;
import co.unicauca.repository.FormatoARepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FormatoAService {

    private final FormatoARepository formatoARepository;

    public FormatoAService(FormatoARepository formatoARepository) {
        this.formatoARepository = formatoARepository;
    }

    /**
     * Guarda la entidad si es nueva o actualiza si ya existe.
     */
    public FormatoAEntity saveOrUpdate(FormatoAEntity entity) {
        if (entity.getId() != null) {
            Optional<FormatoAEntity> existing = formatoARepository.findById(Long.valueOf(entity.getId()));
            if (existing.isPresent()) {
                // Actualizar campos relevantes
                FormatoAEntity actual = existing.get();
                actual.setTitulo(entity.getTitulo());
                actual.setEstado(entity.getEstado());
                actual.setModalidad(entity.getModalidad());
                actual.setCounter(entity.getCounter());
                // Si hay relaciones (versiones, personas), actualizarlas también
                return formatoARepository.save(actual);
            }
        }
        // Guardar nuevo
        return formatoARepository.save(entity);
    }

    /**
     * Busca un FormatoA por su ID.
     */
    public FormatoAEntity findById(Long id) {
        return formatoARepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FormatoA no encontrado con ID: " + id));
    }

    /**
     * Retorna todos los FormatoA.
     */
    public java.util.List<FormatoAEntity> findAll() {
        return formatoARepository.findAll();
    }
}
