package co.unicauca.application.ports.output;

import co.unicauca.domain.entities.FormatoA;

import java.util.List;
import java.util.Optional;


public interface FormatoARepoOutPort {

    FormatoA save(FormatoA formatoA);

    Optional<FormatoA> findById(Long id);

    List<FormatoA> findAll();

}
