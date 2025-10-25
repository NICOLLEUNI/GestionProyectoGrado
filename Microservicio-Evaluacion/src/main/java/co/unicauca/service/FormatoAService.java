package co.unicauca.service;

import co.unicauca.entity.*;
import co.unicauca.infra.dto.*;
import co.unicauca.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FormatoAService {


    private final FormatoARepository formatoARepository;
    private final PersonaRepository personaRepository;

    public FormatoAService(FormatoARepository formatoARepository, PersonaRepository personaRepository) {
        this.formatoARepository = formatoARepository;
        this.personaRepository = personaRepository;
    }


    /**
     * Crea o actualiza un FormatoA en la base de datos.
     */
    public FormatoA guardarFormatoA(FormatoARequest request) {
        FormatoA formatoA;

        if (request.id() != null) {
            // 🔹 Intentar buscar el formato existente
            formatoA = formatoARepository.findById(request.id())
                    .orElseGet(FormatoA::new); // Si no existe, crear uno nuevo
        } else {
            formatoA = new FormatoA();
        }
        formatoA.setId(request.id());
        formatoA.setTitle(request.title());
        formatoA.setMode(request.mode());
        formatoA.setGeneralObjetive(request.generalObjetive());
        formatoA.setSpecificObjetives(request.specificObjetives());
        formatoA.setArchivoPDF(request.archivoPDF());
        formatoA.setCartaLaboral(request.cartaLaboral());
        formatoA.setCounter(request.counter());

        // 🔹 Buscar director y codirector por correo (Persona)
        Persona director = personaRepository.findByEmail(request.projectManagerEmail()).orElse(null);
        Persona codirector = personaRepository.findByEmail(request.projectCoManagerEmail()).orElse(null);

        formatoA.setProjectManager(director);
        formatoA.setProjectCoManager(codirector);

        // 🔹 Buscar los estudiantes por sus correos
        if (request.estudiante() != null) {
            List<Persona> estudiantes = request.estudiante().stream()
                    .map(email -> personaRepository.findByEmail(email).orElse(null))
                    .filter(e -> e != null)
                    .collect(Collectors.toList());
            formatoA.setEstudiantes(estudiantes);
        }

        formatoA.setState(EnumEstado.ENTREGADO);
        formatoARepository.save(formatoA);

        return formatoA;
    }

    /**
     * Obtiene todos los formatos A registrados.
     */
    public List<FormatoA> findAll() {
        return formatoARepository.findAll();
    }



    /**
     * Actualiza el estado del Formato A (por ejemplo, aprobado o rechazado)
     * y aumenta el contador en 1.
     */
    /**
     * 🔹 Actualiza el estado del FormatoA y devuelve el objeto completo
     */
    public Optional<FormatoA> actualizarEstado(Long id, EnumEstado newState, String observations) {
        Optional<FormatoA> formatoAOpt = formatoARepository.findById(id);
        if (formatoAOpt.isEmpty()) return Optional.empty();

        FormatoA formatoA = formatoAOpt.get();
        formatoA.setState(newState);
        formatoA.setObservations(observations);

        // Incrementar contador
        formatoA.setCounter(formatoA.getCounter() + 1);

        formatoARepository.save(formatoA);

        return Optional.of(formatoA);
    }


    /**
     * 🧭 Mapea un FormatoA a su DTO FormatoAResponse (para Submission)
     */
    public FormatoAResponse mapToFormatoAResponse(FormatoA formatoA) {
        return new FormatoAResponse(
                Math.toIntExact(formatoA.getId()),
                formatoA.getTitle(),
                formatoA.getState().toString(),
                formatoA.getObservations(),
                formatoA.getCounter()
        );
    }

    /**
     * ✉️ Mapea un FormatoA a su DTO FormatoAResponseNotificacion (para Notificación)
     */
    public FormatoAReponseNotificacion mapToFormatoAResponseNotificacion(FormatoA formatoA) {
        // 🔹 Obtener correos de estudiantes
        List<String> correosEstudiantes = formatoA.getEstudiantes()
                .stream()
                .map(Persona::getEmail)
                .toList();

        // 🔹 Obtener correos de docentes (director y codirector, si existen)
        List<String> correosDocentes = new ArrayList<>();
        if (formatoA.getProjectManager() != null && formatoA.getProjectManager().getEmail() != null) {
            correosDocentes.add(formatoA.getProjectManager().getEmail());
        }
        if (formatoA.getProjectCoManager() != null && formatoA.getProjectCoManager().getEmail() != null) {
            correosDocentes.add(formatoA.getProjectCoManager().getEmail());
        }

        // 🔹 Crear y devolver el DTO
        return new FormatoAReponseNotificacion(
                formatoA.getId(),
                formatoA.getTitle(),
                correosEstudiantes,
                correosDocentes
        );
    }
}



