package co.unicauca.application.services;

import co.unicauca.application.ports.output.FormatoARepoOutPort;
import co.unicauca.application.ports.output.PersonaRepoOutPort;
import co.unicauca.domain.entities.FormatoA;
import co.unicauca.domain.entities.*;
import co.unicauca.infrastructure.dto.request.FormatoARequest;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FormatoAService  {

    private FormatoARepoOutPort formatoARepo;
    private PersonaRepoOutPort personaRepo;

    public FormatoAService(FormatoARepoOutPort formatoARepo, PersonaRepoOutPort personaRepo) {
        this.formatoARepo = formatoARepo;
        this.personaRepo = personaRepo;
    }


    public FormatoA crearOActualizar(FormatoARequest req) {

        FormatoA formato = req.id() != null
                ? formatoARepo.findById(req.id()).orElse(null)
                : null;

        if (formato == null) {
            formato = new FormatoA(
                    req.id(),
                    req.title(),
                    req.mode(),
                    null,
                    null,
                    req.generalObjetive(),
                    req.specificObjetives(),
                    req.archivoPDF(),
                    req.cartaLaboral(),
                    req.counter(),
                    new ArrayList<>(),
                    EnumEstado.ENTREGADO,
                    null
            );
        }

        Persona manager = personaRepo.findByEmail(req.projectManagerEmail()).orElse(null);
        Persona coManager = personaRepo.findByEmail(req.projectCoManagerEmail()).orElse(null);

        if (manager != null) formato.asignarManager(manager);
        if (coManager != null) formato.asignarCoManager(coManager);

        if (req.estudiante() != null) {
            for (String email : req.estudiante()) {
                personaRepo.findByEmail(email)
                        .ifPresent(formato::addEstudiante);
            }
        }

        formato.validarYAsignarEstadoInicial(EnumEstado.ENTREGADO);

        return formatoARepo.save(formato);
    }


    public Optional<FormatoA> actualizarEstado(Long id, EnumEstado newState, String observations) {
        Optional<FormatoA> opt = formatoARepo.findById(id);
        if (opt.isEmpty()) return Optional.empty();

        FormatoA formato = opt.get();

        switch (newState) {
            case APROBADO -> formato.aprobar();
            case RECHAZADO -> formato.rechazar(observations);
            default -> throw new IllegalStateException("Estado no permitido");
        }

        return Optional.of(formatoARepo.save(formato));
    }


    public List<FormatoAResponse> listarTodos() {
        return formatoARepo.findAll();
    }


    public List<FormatoA> listarPorPrograma(String programa) {
        return formatoARepo.findAll().stream()
                .filter(f -> !f.getEstudiantes().isEmpty())
                .filter(f -> f.getEstudiantes().get(0).getPrograma().equalsIgnoreCase(programa))
                .toList();
    }

    // En FormatoAService - CORREGIDO
    public Optional<FormatoA> findById(Long id) {
        return formatoARepo.findById(id); // ← Ya retorna Optional, no uses orElse(null)
    }

}
