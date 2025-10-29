package co.unicauca.service;

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
}
