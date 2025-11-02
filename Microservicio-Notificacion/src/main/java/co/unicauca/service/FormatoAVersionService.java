package co.unicauca.service;

import co.unicauca.entity.EnumRol;
import co.unicauca.entity.Persona;
import co.unicauca.infra.dto.DtoFormatoVersion;
import co.unicauca.repository.PersonaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;



@Service
public class FormatoAVersionService {

    private static final Logger log = LoggerFactory.getLogger(FormatoAVersionService.class);

    private final PersonaRepository personaRepository;

    public FormatoAVersionService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    /**
     * Procesa la notificación de una versión de Formato A
     * y envía un correo al coordinador del programa del estudiante.
     */
    public void procesarNotificacionVersion(DtoFormatoVersion version) {

        // ✅ Si el estado es APROBADO o RECHAZADO, no enviar notificación
        if (version.estado().equalsIgnoreCase("APROBADO") ||
                version.estado().equalsIgnoreCase("RECHAZADO")) {
            return;
        }
        if (version.estudiantesEmails() == null || version.estudiantesEmails().isEmpty()) {
            return;
        }

        // ✅ Tomamos el primer estudiante para determinar el programa
        String correoEstudiante = version.estudiantesEmails().get(0);

        Persona estudiante = personaRepository.findByEmail(correoEstudiante).orElse(null);

        if (estudiante == null) {
            log.warn("⚠️ No se encontró estudiante con correo: {}", correoEstudiante);
            return;
        }

        String programa = estudiante.getPrograma();
        log.info("🎓 Estudiante '{}' pertenece al programa '{}'", estudiante.getName(), programa);

        // ✅ Buscar coordinador de ese programa
        Persona coordinador = personaRepository
                .findByProgramaAndRolesContaining(programa, EnumRol.COORDINADOR)
                .orElse(null);

        if (coordinador == null) {
            log.warn("⚠️ No existe coordinador para el programa '{}'", programa);
            return;
        }

        // ✅ Simular envío de correo al coordinador
        log.info("📩 [EMAIL SIMULADO - Coordinador]");
        log.info("De: sistema@universidad.edu.co");
        log.info("Para: {}", coordinador.getEmail());
        log.info("Asunto: Nueva versión del Formato A registrada");
        log.info(
                "Body: Estimado/a {}, se ha registrado una nueva versión (#{} ) del Formato A {}. "
                        + "Estado: {}. Por favor revise los detalles.",
                coordinador.getName(),
                version.numeroVersion(),
                version.formatoAId(),
                version.estado()
        );

        log.info("✅ Notificación enviada al coordinador del programa '{}'", programa);
    }}