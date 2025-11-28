package co.unicauca.application.ports.output;

import co.unicauca.domain.entities.FormatoA;
import co.unicauca.infrastructure.dto.response.FormatoAResponse;

import java.util.List;
import java.util.Optional;


public interface FormatoARepoOutPort {

    FormatoA save(FormatoA formatoA);

    Optional<FormatoA> findById(Long id);

    List<FormatoA> findAll();

}
