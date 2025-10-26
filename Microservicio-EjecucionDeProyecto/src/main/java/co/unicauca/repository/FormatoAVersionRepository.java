package co.unicauca.repository;

import co.unicauca.entity.FormatoAVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FormatoAVersionRepository extends JpaRepository<FormatoAVersion, Long> {

    // Método para obtener todas las versiones de un FormatoA por su id

    List<FormatoAVersion> findByIdFormatoA(Long idFormatoA);
}