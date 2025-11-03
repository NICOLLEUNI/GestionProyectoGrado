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

    public Persona guardarPersona(PersonaRequest request) {
        Persona persona;

        // ✅ SIEMPRE usar el ID del request (no generado automáticamente)
        if (request.id() != null) {
            // Buscar por ID primero
            Optional<Persona> existente = personaRepository.findById(request.id());
            persona = existente.orElse(new Persona());
            // ✅ ASIGNAR MANUALMENTE el ID
            persona.setIdUsuario(request.id());
        } else {
            // Si no viene ID, crear nueva pero necesitamos un ID
            persona = new Persona();
            // ⚠️ NECESITAS PROVEER UN ID - usar algún generador o lanzar error
            throw new IllegalArgumentException("El ID es requerido para crear una Persona");
        }

        // Actualizar datos
        persona.setName(request.name());
        persona.setLastname(request.lastname());
        persona.setEmail(request.email());
        persona.setDepartment(request.department());
        persona.setPrograma(request.programa());
        persona.setRoles(convertirRoles(request.roles()));

        return personaRepository.save(persona);
    }

    private EnumSet<EnumRol> convertirRoles(Set<String> rolesString) {
        if (rolesString == null || rolesString.isEmpty()) {
            return EnumSet.noneOf(EnumRol.class);
        }

        EnumSet<EnumRol> roles = EnumSet.noneOf(EnumRol.class);
        for (String rolStr : rolesString) {
            try {
                roles.add(EnumRol.valueOf(rolStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.err.println("⚠ Rol no reconocido: " + rolStr);
            }
        }
        return roles;
    }

    public List<Persona> findAll() {
        return personaRepository.findAll();
    }

    public Optional<Persona> findByEmail(String email) {
        return personaRepository.findByEmail(email);
    }
}