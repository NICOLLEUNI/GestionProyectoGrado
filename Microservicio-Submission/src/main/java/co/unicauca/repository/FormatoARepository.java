package co.unicauca.repository;

import co.unicauca.entity.FormatoA;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FormatoARepository extends JpaRepository<FormatoA,Long> {
    @Override
    Optional<FormatoA> findById(Long id);

    // Método para buscar por director O codirector
    List<FormatoA> findByProjectManagerEmailOrProjectCoManagerEmail(
            String projectManagerEmail,
            String projectCoManagerEmail);
}
