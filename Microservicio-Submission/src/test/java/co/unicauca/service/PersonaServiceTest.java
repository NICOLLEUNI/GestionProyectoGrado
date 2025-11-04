package co.unicauca.service;

import co.unicauca.entity.EnumRol;
import co.unicauca.entity.Persona;
import co.unicauca.infra.dto.PersonaRequest;
import co.unicauca.repository.PersonaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PersonaServiceTest {

    @Mock
    private PersonaRepository personaRepository;

    @InjectMocks
    private PersonaService personaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void guardarPersona_nuevaPersona() {
        PersonaRequest request = new PersonaRequest(
                1L, "Juan", "Pérez", "juan@example.com",
                Set.of("ESTUDIANTE"),
                "SISTEMAS",
                "INGENIERIA_DE_SISTEMAS"
        );

        when(personaRepository.findById(1L)).thenReturn(Optional.empty());
        when(personaRepository.save(any(Persona.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Persona resultado = personaService.guardarPersona(request);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getName());
        assertEquals("Pérez", resultado.getLastname());
        assertEquals("juan@example.com", resultado.getEmail());
        assertEquals("SISTEMAS", resultado.getDepartment());
        assertTrue(resultado.getRoles().contains(EnumRol.ESTUDIANTE));
        verify(personaRepository, times(1)).save(any(Persona.class));
    }

    @Test
    void guardarPersona_actualizaExistente() {
        Persona existente = new Persona();
        existente.setIdUsuario(1L);
        existente.setName("Pedro");

        PersonaRequest request = new PersonaRequest(
                1L, "Pedro", "Gómez", "pedro@example.com",
                Set.of("DOCENTE"),
                "ELECTRONICA",
                "INGENIERIA_ELECTRONICA_Y_TELECOMUNICACIONES"
        );

        when(personaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(personaRepository.save(any(Persona.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Persona resultado = personaService.guardarPersona(request);

        assertNotNull(resultado);
        assertEquals("Pedro", resultado.getName());
        assertEquals("Gómez", resultado.getLastname());
        assertEquals("pedro@example.com", resultado.getEmail());
        assertEquals("ELECTRONICA", resultado.getDepartment());
        assertTrue(resultado.getRoles().contains(EnumRol.DOCENTE));
        verify(personaRepository, times(1)).save(existente);
    }

    @Test
    void findAll_devuelveListaPersonas() {
        List<Persona> personas = Arrays.asList(new Persona(), new Persona());
        when(personaRepository.findAll()).thenReturn(personas);

        List<Persona> resultado = personaService.findAll();

        assertEquals(2, resultado.size());
        verify(personaRepository, times(1)).findAll();
    }

    @Test
    void findByRol_devuelvePersonasConRol() {
        Persona persona = new Persona();
        persona.setRoles(EnumSet.of(EnumRol.ESTUDIANTE));

        when(personaRepository.findByRolesContaining(EnumRol.ESTUDIANTE))
                .thenReturn(Collections.singletonList(persona));

        List<Persona> resultado = personaService.findByRol(EnumRol.ESTUDIANTE);

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getRoles().contains(EnumRol.ESTUDIANTE));
        verify(personaRepository, times(1)).findByRolesContaining(EnumRol.ESTUDIANTE);
    }

    @Test
    void findPersonaByEmail_emailValido() {
        Persona persona = new Persona();
        persona.setEmail("mail@example.com");
        when(personaRepository.findByEmail("mail@example.com")).thenReturn(Optional.of(persona));

        Persona resultado = personaService.findPersonaByEmail("mail@example.com");

        assertNotNull(resultado);
        assertEquals("mail@example.com", resultado.getEmail());
        verify(personaRepository, times(1)).findByEmail("mail@example.com");
    }

    @Test
    void findPersonaByEmail_emailInvalido() {
        Persona resultado = personaService.findPersonaByEmail("");
        assertNull(resultado);
        verify(personaRepository, times(0)).findByEmail(anyString());
    }

    @Test
    void findEstudiantesSinFormatoA_devuelveLista() {
        Persona estudiante = new Persona();
        estudiante.setRoles(EnumSet.of(EnumRol.ESTUDIANTE));

        when(personaRepository.findEstudiantesNoAsociados(EnumRol.ESTUDIANTE))
                .thenReturn(Collections.singletonList(estudiante));

        List<Persona> resultado = personaService.findEstudiantesSinFormatoA();

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getRoles().contains(EnumRol.ESTUDIANTE));
        verify(personaRepository, times(1)).findEstudiantesNoAsociados(EnumRol.ESTUDIANTE);
    }
}
