package co.unicauca.repository;

import co.unicauca.entity.ProyectoGrado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProyectoRepository extends JpaRepository<ProyectoGrado, Long> {
}
