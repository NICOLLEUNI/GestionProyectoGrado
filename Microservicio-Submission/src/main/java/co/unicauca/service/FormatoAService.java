package co.unicauca.service;

import co.unicauca.entity.*;
import co.unicauca.infra.dto.FormatoARequest;
import co.unicauca.repository.FormatoARepository;
import co.unicauca.repository.FormatoVersionRepository;
import co.unicauca.repository.PersonaRepository;
import co.unicauca.repository.ProyectoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class FormatoAService {

    private final FormatoARepository formatoARepository;
    private final PersonaRepository personaRepository;
    private final ProyectoService proyectoService;
    private final VersionService versionService;

    public FormatoAService(FormatoARepository formatoARepository, PersonaRepository personaRepository, ProyectoService proyectoService, VersionService versionService) {
        this.formatoARepository = formatoARepository;
        this.personaRepository = personaRepository;
        this.proyectoService = proyectoService;
        this.versionService = versionService;
    }

    //metodo para subir un formatoA
    @Transactional
    public FormatoA subirFormatoA(FormatoA formatoA) {

        // VALIDAR participantes del FormatoA
        validarParticipantes(formatoA);

        // Validar el FormatoA según reglas de negocio
        //formatoA.validar();

        FormatoA formatoAGuardado = formatoARepository.save(formatoA);

        FormatoAVersion version1 = versionService.crearVersionInicial(formatoAGuardado);

        proyectoService.crearProyectoGrado(formatoAGuardado, version1);

        return formatoAGuardado;
    }

    @Transactional
    public FormatoA actualizarFormatoAEvaluado(FormatoARequest request) {
        // 1. BUSCAR FormatoA existente
        FormatoA formatoA = formatoARepository.findById(request.id())
                .orElseThrow(() -> new RuntimeException("FormatoA no encontrado: " + request.id()));

        // 2. ACTUALIZAR FormatoA principal
        formatoA.setState(EnumEstado.valueOf(request.state()));
        formatoA.setObservations(request.observations());
        formatoA.setCounter(Integer.parseInt(request.counter()));

        FormatoA formatoAActualizado = formatoARepository.save(formatoA);

        // 3. CREAR NUEVA VERSIÓN (llamando a VersionService)
        FormatoAVersion nuevaVersion = versionService.crearVersionConEvaluacion(formatoAActualizado, request);

        // 4. AGREGAR nueva versión al ProyectoGrado
        proyectoService.agregarVersionAProyectoGrado(formatoAActualizado, nuevaVersion);

        return formatoAActualizado;
    }



    private void validarParticipantes(FormatoA formatoA) {
        // Validar director
        Persona director = personaRepository.findByEmail(formatoA.getProjectManagerEmail())
                .orElseThrow(() -> new RuntimeException("Director no encontrado: " + formatoA.getProjectManagerEmail()));

        if (!director.esDocente()) {
            throw new RuntimeException("El director debe ser docente");
        }

        // Validar codirector (si existe)
        if (formatoA.getProjectCoManagerEmail() != null && !formatoA.getProjectCoManagerEmail().isBlank()) {
            Persona codirector = personaRepository.findByEmail(formatoA.getProjectCoManagerEmail())
                    .orElseThrow(() -> new RuntimeException("Codirector no encontrado: " + formatoA.getProjectCoManagerEmail()));

            if (!codirector.esDocente()) {
                throw new RuntimeException("El codirector debe ser docente");
            }
        }

        // Validar estudiantes
        for (String emailEstudiante : formatoA.getEstudianteEmails()) {
            Persona estudiante = personaRepository.findByEmail(emailEstudiante)
                    .orElseThrow(() -> new RuntimeException("Estudiante no encontrado: " + emailEstudiante));

            if (!estudiante.esEstudiante()) {
                throw new RuntimeException("El usuario " + emailEstudiante + " no es estudiante");
            }
        }
    }



    





}
