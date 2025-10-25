package co.unicauca.service;

import co.unicauca.entity.EnumEstado;
import co.unicauca.entity.EnumModalidad;
import co.unicauca.entity.FormatoAEntity;
import co.unicauca.entity.FormatoAVersionEntity;
import co.unicauca.infra.dto.FormatoAUpdateRequest;
import co.unicauca.infra.dto.FormatoAUpdateResponse;
import co.unicauca.repository.FormatoAVersionRepository;
import co.unicauca.repository.FormatoARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormatoAVersionService {

    @Autowired
    private FormatoARepository formatoARepository;

    @Autowired
    private FormatoAVersionRepository formatoAVersionRepository;

    /**
     * Método para guardar internamente una versión de FormatoA
     * @param request Respuesta de FormatoAUpdateResponse
     */
    public FormatoAVersionEntity saveInterno(FormatoAUpdateRequest request) {
        // Obtener el FormatoA original desde la base de datos utilizando el id recibido en el response
        FormatoAEntity formatoA = formatoARepository.findById(request.id())
                .orElseThrow(() -> new RuntimeException("FormatoA no encontrado"));

        // Crear una nueva versión de FormatoA
        FormatoAVersionEntity version = new FormatoAVersionEntity();
        version.setTitle(formatoA.getTitle()); // Mantener el título del formato original
        version.setMode(EnumModalidad.valueOf(formatoA.getMode())); // Mantener la modalidad del formato original
        version.setGeneralObjetive(formatoA.getGeneralObjetive()); // Mantener el objetivo general
        version.setSpecificObjetives(formatoA.getSpecificObjetives()); // Mantener los objetivos específicos
        version.setArchivoPDF(formatoA.getArchivoPDF()); // Mantener el archivo PDF original
        version.setCartaLaboral(formatoA.getCartaLaboral()); // Mantener la carta laboral original
        version.setState(EnumEstado.valueOf(request.estado()));  // Asignar el nuevo estado recibido
        version.setObservations(request.observaciones()); // Asignar las observaciones recibidas
        version.setCounter(request.counter()); // Asignar el contador recibido

        // Asociar la nueva versión al FormatoA
        version.setFormatoA(formatoA);

        // Guardar la nueva versión en la base de datos
        formatoAVersionRepository.save(version);

        // Actualizar el FormatoA con los nuevos valores de estado y observaciones
        formatoA.setState(EnumEstado.valueOf(request.estado()));
        formatoA.setCounter(request.counter()); // Actualizar el contador si es necesario
        formatoARepository.save(formatoA); // Guardar el FormatoA actualizado en la base de datos

        return version;
    }
}
