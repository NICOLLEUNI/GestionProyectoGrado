package co.unicauca.repository;

import co.unicauca.entity.FormatoA;
import co.unicauca.entity.FormatoAVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FormatoVersionRepository extends JpaRepository<FormatoAVersion, Long> {

    @Query("SELECT MAX(fv.numeroVersion) FROM FormatoAVersion fv WHERE fv.formatoA.id = :formatoAId")
    Integer findMaxVersionByFormatoAId(@Param("formatoAId") Long formatoAId);

    FormatoAVersion findTopByFormatoAOrderByNumeroVersionDesc(FormatoA formatoA);

}
