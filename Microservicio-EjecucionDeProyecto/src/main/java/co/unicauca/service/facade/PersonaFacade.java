package co.unicauca.service.facade;

import co.unicauca.entity.Persona;
import co.unicauca.infra.dto.PersonaRequest;
import co.unicauca.infra.dto.PersonaResponse;
import co.unicauca.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonaFacade {

    private final PersonaService personaService;

    @Autowired
    public PersonaFacade(PersonaService personaService) {
        this.personaService = personaService;
    }

    /**
     * 🔹 Crear una nueva persona a partir del request.
     */
    public PersonaResponse crearPersona(PersonaRequest request) {
        Persona persona = personaService.guardarPersona(request);
        return mapToResponse(persona);
    }

    /**
     * 🔹 Listar todas las personas.
     */
    public List<PersonaResponse> listarTodas() {
        return personaService.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }



    private PersonaResponse mapToResponse(Persona persona) {
        return new PersonaResponse(
                persona.getIdUsuario(),                // Long → Long
                persona.getName(),
                persona.getLastname(),
                persona.getEmail(),
                persona.getRoles().stream()            // EnumRol → String
                        .map(Enum::name)
                        .collect(Collectors.toSet()),
                persona.getDepartment(),
                persona.getPrograma()
        );
    }

}
