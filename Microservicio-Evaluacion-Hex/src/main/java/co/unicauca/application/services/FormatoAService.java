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
import java.util.stream.Collectors;

@Service
public class FormatoAService  {

    private FormatoARepoOutPort formatoARepo;
    private PersonaRepoOutPort personaRepo;

    public FormatoAService(FormatoARepoOutPort formatoARepo, PersonaRepoOutPort personaRepo) {
        this.formatoARepo = formatoARepo;
        this.personaRepo = personaRepo;
    }


    public FormatoA guardarFormatoA(FormatoARequest request) {

        FormatoA formatoA;

        if (request.id() != null) {
            formatoA = formatoARepo.findById(request.id())
                    .orElseGet(FormatoA::new);
        } else {
            formatoA = new FormatoA();
        }

        // Campos simples
        formatoA.asignarId(request.id());
        formatoA.actualizarTituloD(request.title());
        formatoA.actualizarModo(request.mode());
        formatoA.actualizarGeneralObjective(request.generalObjetive());
        formatoA.actualizarSpecificObjectives(request.specificObjetives());
        formatoA.actualizarArchivoPDF(request.archivoPDF());
        formatoA.actualizarCartaLaboral(request.cartaLaboral());
        formatoA.actualizarCounter(request.counter());

        // Director y codirector
        System.out.println("🔍 Buscando director con email: " + request.projectManagerEmail());
        Persona director = personaRepo.findByEmail(request.projectManagerEmail()).orElse(null);
        System.out.println("👤 Director encontrado: " + (director != null));
        if (director != null) {
            System.out.println("✅ Director ID: " + director.getIdUsuario());
            System.out.println("✅ Director Email: " + director.getEmail());
            System.out.println("✅ Director Roles: " + director.getRoles());
            System.out.println("✅ Es DOCENTE? " + director.tieneRol(EnumRol.DOCENTE));
        }

        System.out.println("🔍 Buscando codirector con email: " + request.projectCoManagerEmail());
        Persona codirector = personaRepo.findByEmail(request.projectCoManagerEmail()).orElse(null);
        System.out.println("👤 Codirector encontrado: " + (codirector != null));
        if (codirector != null) {
            System.out.println("✅ Codirector ID: " + codirector.getIdUsuario());
            System.out.println("✅ Codirector Email: " + codirector.getEmail());
            System.out.println("✅ Codirector Roles: " + codirector.getRoles());
            System.out.println("✅ Es DOCENTE? " + codirector.tieneRol(EnumRol.DOCENTE));
        }

        System.out.println("🔄 ASIGNANDO DIRECTOR...");
        formatoA.asignarManagerD(director);
        System.out.println("🔄 ASIGNANDO CODIRECTOR...");
        formatoA.asignarCoManagerD(codirector);

        // Estudiantes
        if (request.estudiante() != null) {
           List<Persona> estudiantes = request.estudiante() .stream()
                   .map(email -> personaRepo.findByEmail(email).orElse(null))
                   .filter(e -> e != null)
                   .collect(Collectors.toList());
           formatoA.asignarEstudiantesD(estudiantes);

        }

        formatoA.validarYAsignarEstadoInicialD(EnumEstado.ENTREGADO);

        formatoARepo.save(formatoA);

        return formatoA;
     }



    public Optional<FormatoA> actualizarEstado(Long id, EnumEstado newState, String observations) {
        Optional<FormatoA> opt = formatoARepo.findById(id);
        if (opt.isEmpty()) return Optional.empty();

        FormatoA formato = opt.get();
        formato.asignarEstado(newState);
        formato.asignarObservaciones(observations);

        if(newState==EnumEstado.RECHAZADO){
            formato.actualizarCounter(formato.getCounter()+1);
        }
        formatoARepo.save(formato);

        return Optional.of(formato);

    }


    public List<FormatoA> listarTodos() {
        return formatoARepo.findAll();
    }


    public List<FormatoA> listarPorPrograma(String programa) {
       List<FormatoA>  todosFormatos = formatoARepo.findAll();
       //Filtrar solo los que tengan a menos un estudiante;
        return todosFormatos.stream()
                .filter(formato -> !formato.getEstudiantes().isEmpty())
                .filter(formato ->{
                    Persona primerEstudainte = formato.getEstudiantes().get(0);
                    return primerEstudainte.getPrograma() != null &&
                            primerEstudainte.getPrograma().equalsIgnoreCase(programa);
                })
                .collect(Collectors.toList());
    }


    public FormatoA findById(Long id) {
        return formatoARepo.findById(id).orElse(null);
    }


}
