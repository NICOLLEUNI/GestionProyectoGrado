package co.unicauca.service;

import co.unicauca.entity.Persona;
import co.unicauca.infra.dto.PersonaRequest;
import co.unicauca.repository.PersonaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
    void guardarPersona_conId_creaYPersiste() {
        PersonaRequest request = new PersonaRequest(
                1L, "Juan", "Pérez", "juan@example.com",
                Set.of("ESTUDIANTE"), "Ingeniería", "Software"
        );

        when(personaRepository.findById(1L)).thenReturn(Optional.empty());
        when(personaRepository.save(any(Persona.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Persona personaGuardada = personaService.guardarPersona(request);

        assertNotNull(personaGuardada);
        assertEquals(1L, personaGuardada.getIdUsuario());
        assertEquals("Juan", personaGuardada.getName());
        assertEquals("Pérez", personaGuardada.getLastname());
        assertEquals("juan@example.com", personaGuardada.getEmail());
        assertEquals("Ingeniería", personaGuardada.getDepartment());
        assertEquals("Software", personaGuardada.getPrograma());
        assertTrue(personaGuardada.getRoles().containsAll(Set.of(co.unicauca.entity.EnumRol.ESTUDIANTE)));

        System.out.println("✅ Test 'guardarPersona_conId_creaYPersiste' completado correctamente");
    }

    @Test
    void findAll_devuelveLista() {
        Persona persona1 = new Persona();
        persona1.setIdUsuario(1L);
        Persona persona2 = new Persona();
        persona2.setIdUsuario(2L);

        when(personaRepository.findAll()).thenReturn(List.of(persona1, persona2));

        List<Persona> personas = personaService.findAll();

        assertEquals(2, personas.size());
        System.out.println("✅ Test 'findAll_devuelveLista' completado correctamente");
    }

    @Test
    void findByEmail_encuentraPersona() {
        Persona persona = new Persona();
        persona.setIdUsuario(1L);
        persona.setEmail("juan@example.com");

        when(personaRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(persona));

        Optional<Persona> resultado = personaService.findByEmail("juan@example.com");

        assertTrue(resultado.isPresent());
        assertEquals("juan@example.com", resultado.get().getEmail());
        System.out.println("✅ Test 'findByEmail_encuentraPersona' completado correctamente");
    }
}
