package co.unicauca.service;

import co.unicauca.entity.*;
import co.unicauca.infra.dto.FormatoAEditRequest;
import co.unicauca.infra.dto.FormatoARequest;
import co.unicauca.infra.dto.FormatoAResponse;
import co.unicauca.infra.dto.notification.FormatoAnotification;
import co.unicauca.infra.messaging.RabbitMQPublisher;
import co.unicauca.repository.FormatoARepository;
import co.unicauca.repository.PersonaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class FormatoAService {

    private final FormatoARepository formatoARepository;
    private final PersonaRepository personaRepository;
    private final ProyectoService proyectoService;
    private final VersionService versionService;
    private final RabbitMQPublisher rabbitMQPublisher;
    private final String RUTA_BASE = System.getProperty("user.dir") + "/uploads/";

    public FormatoAService(FormatoARepository formatoARepository, PersonaRepository personaRepository, ProyectoService proyectoService, VersionService versionService, RabbitMQPublisher rabbitMQPublisher) {
        this.formatoARepository = formatoARepository;
        this.personaRepository = personaRepository;
        this.proyectoService = proyectoService;
        this.versionService = versionService;
        this.rabbitMQPublisher = rabbitMQPublisher;
    }

    public Optional<FormatoA> findById(Long id) {
        return formatoARepository.findById(id);
    }


    public List<FormatoA> findByProjectManagerEmailOrProjectCoManagerEmail(String email) {
        return formatoARepository.findByProjectManagerEmailOrProjectCoManagerEmail(email, email);
    }

    public boolean eliminarFormatoA(Long id) {
        if (formatoARepository.existsById(id)) {
            formatoARepository.deleteById(id);
            return true;
        }
        return false;
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

        // ⭐⭐ CONVERTIR A RESPONSE Y PUBLICAR ⭐⭐
        FormatoAResponse response = convertirAFormatoAResponse(formatoAGuardado);
        rabbitMQPublisher.publicarFormatoACreado(response);

        // 2. Para notificaciones (solo correos)
        FormatoAnotification notificacion = convertirAFormatoANotificacionEvent(formatoAGuardado);
        rabbitMQPublisher.publicarNotificacionFormatoACreado(notificacion);

        return formatoAGuardado;
    }

    public boolean saveFormatoAPDF(Long id, MultipartFile file) {
        try {
            FormatoA formato = formatoARepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("FormatoA no encontrado con id: " + id));

            String carpetaDestino = RUTA_BASE + "formatoA/";
            File directorio = new File(carpetaDestino);
            if (!directorio.exists()) directorio.mkdirs();

            String nombreArchivo = "FormatoA_" + id + "_" + file.getOriginalFilename();
            File archivoDestino = new File(directorio, nombreArchivo);
            file.transferTo(archivoDestino);

            formato.setArchivoPDF(archivoDestino.getAbsolutePath());
            formatoARepository.save(formato);

            // ❌ NO se crean versiones ni se publica nada
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveCartaLaboral(Long id, MultipartFile file) {
        try {
            FormatoA formato = formatoARepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("FormatoA no encontrado con id: " + id));

            String nombreArchivo = "CartaLaboral_" + id + "_" + file.getOriginalFilename();
            File destino = new File(RUTA_BASE + nombreArchivo);
            destino.getParentFile().mkdirs();
            file.transferTo(destino);

            formato.setCartaLaboral(destino.getAbsolutePath());
            formatoARepository.save(formato);

            // ❌ Tampoco se publican eventos
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Convierte FormatoA entity a FormatoAResponse DTO
     */
    private FormatoAResponse convertirAFormatoAResponse(FormatoA formatoA) {
        return new FormatoAResponse(
                formatoA.getId(),
                formatoA.getTitle(),
                formatoA.getMode().name(),
                formatoA.getProjectManagerEmail(),
                formatoA.getProjectCoManagerEmail(),
                formatoA.getGeneralObjetive(),
                formatoA.getSpecificObjetives(),
                formatoA.getArchivoPDF(),
                formatoA.getCartaLaboral(),
                formatoA.getEstudianteEmails(), // ← Asegúrate que este método existe
                formatoA.getCounter()
        );
    }

    private FormatoAnotification convertirAFormatoANotificacionEvent(FormatoA formatoA) {
        return new FormatoAnotification(
                formatoA.getId(),
                formatoA.getTitle(),
                formatoA.getEstudianteEmails(), // Estudiantes para buscar programa
                formatoA.getProjectManagerEmail() // Director para buscar departamento
        );
    }

    @Transactional
    public FormatoA reenviarFormatoARechazado(FormatoAEditRequest request) {
        // Buscar el FormatoA
        FormatoA formatoA = formatoARepository.findById(request.id())
                .orElseThrow(() -> new RuntimeException("FormatoA no encontrado: " + request.id()));

        // Validar que esté RECHAZADO
        if (formatoA.getState() != EnumEstado.RECHAZADO) {
            throw new RuntimeException("Solo se pueden reenviar los FormatosA RECHAZADOS");
        }

        // Validar límite de reenvíos
        if (formatoA.getCounter() >= 3) {
            throw new RuntimeException("El FormatoA ha sido rechazado más de 3 veces y no puede reenviarse");
        }

        // Actualizar los campos editables
        formatoA.setArchivoPDF(request.archivoPDF());
        formatoA.setCartaLaboral(request.cartaLaboral());
        formatoA.setGeneralObjetive(request.generalObjetive());
        formatoA.setSpecificObjetives(request.specificObjetives());
        formatoA.setState(EnumEstado.ENTREGADO); // vuelve a ENTREGADO

        FormatoA actualizado = formatoARepository.save(formatoA);

        // Crear la nueva versión reenviada
        FormatoAVersion nuevaVersion = versionService.crearVersionReenviada(actualizado, request);

        // Publicar los eventos
        FormatoAResponse response = convertirAFormatoAResponse(actualizado);
        rabbitMQPublisher.publicarFormatoACreado(response);

        FormatoAnotification notificacion = convertirAFormatoANotificacionEvent(actualizado);
        rabbitMQPublisher.publicarNotificacionFormatoACreado(notificacion);

        return actualizado;
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

    public List<FormatoA> listarFormatosAPorDocente(String emailDocente) {
        // Validar que el docente existe y es realmente docente
        Persona docente = personaRepository.findByEmail(emailDocente)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado: " + emailDocente));

        if (!docente.esDocente()) {
            throw new RuntimeException("El usuario " + emailDocente + " no es docente");
        }

        // Buscar formatos donde el docente sea director O codirector
        return formatoARepository.findByProjectManagerEmailOrProjectCoManagerEmail(
                emailDocente, emailDocente);
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
