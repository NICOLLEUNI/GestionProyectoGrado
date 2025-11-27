package co.unicauca.repository;

import co.unicauca.entity.FormatoAVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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


    /**
     * Elimina todas las versiones asociadas a un FormatoA por su ID
     */
    @Modifying
    @Query("DELETE FROM FormatoAVersion f WHERE f.idFormatoA = :formatoAId")
    void deleteByFormatoAId(@Param("formatoAId") Long formatoAId);

    /**
     * Elimina una versión específica por su ID y FormatoA ID
     */
    @Modifying
    @Query("DELETE FROM FormatoAVersion f WHERE f.id = :id AND f.idFormatoA = :formatoAId")
    void deleteByIdAndFormatoAId(@Param("id") Long id, @Param("formatoAId") Long formatoAId);

    /**
     * Elimina versiones por estado y FormatoA ID
     */
    @Modifying
    @Query("DELETE FROM FormatoAVersion f WHERE f.idFormatoA = :formatoAId AND f.state = :state")
    void deleteByFormatoAIdAndState(@Param("formatoAId") Long formatoAId, @Param("state") String state);

    /**
     * Elimina versiones anteriores a un número de versión específico
     */
    @Modifying
    @Query("DELETE FROM FormatoAVersion f WHERE f.idFormatoA = :formatoAId AND f.numeroVersion < :versionNumber")
    void deleteVersionesAnteriores(@Param("formatoAId") Long formatoAId, @Param("versionNumber") Integer versionNumber);

    /**
     * Elimina todas las versiones excepto la más reciente
     */
    @Modifying
    @Query("DELETE FROM FormatoAVersion f WHERE f.idFormatoA = :formatoAId AND f.id NOT IN (" +
            "SELECT MAX(f2.id) FROM FormatoAVersion f2 WHERE f2.idFormatoA = :formatoAId)")
    void deleteAllExceptLatest(@Param("formatoAId") Long formatoAId);

}