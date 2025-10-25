package co.unicauca.service;

import co.unicauca.entity.EnumEstado;
import co.unicauca.entity.EnumModalidad;
import co.unicauca.entity.FormatoAEntity;
import co.unicauca.entity.PersonaEntity;
import co.unicauca.repository.FormatoARepository;
import co.unicauca.repository.PersonaRepository;
import co.unicauca.infra.dto.FormatoAResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormatoAService {

    @Autowired
    private FormatoARepository formatoARepository;

    @Autowired
    private PersonaRepository personaRepository;

    /**
     * Método que mapea los datos de FormatoAResponse y guarda el FormatoA en la base de datos.
     */
    public void saveFormatoA(FormatoAResponse response) {
        // Crear un nuevo FormatoAEntity con los datos del response
        FormatoAEntity formatoA = new FormatoAEntity();

        // Obtener los estudiantes por sus emails
        List<PersonaEntity> estudiantes = personaRepository.findAllByEmailIn(response.estudiante());

        // Asignar los estudiantes al formatoA
        formatoA.setEstudiantes(estudiantes);

        // Asignar el resto de los campos a la entidad
        formatoA.setId(Long.valueOf(response.id()));  // Convertir el ID de String a Long
        formatoA.setTitle(response.title());
        formatoA.setMode(EnumModalidad.valueOf(response.mode()));  // Usar EnumModalidad
        formatoA.setState(EnumEstado.valueOf("CREADO"));  // Estado inicial
        formatoA.setCounter(response.counter());
        formatoA.setProjectCoManagerEmail(response.projectCoManagerEmail());
        formatoA.setArchivoPDF(response.archivoPDF());
        formatoA.setCartaLaboral(response.cartaLaboral());
        formatoA.setGeneralObjetive(response.generalObjetive());
        formatoA.setSpecificObjetives(response.specificObjetives());

        // Guardar el FormatoA en la base de datos
        formatoARepository.save(formatoA);
    }
}
