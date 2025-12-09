package co.unicauca.application.ports.output;

import co.unicauca.domain.entities.Anteproyecto;

import java.util.List;
import java.util.Optional;

public interface AnteproyectoRepoOutPort {
    List<Anteproyecto> findAll();
    Optional<Anteproyecto> findById(Long id);
    Anteproyecto save(Anteproyecto anteproyecto);
}
