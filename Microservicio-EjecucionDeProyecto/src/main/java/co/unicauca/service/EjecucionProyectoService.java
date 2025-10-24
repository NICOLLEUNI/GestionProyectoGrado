package co.unicauca.service;

import co.unicauca.dto.SubmissionRequest;
import co.unicauca.entity.*;
import co.unicauca.mapper.*;
import co.unicauca.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EjecucionProyectoService {

    private final PersonaRepository personaRepository;
    private final ProyectoGradoRepository proyectoRepository;
    private final FormatoARepository formatoARepository;
    private final FormatoAVersionRepository formatoAVersionRepository;
    private final AnteproyectoRepository anteproyectoRepository;

    @Transactional
    public void procesarSubmission(SubmissionRequest request) {
        log.info("🚀 Iniciando procesamiento de Submission...");

        try {
            // 1️⃣ Guardar personas
            if (request.getPersonas() != null) {
                request.getPersonas().forEach(dto -> {
                    PersonaEntity persona = PersonaMapper.fromRequest(dto);
                    personaRepository.save(persona);
                });
            }

            // 2️⃣ Guardar proyecto de grado
            ProyectoGradoEntity proyecto = null;
            if (request.getProyectoGrado() != null) {
                proyecto = ProyectoGradoMapper.fromRequest(request.getProyectoGrado());
                proyectoRepository.save(proyecto);
            }

            // 3️⃣ Guardar Formato A vinculado al proyecto
            FormatoAEntity formatoA;
            if (request.getFormatoA() != null && proyecto != null) {
                formatoA = FormatoAMapper.fromRequest(request.getFormatoA(), proyecto);
                formatoARepository.save(formatoA);
            } else {
                formatoA = null;
            }

            // 4️⃣ Guardar versiones del Formato A
            if (request.getFormatoAVersiones() != null && formatoA != null) {
                request.getFormatoAVersiones().forEach(dto -> {
                    FormatoAVersionEntity version = FormatoAVersionMapper.fromRequest(dto, formatoA);
                    formatoAVersionRepository.save(version);
                });
            }

            // 5️⃣ Guardar anteproyecto vinculado al proyecto
            if (request.getAnteproyecto() != null && proyecto != null) {
                AnteproyectoEntity anteproyecto = AnteproyectoMapper.fromRequest(request.getAnteproyecto(), proyecto);
                anteproyectoRepository.save(anteproyecto);
            }

            log.info("✅ Submission procesado y almacenado correctamente.");

        } catch (Exception e) {
            log.error("❌ Error al procesar Submission: {}", e.getMessage(), e);
            throw e; // rollback automático
        }
    }
}
