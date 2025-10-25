package co.unicauca.service;

import co.unicauca.entity.AnteproyectoEntity;
import co.unicauca.entity.ProyectoGradoEntity;
import co.unicauca.repository.AnteproyectoRepository;
import co.unicauca.repository.ProyectoGradoRepository;
import co.unicauca.infra.dto.AnteproyectoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnteproyectoService {

    @Autowired
    private AnteproyectoRepository anteproyectoRepository;

    @Autowired
    private ProyectoGradoRepository proyectoGradoRepository;

    /**
     * Método que mapea el AnteproyectoResponse y lo guarda internamente en la base de datos
     */
    public void saveInterno(AnteproyectoResponse response) {
        // Obtener ProyectoGrado de la base de datos usando el idProyectoGrado
        ProyectoGradoEntity proyectoGrado = proyectoGradoRepository.findById(response.idProyectoGrado())
                .orElseThrow(() -> new RuntimeException("Proyecto Grado no encontrado"));

        // Crear una nueva entidad AnteproyectoEntity con la información recibida
        AnteproyectoEntity anteproyecto = new AnteproyectoEntity();
        anteproyecto.setId(response.id());
        anteproyecto.setTitulo(response.titulo());
        anteproyecto.setEstado(response.estado());
        anteproyecto.setObservaciones(response.observaciones());
        anteproyecto.setFecha(response.fecha());

        // Asociar el Anteproyecto con el ProyectoGrado
        anteproyecto.setProyectoGrado(proyectoGrado);

        // Guardar el Anteproyecto en la base de datos
        anteproyectoRepository.save(anteproyecto);
    }
}
