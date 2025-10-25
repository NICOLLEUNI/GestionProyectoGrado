package co.unicauca.service;

import co.unicauca.entity.EnumEstado;
import co.unicauca.entity.EnumModalidad;
import co.unicauca.entity.FormatoAEntity;
import co.unicauca.entity.PersonaEntity;
import co.unicauca.infra.dto.FormatoARequest;
import co.unicauca.repository.FormatoARepository;
import co.unicauca.repository.PersonaRepository;
import co.unicauca.infra.dto.FormatoAResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FormatoAService {

    @Autowired
    private FormatoARepository formatoARepository;

    @Autowired
    private PersonaRepository personaRepository;

    /**
     * Método que mapea los datos de FormatoAResponse y guarda el FormatoA en la base de datos.
     */
    public FormatoAEntity saveFormatoA(FormatoARequest request) {
        // Crear un nuevo FormatoAEntity con los datos del response
        FormatoAEntity formatoA = new FormatoAEntity();

        // 🔹 Buscar los estudiantes por sus correos
        if (request.estudiante() != null) {
            List<PersonaEntity> estudiantes = request.estudiante().stream()
                    .map(email -> personaRepository.findByEmail(email).orElse(null))
                    .filter(e -> e != null)
                    .collect(Collectors.toList());
            formatoA.setEstudiantes(estudiantes);
        }

        // Asignar el resto de los campos a la entidad
        formatoA.setId(request.id());  // Convertir el ID de String a Long
        formatoA.setTitle(request.title());
        formatoA.setMode(request.mode());  // Usar EnumModalidad
        formatoA.setCounter(request.counter());
        formatoA.setArchivoPDF(request.archivoPDF());
        formatoA.setCartaLaboral(request.cartaLaboral());
        formatoA.setGeneralObjetive(request.generalObjetive());
        formatoA.setSpecificObjetives(request.specificObjetives());


        // 🔹 Buscar director y codirector por correo (Persona)
        PersonaEntity director = personaRepository.findByEmail(request.projectManagerEmail()).orElse(null);
        PersonaEntity codirector = personaRepository.findByEmail(request.projectCoManagerEmail()).orElse(null);

        formatoA.setProjectManager(director);
        formatoA.setProjectCoManager(codirector);

        formatoA.setState(EnumEstado.ENTREGADO);
        formatoARepository.save(formatoA);

        return formatoA;


    }

    public List<FormatoAEntity> findAll() {
        return formatoARepository.findAll();
    }



}
