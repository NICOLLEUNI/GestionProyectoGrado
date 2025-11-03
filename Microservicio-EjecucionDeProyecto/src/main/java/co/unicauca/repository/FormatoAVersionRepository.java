package co.unicauca.repository;

import co.unicauca.entity.FormatoAVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FormatoAVersionRepository extends JpaRepository<FormatoAVersion, Long> {

    // Método para obtener todas las versiones de un FormatoA por su id

    List<FormatoAVersion> findByIdFormatoA(Long idFormatoA);
    // En FormatoAVersionRepository
    List<FormatoAVersion> findByIdFormatoAOrderByNumeroVersionDesc(Long idFormatoA);
    List<FormatoAVersion> findByIdFormatoAOrderByNumeroVersionAsc(Long idFormatoA);
    Optional<FormatoAVersion> findByIdFormatoAAndNumeroVersion(Long idFormatoA, Integer numeroVersion);
    @Query("SELECT MAX(f.id) FROM FormatoAVersion f")
    Long findMaxId();

}