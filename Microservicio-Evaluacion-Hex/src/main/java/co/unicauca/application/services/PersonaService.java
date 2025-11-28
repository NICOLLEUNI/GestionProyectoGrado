// application/services/PersonaService.java
package co.unicauca.application.services;

import co.unicauca.application.ports.input.PersonaInPort;
import co.unicauca.application.ports.output.PersonaRepoOutPort;
import co.unicauca.domain.entities.FormatoA;
import co.unicauca.domain.entities.Persona;
import co.unicauca.domain.entities.EnumRol;
import co.unicauca.infrastructure.dto.request.PersonaRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PersonaService implements PersonaInPort {
    private final PersonaRepoOutPort personaRepoOutPort;
    private final FormatoAService formatoAService;

    public PersonaService(PersonaRepoOutPort personaRepoOutPort, FormatoAService formatoAService) {
        this.personaRepoOutPort = personaRepoOutPort;
        this.formatoAService = formatoAService;
    }

    /**
     * Guarda o actualiza la información de una Persona en la base de datos.
     * Si la persona ya existe, se actualizan sus datos.
     */
    @Override
    public Persona guardarPersona(PersonaRequest request) {
        Optional<Persona> existente = personaRepoOutPort.findById((long) request.id());

        Persona persona;

        if (existente.isPresent()) {
            // ACTUALIZAR: Usar el método de actualización controlado
            persona = existente.get();
            persona.actualizarDatos(
                    request.name(),
                    request.lastname(),
                    request.email(),
                    request.department(),
                    request.programa(),
                    convertirRoles(request.roles())
            );
        } else {
            // CREAR: Nueva instancia con validaciones
            persona = new Persona(
                    (long) request.id(),
                    request.name(),
                    request.lastname(),
                    request.email(),
                    request.department(),
                    request.programa(),
                    convertirRoles(request.roles())
            );
        }

        return personaRepoOutPort.save(persona);
    }

    /**
     * Convierte una lista de strings en un Set<EnumRol>.
     */
    private Set<EnumRol> convertirRoles(Set<String> rolesString) {
        if (rolesString == null || rolesString.isEmpty()) {
            return Collections.emptySet();
        }

        Set<EnumRol> roles = new HashSet<>();
        for (String rolStr : rolesString) {
            try {
                roles.add(EnumRol.valueOf(rolStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.err.println("⚠️ Rol no reconocido: " + rolStr);
            }
        }
        return roles;
    }

    /**
     * Obtiene todos las personas registradas.
     */
    public List<Persona> findAll() {
        return personaRepoOutPort.findAll();
    }

    public Optional<Persona> findPersonaByEmail(String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }
        return personaRepoOutPort.findByEmail(email);
    }

    public List<Persona> listarDocentesDisponiblesParaEvaluar(Long idFormatoA) {
        // 1️⃣ Obtener el FormatoA
        Optional<FormatoA> formatoOpt = formatoAService.findById(idFormatoA);
        if (formatoOpt.isEmpty()) {
            return List.of(); // retornar lista vacía si no existe el formato
        }

        FormatoA formato = formatoOpt.get();

        // 2️⃣ Obtener director y codirector
        Optional<Persona> directorOpt = formato.getProjectManager() != null ?
                findPersonaByEmail(formato.getProjectManager().getEmail()) : Optional.empty();
        Optional<Persona> codirectorOpt = formato.getProjectCoManager() != null ?
                findPersonaByEmail(formato.getProjectCoManager().getEmail()) : Optional.empty();

        String departmentDirector = directorOpt.map(Persona::getDepartment).orElse(null);
        String emailDirector = directorOpt.map(Persona::getEmail).orElse(null);
        String emailCodirector = codirectorOpt.map(Persona::getEmail).orElse(null);

        if (departmentDirector == null || departmentDirector.isBlank()) {
            return List.of(); // si el director no tiene departamento, no hay docentes a listar
        }

        // 3️⃣ Listar todos los docentes del mismo departamento
        List<Persona> docentes = personaRepoOutPort.findByDepartmentIgnoreCase(departmentDirector).stream()
                .filter(p -> p.tieneRol(EnumRol.DOCENTE)) // solo docentes
                .filter(p -> emailDirector == null || !p.getEmail().equalsIgnoreCase(emailDirector)) // excluir director
                .filter(p -> emailCodirector == null || !p.getEmail().equalsIgnoreCase(emailCodirector)) // excluir codirector
                .collect(Collectors.toList());

        return docentes;
    }

}