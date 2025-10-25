package co.unicauca.service;

import co.unicauca.entity.AnteproyectoEntity;
import co.unicauca.entity.FormatoAEntity;
import co.unicauca.entity.ProyectoGradoEntity;
import co.unicauca.repository.AnteproyectoRepository;
import co.unicauca.repository.FormatoARepository;
import co.unicauca.repository.ProyectoGradoRepository;
import co.unicauca.infra.dto.ProyectoGradoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProyectoGradoService {

    @Autowired
    private ProyectoGradoRepository proyectoGradoRepository;

    @Autowired
    private FormatoARepository formatoARepository;

    @Autowired
    private AnteproyectoRepository anteproyectoRepository;

    /**
     * Método para guardar internamente un ProyectoGrado
     * @param response Respuesta del ProyectoGrado (ProyectoGradoResponse)
     */
    public ProyectoGradoEntity saveInterno(ProyectoGradoResponse response) {
        // Obtener FormatoA y Anteproyecto desde la base de datos usando los IDs
        FormatoAEntity formatoA = formatoARepository.findById(response.idFormatoA())
                .orElseThrow(() -> new RuntimeException("Formato A no encontrado"));
        AnteproyectoEntity anteproyecto = anteproyectoRepository.findById(response.idAnteproyecto())
                .orElseThrow(() -> new RuntimeException("Anteproyecto no encontrado"));

        // Crear una nueva entidad ProyectoGrado con los datos del response
        ProyectoGradoEntity proyectoGrado = new ProyectoGradoEntity();
        proyectoGrado.setId(response.id());
        proyectoGrado.setTitulo(response.title());
        proyectoGrado.setFechaCreacion(response.fecha().atStartOfDay());  // Convertir fecha a LocalDateTime
        proyectoGrado.setFormatoAActual(formatoA);
        proyectoGrado.setAnteproyecto(anteproyecto);

        // Guardar el ProyectoGrado en la base de datos
       return proyectoGradoRepository.save(proyectoGrado);
    }
}
