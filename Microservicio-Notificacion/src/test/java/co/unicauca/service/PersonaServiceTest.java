package co.unicauca.service;

import co.unicauca.entity.EnumRol;
import co.unicauca.entity.Persona;
import co.unicauca.infra.dto.PersonaRequest;
import co.unicauca.repository.PersonaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonaServiceTest {

    @Mock
    private PersonaRepository personaRepository;

    @InjectMocks
    private PersonaService personaService;

    private PersonaRequest request;

    @BeforeEach
    void setup() {
        Set<String> roles = new HashSet<>();
        roles.add("DOCENTE");
        roles.add("JEFE_DEPARTAMENTO");

        request = new PersonaRequest(
                1L,
                "Nicolas",
                "Montaño",
                "nico@unicauca.edu.co",
                roles,
                "Ingeniería",
                "Sistemas"
        );
    }
    @Test
    void testGuardarPersona_NuevaPersona() {
        when(personaRepository.findById(1L)).thenReturn(Optional.empty());
        when(personaRepository.save(any(Persona.class))).thenAnswer(inv -> inv.getArguments()[0]);

        Persona result = personaService.guardarPersona(request);

        assertNotNull(result);
        assertEquals(1L, result.getIdUsuario());
        assertEquals("Nicolas", result.getName());
        assertTrue(result.getRoles().contains(EnumRol.DOCENTE));
        assertTrue(result.getRoles().contains(EnumRol.JEFE_DEPARTAMENTO));

        verify(personaRepository).findById(1L);
        verify(personaRepository).save(any(Persona.class));
    }


    @Test
    void testGuardarPersona_ActualizarPersonaExistente() {
        Persona existente = new Persona();
        existente.setIdUsuario(1L);
        existente.setName("Antigua");

        when(personaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(personaRepository.save(any(Persona.class))).thenAnswer(inv -> inv.getArguments()[0]);

        Persona result = personaService.guardarPersona(request);

        assertEquals("Nicolas", result.getName());
        assertTrue(result.getRoles().contains(EnumRol.DOCENTE));
        assertTrue(result.getRoles().contains(EnumRol.JEFE_DEPARTAMENTO));

        verify(personaRepository).findById(1L);
        verify(personaRepository).save(any(Persona.class));
    }

    @Test
    void testFindAll() {
        List<Persona> lista = Arrays.asList(new Persona(), new Persona());

        when(personaRepository.findAll()).thenReturn(lista);

        List<Persona> result = personaService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(personaRepository).findAll();
    }
}