package co.unicauca.service;

import co.unicauca.entity.EnumRol;
import co.unicauca.entity.FormatoA;
import co.unicauca.entity.Persona;
import co.unicauca.infra.dto.PersonaRequest;
import co.unicauca.repository.PersonaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PersonaServiceTest {

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private FormatoAService formatoAService;

    @InjectMocks
    private PersonaService personaService;

    private PersonaRequest request;

    @BeforeEach
    void setup() {
        Set<String> roles = Set.of("DOCENTE", "JEFE_DEPARTAMENTO");

        request = new PersonaRequest(
                1L,
                "Nicolas",
                "Montaño",
                "nico@unicauca.edu.co",
                roles,
                "Sistemas",
                "Ingeniería"
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
        existente.setName("ViejoNombre");

        when(personaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(personaRepository.save(any(Persona.class))).thenAnswer(inv -> inv.getArguments()[0]);

        Persona result = personaService.guardarPersona(request);

        assertEquals("Nicolas", result.getName());
        verify(personaRepository).findById(1L);
        verify(personaRepository).save(any(Persona.class));
    }

    @Test
    void testFindAll() {
        when(personaRepository.findAll()).thenReturn(List.of(new Persona(), new Persona()));

        List<Persona> result = personaService.findAll();

        assertEquals(2, result.size());
        verify(personaRepository).findAll();
    }

    @Test
    void testFindPersonaByEmail() {
        Persona persona = new Persona();
        persona.setEmail("test@unicauca.edu.co");

        when(personaRepository.findByEmail("test@unicauca.edu.co")).thenReturn(Optional.of(persona));

        Persona result = personaService.findPersonaByEmail("test@unicauca.edu.co");

        assertNotNull(result);
        assertEquals("test@unicauca.edu.co", result.getEmail());
        verify(personaRepository).findByEmail("test@unicauca.edu.co");
    }
    @Test
    void testListarDocentesDisponiblesParaEvaluar() {
        Persona director = new Persona();
        director.setIdUsuario(1L);
        director.setName("Dir");
        director.setLastname("A");
        director.setEmail("dir@uni.edu");
        director.setDepartment("Sistemas");
        director.setRoles(EnumSet.of(EnumRol.DOCENTE));

        Persona codirector = new Persona();
        codirector.setIdUsuario(2L);
        codirector.setName("Co");
        codirector.setLastname("B");
        codirector.setEmail("co@uni.edu");
        codirector.setDepartment("Sistemas");
        codirector.setRoles(EnumSet.of(EnumRol.DOCENTE));

        FormatoA formato = new FormatoA();
        formato.setId(10L);
        formato.setProjectManager(director);
        formato.setProjectCoManager(codirector);

        when(formatoAService.findById(10L)).thenReturn(formato);
        when(personaRepository.findByEmail("dir@uni.edu")).thenReturn(Optional.of(director));
        when(personaRepository.findByEmail("co@uni.edu")).thenReturn(Optional.of(codirector));

        Persona docente1 = new Persona();
        docente1.setIdUsuario(3L);
        docente1.setEmail("doc1@uni.edu");
        docente1.setDepartment("Sistemas");
        docente1.setRoles(EnumSet.of(EnumRol.DOCENTE));

        Persona docente2 = new Persona();
        docente2.setIdUsuario(4L);
        docente2.setEmail("doc2@uni.edu");
        docente2.setDepartment("Sistemas");
        docente2.setRoles(EnumSet.of(EnumRol.DOCENTE));

        when(personaRepository.findByDepartmentIgnoreCase("Sistemas"))
                .thenReturn(List.of(director, codirector, docente1, docente2));

        List<Persona> result = personaService.listarDocentesDisponiblesParaEvaluar(10L);

        assertEquals(2, result.size());
        assertTrue(result.contains(docente1));
        assertTrue(result.contains(docente2));
    }

}