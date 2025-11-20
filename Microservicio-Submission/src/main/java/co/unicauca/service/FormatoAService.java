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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Transactional
    public boolean eliminarFormatoA(Long id) {
        FormatoA formatoA = formatoARepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FormatoA no encontrado"));

        // Eliminar versiones PRIMERO
        versionService.eliminarVersionesPorFormatoA(id);

        // Eliminar proyecto asociado
        proyectoService.eliminarProyectoPorFormatoA(id);

        // Ahora sí eliminar FormatoA
        formatoARepository.delete(formatoA);

        return true;
    }

    //metodo para subir un formatoA
    @Transactional
    public FormatoA subirFormatoA(FormatoA formatoA) {

        validarParticipantes(formatoA);

        // Guardar primero el FormatoA base
        FormatoA formatoAGuardado = formatoARepository.save(formatoA);


        // Guardar nuevamente para actualizar rutas
        formatoAGuardado = formatoARepository.save(formatoAGuardado);

        // Crear versión inicial con rutas correctas
        FormatoAVersion version1 = versionService.crearVersionInicial(formatoAGuardado);

        proyectoService.crearProyectoGrado(formatoAGuardado, version1);

        // ⭐⭐ CONVERTIR A RESPONSE Y PUBLICAR ⭐⭐
        //FormatoAResponse response = convertirAFormatoAResponse(formatoAGuardado);
        //rabbitMQPublisher.publicarFormatoACreado(response);

        // 2. Para notificaciones (solo correos)
        //FormatoAnotification notificacion = convertirAFormatoANotificacionEvent(formatoAGuardado);
        //rabbitMQPublisher.publicarNotificacionFormatoACreado(notificacion);

        return formatoAGuardado;
    }

    @Transactional
    public FormatoA publicarFormatoA(Long id) {
        FormatoA formatoA = formatoARepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FormatoA no encontrado: " + id));

        // Validar que tenga los archivos necesarios
        if (formatoA.getArchivoPDF() == null || formatoA.getArchivoPDF().isBlank()) {
            throw new RuntimeException("No se puede publicar FormatoA sin PDF");
        }

        // Publicar en RabbitMQ SOLO AHORA (con archivos ya subidos)
        FormatoAResponse response = convertirAFormatoAResponse(formatoA);
        rabbitMQPublisher.publicarFormatoACreado(response);

        FormatoAnotification notificacion = convertirAFormatoANotificacionEvent(formatoA);
        rabbitMQPublisher.publicarNotificacionFormatoACreado(notificacion);

        return formatoA;
    }


    @Transactional
    public boolean saveFormatoAPDF(Long id, MultipartFile file) {
        try {
            FormatoA formato = formatoARepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("FormatoA no encontrado con id: " + id));

            // Carpeta relativa bajo RUTA_BASE
            String carpetaDestino = RUTA_BASE + File.separator + "formatoA" + File.separator;
            File directorio = new File(carpetaDestino);
            if (!directorio.exists()) directorio.mkdirs();

            // Construir nombre seguro
            String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : "archivo.pdf";
            String nombreArchivo = "FormatoA_" + id + "_" + original;
            File archivoDestino = new File(directorio, nombreArchivo);
            file.transferTo(archivoDestino);

            // Guardar ruta RELATIVA en la entidad (mejor para portabilidad)
            String rutaRelativa = "formatoA/" + nombreArchivo;
            formato.setArchivoPDF(rutaRelativa);
            formatoARepository.save(formato);

            // ⭐ DEBUG para confirmar
            System.out.println("🔍 Ruta guardada en BD: " + rutaRelativa);

            // Actualizar la última versión asociada para que tenga la ruta nueva
            versionService.actualizarRutasArchivos(formato);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public boolean saveCartaLaboral(Long id, MultipartFile file) {
        try {
            FormatoA formato = formatoARepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("FormatoA no encontrado con id: " + id));

            String carpetaDestino = RUTA_BASE + File.separator + "cartaLaboral" + File.separator;
            File directorio = new File(carpetaDestino);
            if (!directorio.exists()) directorio.mkdirs();

            String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : "carta.pdf";
            String nombreArchivo = "CartaLaboral_" + id + "_" + original;
            File archivoDestino = new File(directorio, nombreArchivo);
            file.transferTo(archivoDestino);

            String rutaRelativa = "cartaLaboral/" + nombreArchivo;
            formato.setCartaLaboral(rutaRelativa);
            formatoARepository.save(formato);

            // Actualizar la última versión asociada
            versionService.actualizarRutasArchivos(formato);

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
        FormatoA formatoA = formatoARepository.findById(request.id())
                .orElseThrow(() -> new RuntimeException("FormatoA no encontrado: " + request.id()));

        System.out.println("🎭 STATE PATTERN para reenvío");
        System.out.println("🔍 Estado actual: " + formatoA.getState() + ", Counter: " + formatoA.getCounter());

        // Actualizar campos editables primero
        formatoA.setArchivoPDF(request.archivoPDF());
        formatoA.setCartaLaboral(request.cartaLaboral());
        formatoA.setGeneralObjetive(request.generalObjetive());
        formatoA.setSpecificObjetives(request.specificObjetives());

        // ✅ USAR STATE PATTERN COMPLETO para el reenvío
        try {
            formatoA.reenviar();
            System.out.println("✅ State Pattern ejecutado exitosamente");
        } catch (Exception e) {
            System.err.println("❌ Error en State Pattern: " + e.getMessage());
            throw new RuntimeException("No se puede reenviar el formato: " + e.getMessage());
        }

        FormatoA actualizado = formatoARepository.save(formatoA);

        // Mantener lógica de versiones y eventos
        FormatoAVersion nuevaVersion = versionService.crearVersionReenviada(actualizado, request);

        FormatoAResponse response = convertirAFormatoAResponse(actualizado);
        rabbitMQPublisher.publicarFormatoACreado(response);

        FormatoAnotification notificacion = convertirAFormatoANotificacionEvent(actualizado);
        rabbitMQPublisher.publicarNotificacionFormatoACreado(notificacion);

        return actualizado;
    }

    @Transactional
    public FormatoA actualizarFormatoAEvaluado(FormatoARequest request) {
        FormatoA formatoA = formatoARepository.findById(request.id())
                .orElseThrow(() -> new RuntimeException("FormatoA no encontrado: " + request.id()));

        System.out.println("🎭 STATE PATTERN HÍBRIDO");
        System.out.println("🔍 Estado actual: " + formatoA.getState() +
                ", Estado solicitado: " + request.state() +
                ", Counter: " + request.counter());

        try {
            String estadoSolicitado = request.state();
            int counterSolicitado = Integer.parseInt(request.counter());

            // ✅ ESTRATEGIA HÍBRIDA:
            switch (EnumEstado.valueOf(estadoSolicitado)) {
                case APROBADO:
                    // ✅ USAR STATE PATTERN (seguro)
                    System.out.println("✅ Usando State Pattern para APROBADO");
                    formatoA.aprobar();
                    break;

                case RECHAZADO:
                    // ✅ LÓGICA HÍBRIDA para RECHAZADO
                    System.out.println("🎭 Lógica híbrida para RECHAZADO");

                    // 1. Aplicar cambios básicos
                    formatoA.setObservations(request.observations());
                    formatoA.setCounter(counterSolicitado);

                    // 2. Decidir estado final
                    if (counterSolicitado >= 3) {
                        System.out.println("🚨 Counter >= 3 - RECHAZADO_DEFINITIVAMENTE");
                        formatoA.setState(EnumEstado.RECHAZADO_DEFINITIVAMENTE);
                    } else {
                        System.out.println("✅ Counter < 3 - RECHAZADO normal");
                        formatoA.setState(EnumEstado.RECHAZADO);
                    }
                    break;

                case ENTREGADO:
                    // ✅ LÓGICA ORIGINAL para ENTREGADO
                    System.out.println("🔧 Lógica original para ENTREGADO");
                    formatoA.setState(EnumEstado.ENTREGADO);
                    formatoA.setObservations(request.observations());
                    formatoA.setCounter(counterSolicitado);
                    break;

                default:
                    throw new RuntimeException("Estado no soportado: " + estadoSolicitado);
            }

        } catch (Exception e) {
            System.err.println("❌ Error en lógica híbrida: " + e.getMessage());
            // ✅ FALLBACK SEGURO
            System.out.println("🔄 Fallback a lógica original");
            formatoA.setState(EnumEstado.valueOf(request.state()));
            formatoA.setObservations(request.observations());
            formatoA.setCounter(Integer.parseInt(request.counter()));
        }

        FormatoA formatoAActualizado = formatoARepository.save(formatoA);

        System.out.println("🎯 RESULTADO FINAL - Estado: " + formatoAActualizado.getState() +
                ", Counter: " + formatoAActualizado.getCounter());

        // Mantener lógica de versiones
        FormatoAVersion nuevaVersion = versionService.crearVersionConEvaluacion(formatoAActualizado, request);
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
    public List<FormatoA> findAll() {
        return formatoARepository.findAll();
    }
}
