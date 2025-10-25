package co.unicauca.repository;

import co.unicauca.entity.ProyectoGradoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProyectoGradoRepository extends JpaRepository<ProyectoGradoEntity, Long> {

    // Método para encontrar un ProyectoGradoEntity por id
    Optional<ProyectoGradoEntity> findById(Long id);

    // Puedes agregar más métodos personalizados según sea necesario
    // Ejemplo: Buscar por título
    Optional<ProyectoGradoEntity> findByTitulo(String titulo);

    // Método para obtener el ProyectoGrado por estado
    Optional<ProyectoGradoEntity> findByEstado(String estado);

    boolean existsByEstudiantesEmailContainsAndEstadoNot(String emailEstudiante, String estado);
}
