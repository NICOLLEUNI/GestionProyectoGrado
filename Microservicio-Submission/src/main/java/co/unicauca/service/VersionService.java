package co.unicauca.service;

import co.unicauca.entity.EnumEstado;
import co.unicauca.entity.FormatoA;
import co.unicauca.entity.FormatoAVersion;
import co.unicauca.repository.FormatoVersionRepository;

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
}
