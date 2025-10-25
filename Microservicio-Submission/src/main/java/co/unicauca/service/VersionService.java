package co.unicauca.service;

import co.unicauca.entity.EnumEstado;
import co.unicauca.entity.FormatoA;
import co.unicauca.entity.FormatoAVersion;
import co.unicauca.infra.dto.FormatoARequest;
import co.unicauca.repository.FormatoVersionRepository;
import org.springframework.stereotype.Service;

@Service
public class VersionService {

    private final FormatoVersionRepository formatoVersionRepository;

    public VersionService(FormatoVersionRepository formatoVersionRepository) {
        this.formatoVersionRepository = formatoVersionRepository;
    }

    public FormatoAVersion crearVersionInicial(FormatoA formatoA) {
        FormatoAVersion version1 = new FormatoAVersion();
        version1.setNumeroVersion(1);
        version1.setFecha(java.time.LocalDate.now());
        version1.setTitle(formatoA.getTitle());
        version1.setMode(formatoA.getMode());
        version1.setGeneralObjetive(formatoA.getGeneralObjetive());
        version1.setSpecificObjetives(formatoA.getSpecificObjetives());
        version1.setArchivoPDF(formatoA.getArchivoPDF());
        version1.setCartaLaboral(formatoA.getCartaLaboral());
        version1.setState(EnumEstado.ENTREGADO);
        version1.setObservations(null);
        version1.setCounter(0);
        version1.setFormatoA(formatoA);

        return formatoVersionRepository.save(version1);
    }

    public FormatoAVersion crearVersionConEvaluacion(FormatoA formatoAActualizado, FormatoARequest request) {
        FormatoAVersion version = new FormatoAVersion();
        version.setNumeroVersion(obtenerProximaVersion(formatoAActualizado));
        version.setFecha(java.time.LocalDate.now());

        //Copia los datos ACTUALIZADOS del FormatoA
        version.setTitle(formatoAActualizado.getTitle());
        version.setMode(formatoAActualizado.getMode());
        version.setGeneralObjetive(formatoAActualizado.getGeneralObjetive());
        version.setSpecificObjetives(formatoAActualizado.getSpecificObjetives());
        version.setArchivoPDF(formatoAActualizado.getArchivoPDF());
        version.setCartaLaboral(formatoAActualizado.getCartaLaboral());
        version.setState(formatoAActualizado.getState()); // Estado ACTUALIZADO
        version.setObservations(formatoAActualizado.getObservations()); //Observaciones ACTUALIZADAS
        version.setCounter(formatoAActualizado.getCounter()); //Counter ACTUALIZADO
        version.setFormatoA(formatoAActualizado);

        return formatoVersionRepository.save(version);
    }

    public int obtenerProximaVersion(FormatoA formatoA) {
        // Buscar la versión más alta para este FormatoA y sumar 1
        Integer maxVersion = formatoVersionRepository.findMaxVersionByFormatoAId(formatoA.getId());
        return (maxVersion != null ? maxVersion : 0) + 1;
    }
}
