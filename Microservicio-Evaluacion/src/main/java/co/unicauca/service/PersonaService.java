package co.unicauca.service;

import co.unicauca.entity.*;
import co.unicauca.infra.dto.PersonaRequest;
import co.unicauca.repository.PersonaRepository;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PersonaService {
    private final PersonaRepository personaRepository;

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }


    /**
     * Guarda o actualiza la información de una Persona en la base de datos.
     * Si la persona ya existe, se actualizan sus datos.
     */
    public Persona guardarPersona(PersonaRequest request) {
        Optional<Persona> existente = personaRepository.findById((long) request.id());

        Persona persona = existente.orElseGet(Persona::new);

        persona.setIdUsuario((long) request.id());
        persona.setName(request.name());
        persona.setLastname(request.lastname());
        persona.setEmail(request.email());
        persona.setDepartment(request.department());
        persona.setPrograma(request.programa());

        // Convertir los roles del request (Set<String>) a EnumSet<EnumRol>
        persona.setRoles(convertirRoles(request.roles()));

        return personaRepository.save(persona);
    }


    /**
     * Convierte una lista de strings en un EnumSet<EnumRol>.
     */
    private EnumSet<EnumRol> convertirRoles(Set<String> rolesString) {
        if (rolesString == null || rolesString.isEmpty()) {
            return EnumSet.noneOf(EnumRol.class);
        }

        EnumSet<EnumRol> roles = EnumSet.noneOf(EnumRol.class);
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
     * Obtiene todos las personas  registradas.
     */
    public List<Persona> findAll() {
        return personaRepository.findAll();
    }

    public Persona findPersonaByEmail(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }

        Optional<Persona> personaOpt = personaRepository.findByEmail(email);
        return personaOpt.orElse(null);
    }
}
