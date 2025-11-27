package co.unicauca.service;

import co.unicauca.entity.Anteproyecto;
import co.unicauca.entity.EnumRol;
import co.unicauca.entity.Persona;
import co.unicauca.infra.dto.AnteproyectoCreado;
import co.unicauca.repository.PersonaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnteproyectoService {
    private static final Logger log = LoggerFactory.getLogger(AnteproyectoService.class);

    private final PersonaRepository personaRepository;

    public AnteproyectoService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    /**
     * Procesa la notificación cuando se crea un nuevo anteproyecto.
     * 1. Busca el docente director por su correo.
     * 2. Obtiene su departamento.
     * 3. Busca al jefe de ese departamento.
     * 4. Envía notificación solo al jefe.
     */
    public void procesarNotificacionCreado(AnteproyectoCreado evento) {
        if (evento == null || evento.directorEmail() == null) {
            return;
        }
        log.info("📬 Procesando notificación de creación de anteproyecto: {}", evento.titulo());

        // 1️⃣ Buscar docente director por su correo
        Optional<Persona> docenteOpt = personaRepository.findByEmail(evento.directorEmail());

        if (docenteOpt.isEmpty()) {
            log.warn("⚠️ No se encontró docente con el email: {}", evento.directorEmail());
            return;
        }

        Persona docente = docenteOpt.get();
        String departamento = docente.getDepartment();

        log.info("👨‍🏫 Docente encontrado: {} {}, departamento: {}",
                docente.getName(), docente.getLastname(), departamento);

        // 2️⃣ Buscar jefe de departamento correspondiente
        List<Persona> jefes = personaRepository.findByDepartmentAndRolesContaining(
                departamento, EnumRol.JEFE_DEPARTAMENTO
        );

        if (jefes.isEmpty()) {
            log.warn("⚠️ No se encontró jefe de departamento para el departamento '{}'", departamento);
            return;
        }

        // 3️⃣ Enviar notificación al jefe de departamento
        for (Persona jefe : jefes) {
            log.info("📩 [EMAIL SIMULADO - Jefe de Departamento]");
            log.info("De: sistema@universidad.edu.co");
            log.info("Para: {}", jefe.getEmail());
            log.info("Asunto: Nuevo anteproyecto registrado en su departamento");
            log.info("Body: Estimado/a {}, se ha creado un nuevo anteproyecto titulado '{}', dirigido por el docente {} {} del departamento de {}.",
                    jefe.getName(), evento.titulo(), docente.getName(), docente.getLastname(), departamento);
        }

        log.info("✅ Notificación enviada correctamente al jefe del departamento '{}'.", departamento);
    }

    public void procesarNotificacionAsignado(Anteproyecto anteproyecto) {

        if (anteproyecto == null) {
            log.warn("⚠️ Anteproyecto recibido es NULL");
            return;
        }

        log.info("📬 Procesando notificación de asignación de evaluadores al anteproyecto: {}",
                anteproyecto.getTitulo());

        // Correos recibidos en la cola
        String email1 = anteproyecto.getEmailEvaluador1();
        String email2 = anteproyecto.getEmailEvaluador2();

        if (email1 == null && email2 == null) {
            log.warn("⚠️ El anteproyecto no tiene correos de evaluadores.");
            return;
        }

        // 📨 Notificar evaluador 1
        if (email1 != null) {
            Optional<Persona> eval1 = personaRepository.findByEmail(email1.trim());

            if (eval1.isPresent()) {
                Persona p = eval1.get();
                log.info("📩 [EMAIL SIMULADO - Evaluador Asignado]");
                log.info("De: sistema@universidad.edu.co");
                log.info("Para: {}", p.getEmail());
                log.info("Asunto: Asignación como evaluador de anteproyecto");
                log.info("Body: Estimado/a {}, usted ha sido asignado como evaluador del anteproyecto '{}'.",
                        p.getName(), anteproyecto.getTitulo());
            } else {
                log.warn("⚠️ No se encontró evaluador con email: {}", email1);
            }
        }

        // 📨 Notificar evaluador 2
        if (email2 != null) {
            Optional<Persona> eval2 = personaRepository.findByEmail(email2.trim());

            if (eval2.isPresent()) {
                Persona p = eval2.get();
                log.info("📩 [EMAIL SIMULADO - Evaluador Asignado]");
                log.info("De: sistema@universidad.edu.co");
                log.info("Para: {}", p.getEmail());
                log.info("Asunto: Asignación como evaluador de anteproyecto");
                log.info("Body: Estimado/a {}, usted ha sido asignado como evaluador del anteproyecto '{}'.",
                        p.getName(), anteproyecto.getTitulo());
            } else {
                log.warn("⚠️ No se encontró evaluador con email: {}", email2);
            }
        }

        log.info("✅ Notificaciones enviadas a los evaluadores del anteproyecto '{}'.",
                anteproyecto.getTitulo());
    }
}
