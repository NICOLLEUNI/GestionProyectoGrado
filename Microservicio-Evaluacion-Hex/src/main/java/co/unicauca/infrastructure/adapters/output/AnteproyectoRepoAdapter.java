package co.unicauca.infrastructure.adapters.output;

import co.unicauca.application.ports.output.AnteproyectoRepoOutPort;
import co.unicauca.domain.entities.Anteproyecto;
import co.unicauca.infrastructure.adapters.output.persistence.entities.AnteproyectoEntity;
import co.unicauca.infrastructure.adapters.output.persistence.mapper.AnteproyectoMaper;
import co.unicauca.infrastructure.adapters.output.persistence.repository.AnteproyectoJPARepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnteproyectoRepoAdapter implements AnteproyectoRepoOutPort {
    @Autowired
    private AnteproyectoJPARepo anteproyectoJPARepo;


    @Override
    public List<Anteproyecto> findAll() {
        return anteproyectoJPARepo.findAll().stream()
                .map(AnteproyectoMaper::toAnteproyecto) // Mapea cada BookEntity a Book
                .collect(Collectors.toList()); // Colecta los resultados en una lista de Book
    }

    @Override
    public Optional<Anteproyecto> findById(Long id) {
        Optional<AnteproyectoEntity> anteproyectoEntity = anteproyectoJPARepo.findById(id);
        if (anteproyectoEntity.isEmpty()) {
            return Optional.empty();
        }
        Anteproyecto anteproyecto = AnteproyectoMaper.toAnteproyecto(anteproyectoEntity.get());

        return Optional.of(anteproyecto);
    }

    @Override
    public Anteproyecto save(Anteproyecto anteproyecto) {
        AnteproyectoEntity anteproyectoEntity =  AnteproyectoMaper.toAnteproyectoEntity(anteproyecto);
        anteproyectoEntity = anteproyectoJPARepo.save(anteproyectoEntity);
        return AnteproyectoMaper.toAnteproyecto(anteproyectoEntity);
    }
}
