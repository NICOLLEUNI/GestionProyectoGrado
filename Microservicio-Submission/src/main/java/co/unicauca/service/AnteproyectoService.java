package co.unicauca.service;

import co.unicauca.entity.Anteproyecto;
import co.unicauca.entity.EnumEstado;
import co.unicauca.entity.EnumEstadoAnteproyecto;
import co.unicauca.entity.ProyectoGrado;
import co.unicauca.repository.AnteproyectoRepository;
import co.unicauca.repository.ProyectoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Transactional
public class AnteproyectoService {

    private final AnteproyectoRepository anteproyectoRepository;
    private final ProyectoRepository proyectoRepository;

    public AnteproyectoService(AnteproyectoRepository anteproyectoRepository, ProyectoRepository proyectoRepository) {
        this.anteproyectoRepository = anteproyectoRepository;
        this.proyectoRepository = proyectoRepository;
    }

    /**
     * Subir anteproyecto buscando por coincidencia de título con FormatoA
     */
    public Anteproyecto subirAnteproyecto(Anteproyecto anteproyecto) {
        // 1. BUSCAR proyecto por título coincidente con FormatoA aprobado
        ProyectoGrado proyecto = proyectoRepository
                .findByFormatoAActualTitleAndFormatoAActualState(
                        anteproyecto.getTitulo(),
                        EnumEstado.APROBADO
                )
                .orElseThrow(() -> new RuntimeException(
                        "No se encontró proyecto con FormatoA aprobado que coincida con el título: " +
                                anteproyecto.getTitulo()
                ));

        // 2. VALIDAR que puede subir anteproyecto
        validarPuedeSubirAnteproyecto(proyecto);

        // 3. CONFIGURAR y GUARDAR anteproyecto
        anteproyecto.setFechaCreacion(LocalDate.now());
        anteproyecto.setEstado(EnumEstadoAnteproyecto.ENTREGADO);
        anteproyecto.setProyectoGrado(proyecto);

        Anteproyecto anteproyectoGuardado = anteproyectoRepository.save(anteproyecto);

        // 4. ACTUALIZAR proyecto
        proyecto.setAnteproyecto(anteproyectoGuardado);
        proyectoRepository.save(proyecto);

        return anteproyectoGuardado;
    }

    private void validarPuedeSubirAnteproyecto(ProyectoGrado proyecto) {
        // Validaciones adicionales
        if (proyecto.getAnteproyecto() != null) {
            throw new RuntimeException("El proyecto ya tiene un anteproyecto asignado");
        }

        if (!"ACTIVO".equals(proyecto.getEstado())) {
            throw new RuntimeException("El proyecto debe estar ACTIVO");
        }

        //  Validar que los títulos coincidan exactamente
        if (!proyecto.getFormatoAActual().getTitle().equals(proyecto.getNombre())) {
            throw new RuntimeException("El título del anteproyecto debe coincidir con el título del FormatoA aprobado");
        }
    }
}
