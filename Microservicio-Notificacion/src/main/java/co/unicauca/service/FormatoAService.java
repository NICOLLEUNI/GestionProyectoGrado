package co.unicauca.service;

import co.unicauca.entity.FormatoA;
import co.unicauca.infra.dto.FormatoACreado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormatoAService {

    private static final Logger log = LoggerFactory.getLogger(FormatoAService.class);

    public void procesarNotificacionEvaluado(FormatoA formatoA) {

        log.info("🚀 Procesando notificaciones para el anteproyecto: {}", formatoA.getTitulo());



        // === Notificar a cada docente ===
        List<String> correosDocentes = formatoA.getCorreosDocentes();
        if (correosDocentes != null && !correosDocentes.isEmpty()) {
            for (String correo : correosDocentes) {
                log.info("📩 [EMAIL SIMULADO - Docente]");
                log.info("De: sistema@universidad.edu.co");
                log.info("Para: {}", correo);
                log.info("Asunto: Formato A evaluado - {}", formatoA.getTitulo());
                log.info("Body: Estimado/a docente, se ha evaluado el formatoA  '{}'.", formatoA.getTitulo());
            }
        }

        // === Notificar a cada estudiante ===
        List<String> correosEstudiantes = formatoA.getCorreosEstudiantes();
        if (correosEstudiantes != null && !correosEstudiantes.isEmpty()) {
            for (String correo : correosEstudiantes) {
                log.info("📩 [EMAIL SIMULADO - Estudiante]");
                log.info("De: sistema@universidad.edu.co");
                log.info("Para: {}", correo);
                log.info("Asunto: Confirmación evaluacion de formatoA - {}", formatoA.getTitulo());
                log.info("Body: Estimado/a estudiante, su formatoA '{}' ha sido evaluado exitosamente.",
                        formatoA.getTitulo());
            }
        }

        log.info("✅ Todas las notificaciones individuales fueron enviadas correctamente para '{}'.",
                formatoA.getTitulo());
    }

    public void procesarNotificacionCreado(FormatoACreado evento) {
        log.info("🆕 Procesando notificación de creación para el Formato A: {}", evento.titulo());

        // === Notificar al director ===
        if (evento.directorEmail() != null && !evento.directorEmail().isBlank()) {
            log.info("📩 [EMAIL SIMULADO - Director]");
            log.info("De: sistema@universidad.edu.co");
            log.info("Para: {}", evento.directorEmail());
            log.info("Asunto: Nuevo Formato A creado - {}", evento.titulo());
            log.info("Body: Estimado/a director/a, se ha registrado un nuevo Formato A titulado '{}'. "
                    + "Por favor revise los detalles en el sistema.", evento.titulo());
        } else {
            log.warn("⚠️ No se pudo enviar notificación al director: correo no disponible.");
        }

        // === Notificar a los estudiantes involucrados ===
        List<String> correosEstudiantes = evento.estudiantesEmails();
        if (correosEstudiantes != null && !correosEstudiantes.isEmpty()) {
            for (String correo : correosEstudiantes) {
                log.info("📩 [EMAIL SIMULADO - Estudiante]");
                log.info("De: sistema@universidad.edu.co");
                log.info("Para: {}", correo);
                log.info("Asunto: Confirmación de creación de Formato A - {}", evento.titulo());
                log.info("Body: Estimado/a estudiante, su Formato A titulado '{}' ha sido creado exitosamente "
                        + "y se encuentra pendiente de evaluación.", evento.titulo());
            }
        } else {
            log.warn("⚠️ No se encontraron correos de estudiantes para el Formato A '{}'.", evento.titulo());
        }

        log.info("✅ Notificaciones de creación procesadas correctamente para '{}'.", evento.titulo());
    }
}
