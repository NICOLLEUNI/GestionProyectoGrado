package co.unicauca.infrastructure.adapters.output;

import co.unicauca.application.ports.output.FormatoARepoOutPort;
import co.unicauca.domain.entities.FormatoA;
import co.unicauca.infrastructure.adapters.output.persistence.entities.FormatoAEntity;
import co.unicauca.infrastructure.adapters.output.persistence.mapper.FormatoAMaper;
import co.unicauca.infrastructure.adapters.output.persistence.repository.FormatoAJPARepo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FormatoARepoAdapter implements FormatoARepoOutPort {

    private final FormatoAJPARepo jpaRepository;

    public FormatoARepoAdapter(FormatoAJPARepo jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public FormatoA save(FormatoA formatoA) {
        FormatoAEntity entity = FormatoAMaper.toEntity(formatoA);
        FormatoAEntity saved = jpaRepository.save(entity);
        return FormatoAMaper.toDomain(saved);
    }

    @Override
    public Optional<FormatoA> findById(Long id) {
        return jpaRepository.findById(id)
                .map(FormatoAMaper::toDomain);
    }

    @Override
    public List<FormatoA> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(FormatoAMaper::toDomain)
                .collect(Collectors.toList());
    }
}
