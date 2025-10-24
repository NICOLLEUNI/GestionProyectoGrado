package co.unicauca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import co.unicauca.entity.FormatoAVersionEntity;
import java.util.List;

public interface FormatoAVersionRepository extends JpaRepository<FormatoAVersionEntity,Long> {
    List<FormatoAVersionEntity> findByFormatoA_Id(String formatoAId);

}
