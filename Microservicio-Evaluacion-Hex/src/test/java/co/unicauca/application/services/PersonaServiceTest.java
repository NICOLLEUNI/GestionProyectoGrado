// application/services/PersonaServiceTest.java
package co.unicauca.application.services;

import co.unicauca.application.ports.output.PersonaRepoOutPort;
import co.unicauca.domain.entities.EnumEstado;
import co.unicauca.domain.entities.EnumRol;
import co.unicauca.domain.entities.FormatoA;
import co.unicauca.domain.entities.Persona;
import co.unicauca.infrastructure.dto.request.PersonaRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonaServiceTest {

    @Mock
    private PersonaRepoOutPort personaRepoOutPort;

    @Mock
    private FormatoAService formatoAService;

    @InjectMocks
    private PersonaService personaService;

    private PersonaRequest personaRequestNueva;
    private PersonaRequest personaRequestExistente;
    private Persona personaExistente;
    private Set<String> rolesString;

    @BeforeEach
    void setUp() {
        rolesString = new HashSet<>(Arrays.asList("DOCENTE", "ESTUDIANTE"));

        personaRequestNueva = new PersonaRequest(
                1L,
                "Juan",
                "Pérez",
                "juan.perez@unicauca.edu.co",
                rolesString,
                "Ingeniería de Sistemas",
                "Ingeniería de Software"
        );

        personaRequestExistente = new PersonaRequest(
                1L,
                "Juan Actualizado",
                "Pérez Actualizado",
                "juan.actualizado@unicauca.edu.co",
                rolesString,
                "Ingeniería Civil",
                "Ingeniería Civil"
        );

        personaExistente = new Persona(
                1L,
                "Juan",
                "Pérez",
                "juan.perez@unicauca.edu.co",
                "Ingeniería de Sistemas",
                "Ingeniería de Software",
                new HashSet<>(Arrays.asList(EnumRol.DOCENTE))
        );
    }

    // 1. TESTS PARA guardarPersona()

    @Test
    void testGuardarPersona_NuevaPersona() {
        // Arrange
        when(personaRepoOutPort.findById(1L)).thenReturn(Optional.empty());
        when(personaRepoOutPort.save(any(Persona.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Persona resultado = personaService.guardarPersona(personaRequestNueva);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdUsuario());
        assertEquals("Juan", resultado.getName());
        assertEquals("Pérez", resultado.getLastname());
        assertEquals("juan.perez@unicauca.edu.co", resultado.getEmail());
        assertEquals("Ingeniería de Sistemas", resultado.getDepartment());
        assertEquals("Ingeniería de Software", resultado.getPrograma());
        assertTrue(resultado.tieneRol(EnumRol.DOCENTE));
        assertTrue(resultado.tieneRol(EnumRol.ESTUDIANTE));

        verify(personaRepoOutPort).findById(1L);
        verify(personaRepoOutPort).save(any(Persona.class));
    }

    @Test
    void testGuardarPersona_ActualizarExistente() {
        // Arrange
        when(personaRepoOutPort.findById(1L)).thenReturn(Optional.of(personaExistente));
        when(personaRepoOutPort.save(any(Persona.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Persona resultado = personaService.guardarPersona(personaRequestExistente);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdUsuario());
        assertEquals("Juan Actualizado", resultado.getName());
        assertEquals("Pérez Actualizado", resultado.getLastname());
        assertEquals("juan.actualizado@unicauca.edu.co", resultado.getEmail());
        assertEquals("Ingeniería Civil", resultado.getDepartment());
        assertEquals("Ingeniería Civil", resultado.getPrograma());

        verify(personaRepoOutPort).findById(1L);
        verify(personaRepoOutPort).save(any(Persona.class));
    }

    @Test
    void testGuardarPersona_RolNoReconocido() {
        // Arrange
        Set<String> rolesInvalidos = new HashSet<>(Arrays.asList("DOCENTE", "ROL_INEXISTENTE", "ESTUDIANTE"));
        PersonaRequest requestConRolInvalido = new PersonaRequest(
                2L,
                "Maria",
                "Gómez",
                "maria.gomez@unicauca.edu.co",
                rolesInvalidos,
                "Departamento",
                "Programa"
        );

        when(personaRepoOutPort.findById(2L)).thenReturn(Optional.empty());
        when(personaRepoOutPort.save(any(Persona.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Persona resultado = personaService.guardarPersona(requestConRolInvalido);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.tieneRol(EnumRol.DOCENTE));
        assertTrue(resultado.tieneRol(EnumRol.ESTUDIANTE));
        // El rol "ROL_INEXISTENTE" debería ser ignorado
    }

    // 2. TESTS PARA findPersonaByEmail()

    @Test
    void testFindPersonaByEmail_Existe() {
        // Arrange
        String email = "juan.perez@unicauca.edu.co";
        when(personaRepoOutPort.findByEmail(email)).thenReturn(Optional.of(personaExistente));

        // Act
        Optional<Persona> resultado = personaService.findPersonaByEmail(email);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(email, resultado.get().getEmail());
        verify(personaRepoOutPort).findByEmail(email);
    }

    @Test
    void testFindPersonaByEmail_NoExiste() {
        // Arrange
        String email = "inexistente@unicauca.edu.co";
        when(personaRepoOutPort.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        Optional<Persona> resultado = personaService.findPersonaByEmail(email);

        // Assert
        assertFalse(resultado.isPresent());
        verify(personaRepoOutPort).findByEmail(email);
    }

    @Test
    void testFindPersonaByEmail_EmailNulo() {
        // Act
        Optional<Persona> resultado = personaService.findPersonaByEmail(null);

        // Assert
        assertFalse(resultado.isPresent());
        verify(personaRepoOutPort, never()).findByEmail(anyString());
    }

    @Test
    void testFindPersonaByEmail_EmailVacio() {
        // Act
        Optional<Persona> resultado = personaService.findPersonaByEmail("   ");

        // Assert
        assertFalse(resultado.isPresent());
        verify(personaRepoOutPort, never()).findByEmail(anyString());
    }

    // 3. TESTS PARA listarDocentesDisponiblesParaEvaluar()

    @Test
    void testListarDocentesDisponiblesParaEvaluar_FormatoAExiste() {
        // Arrange
        Long formatoAId = 1L;

        // Director y codirector
        Persona director = new Persona(
                10L,
                "Director",
                "Apellido",
                "director@unicauca.edu.co",
                "Ingeniería de Sistemas",
                null,
                new HashSet<>(Arrays.asList(EnumRol.DOCENTE))
        );

        Persona codirector = new Persona(
                11L,
                "Codirector",
                "Apellido",
                "codirector@unicauca.edu.co",
                "Ingeniería de Sistemas",
                null,
                new HashSet<>(Arrays.asList(EnumRol.DOCENTE))
        );

        // Formato A usando constructor
        FormatoA formatoA = new FormatoA(
                formatoAId,
                "Título",
                "Modalidad",
                director,
                codirector,
                "Objetivo general",
                "Objetivos específicos",
                "archivo.pdf",
                "carta_laboral.pdf",
                0,
                new ArrayList<>(),
                EnumEstado.ENTREGADO,
                "Observaciones"
        );

        // Docentes del departamento (incluyendo director y codirector)
        Persona docente1 = new Persona(
                20L,
                "Docente1",
                "Apellido",
                "docente1@unicauca.edu.co",
                "Ingeniería de Sistemas",
                null,
                new HashSet<>(Arrays.asList(EnumRol.DOCENTE))
        );

        Persona docente2 = new Persona(
                21L,
                "Docente2",
                "Apellido",
                "docente2@unicauca.edu.co",
                "Ingeniería de Sistemas",
                null,
                new HashSet<>(Arrays.asList(EnumRol.DOCENTE))
        );

        List<Persona> todosDocentes = Arrays.asList(director, codirector, docente1, docente2);

        when(formatoAService.findById(formatoAId)).thenReturn(formatoA);
        when(personaRepoOutPort.findByDepartmentIgnoreCase("Ingeniería de Sistemas"))
                .thenReturn(todosDocentes);
        // Mock del repositorio para findPersonaByEmail (que usa findByEmail internamente)
        when(personaRepoOutPort.findByEmail("director@unicauca.edu.co")).thenReturn(Optional.of(director));
        when(personaRepoOutPort.findByEmail("codirector@unicauca.edu.co")).thenReturn(Optional.of(codirector));

        // Act
        List<Persona> resultado = personaService.listarDocentesDisponiblesParaEvaluar(formatoAId);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size()); // Solo docente1 y docente2 (excluyendo director y codirector)
        assertFalse(resultado.stream().anyMatch(p -> p.getEmail().equals("director@unicauca.edu.co")));
        assertFalse(resultado.stream().anyMatch(p -> p.getEmail().equals("codirector@unicauca.edu.co")));

        verify(formatoAService).findById(formatoAId);
        verify(personaRepoOutPort).findByDepartmentIgnoreCase("Ingeniería de Sistemas");
    }
    @Test
    void testListarDocentesDisponiblesParaEvaluar_FormatoANoExiste() {
        // Arrange
        Long formatoAId = 999L;
        when(formatoAService.findById(formatoAId)).thenReturn(null);

        // Act
        List<Persona> resultado = personaService.listarDocentesDisponiblesParaEvaluar(formatoAId);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(formatoAService).findById(formatoAId);
        verify(personaRepoOutPort, never()).findByDepartmentIgnoreCase(anyString());
    }

    @Test
    void testListarDocentesDisponiblesParaEvaluar_DirectorSinDepartamento() {
        // Arrange
        Long formatoAId = 1L;
        Persona directorSinDepartamento = new Persona(
                10L,
                "Director",
                "Apellido",
                "director@unicauca.edu.co",
                null, // Sin departamento
                null,
                new HashSet<>(Arrays.asList(EnumRol.DOCENTE))
        );

        // Usar constructor de FormatoA que acepta todos los parámetros
        FormatoA formatoA = new FormatoA(
                formatoAId,
                "Título",
                "Modalidad",
                directorSinDepartamento,
                null,
                "Objetivo general",
                "Objetivos específicos",
                "archivo.pdf",
                "carta_laboral.pdf",
                0,
                new ArrayList<>(),
                EnumEstado.ENTREGADO,
                "Observaciones"
        );

        when(formatoAService.findById(formatoAId)).thenReturn(formatoA);
        when(personaService.findPersonaByEmail("director@unicauca.edu.co"))
                .thenReturn(Optional.of(directorSinDepartamento));

        // Act
        List<Persona> resultado = personaService.listarDocentesDisponiblesParaEvaluar(formatoAId);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void testListarDocentesDisponiblesParaEvaluar_SoloDocentes() {
        // Arrange
        Long formatoAId = 1L;
        Persona director = new Persona(
                10L,
                "Director",
                "Apellido",
                "director@unicauca.edu.co",
                "Ingeniería de Sistemas",
                null,
                new HashSet<>(Arrays.asList(EnumRol.DOCENTE))
        );

        // Formato A usando constructor
        FormatoA formatoA = new FormatoA(
                formatoAId,
                "Título",
                "Modalidad",
                director,
                null, // Sin codirector
                "Objetivo general",
                "Objetivos específicos",
                "archivo.pdf",
                "carta_laboral.pdf",
                0,
                new ArrayList<>(),
                EnumEstado.ENTREGADO,
                "Observaciones"
        );

        // Personas que no son docentes (deben ser filtradas)
        Persona estudiante = new Persona(
                30L,
                "Estudiante",
                "Apellido",
                "estudiante@unicauca.edu.co",
                "Ingeniería de Sistemas",
                null,
                new HashSet<>(Arrays.asList(EnumRol.ESTUDIANTE))
        );

        Persona coordinador = new Persona(
                31L,
                "Coordinador",
                "Apellido",
                "coordinador@unicauca.edu.co",
                "Ingeniería de Sistemas",
                null,
                new HashSet<>(Arrays.asList(EnumRol.COORDINADOR))
        );

        Persona docente = new Persona(
                32L,
                "Docente",
                "Apellido",
                "docente@unicauca.edu.co",
                "Ingeniería de Sistemas",
                null,
                new HashSet<>(Arrays.asList(EnumRol.DOCENTE))
        );

        List<Persona> todasPersonas = Arrays.asList(director, estudiante, coordinador, docente);

        when(formatoAService.findById(formatoAId)).thenReturn(formatoA);
        when(personaRepoOutPort.findByDepartmentIgnoreCase("Ingeniería de Sistemas"))
                .thenReturn(todasPersonas);
        // Mock para el director
        when(personaRepoOutPort.findByEmail("director@unicauca.edu.co")).thenReturn(Optional.of(director));

        // Act
        List<Persona> resultado = personaService.listarDocentesDisponiblesParaEvaluar(formatoAId);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size()); // Solo el docente (excluyendo director)
        assertEquals("docente@unicauca.edu.co", resultado.get(0).getEmail());
    }
    @Test
    void testListarDocentesDisponiblesParaEvaluar_SinCodirector() {
        // Arrange
        Long formatoAId = 1L;
        Persona director = new Persona(
                10L,
                "Director",
                "Apellido",
                "director@unicauca.edu.co",
                "Ingeniería de Sistemas",
                null,
                new HashSet<>(Arrays.asList(EnumRol.DOCENTE))
        );

        // Formato A sin codirector usando constructor
        FormatoA formatoA = new FormatoA(
                formatoAId,
                "Título",
                "Modalidad",
                director,
                null, // Sin codirector
                "Objetivo general",
                "Objetivos específicos",
                "archivo.pdf",
                "carta_laboral.pdf",
                0,
                new ArrayList<>(),
                EnumEstado.ENTREGADO,
                "Observaciones"
        );

        Persona docente1 = new Persona(
                20L,
                "Docente1",
                "Apellido",
                "docente1@unicauca.edu.co",
                "Ingeniería de Sistemas",
                null,
                new HashSet<>(Arrays.asList(EnumRol.DOCENTE))
        );

        Persona docente2 = new Persona(
                21L,
                "Docente2",
                "Apellido",
                "docente2@unicauca.edu.co",
                "Ingeniería de Sistemas",
                null,
                new HashSet<>(Arrays.asList(EnumRol.DOCENTE))
        );

        List<Persona> todosDocentes = Arrays.asList(director, docente1, docente2);

        when(formatoAService.findById(formatoAId)).thenReturn(formatoA);
        when(personaRepoOutPort.findByDepartmentIgnoreCase("Ingeniería de Sistemas"))
                .thenReturn(todosDocentes);
        // Mock para el director (no hay codirector)
        when(personaRepoOutPort.findByEmail("director@unicauca.edu.co")).thenReturn(Optional.of(director));

        // Act
        List<Persona> resultado = personaService.listarDocentesDisponiblesParaEvaluar(formatoAId);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size()); // docente1 y docente2 (excluyendo solo director)
    }
    @Test
    void testListarDocentesDisponiblesParaEvaluar_SinDirector() {
        // Arrange
        Long formatoAId = 1L;

        // Formato A sin director
        FormatoA formatoA = new FormatoA(
                formatoAId,
                "Título",
                "Modalidad",
                null, // Sin director
                null, // Sin codirector
                "Objetivo general",
                "Objetivos específicos",
                "archivo.pdf",
                "carta_laboral.pdf",
                0,
                new ArrayList<>(),
                EnumEstado.ENTREGADO,
                "Observaciones"
        );

        when(formatoAService.findById(formatoAId)).thenReturn(formatoA);

        // Act
        List<Persona> resultado = personaService.listarDocentesDisponiblesParaEvaluar(formatoAId);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(personaRepoOutPort, never()).findByDepartmentIgnoreCase(anyString());
    }

    // 4. TESTS PARA findAll()

    @Test
    void testFindAll() {
        // Arrange
        List<Persona> personas = Arrays.asList(personaExistente);
        when(personaRepoOutPort.findAll()).thenReturn(personas);

        // Act
        List<Persona> resultado = personaService.findAll();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(personaRepoOutPort).findAll();
    }

    @Test
    void testFindAll_ListaVacia() {
        // Arrange
        when(personaRepoOutPort.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Persona> resultado = personaService.findAll();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(personaRepoOutPort).findAll();
    }

    // 5. TEST PARA comportamiento de convertirRoles()

    @Test
    void testConvertirRoles_Comportamiento() {
        // Test indirecto del método privado convertirRoles
        // Arrange
        Set<String> rolesString = new HashSet<>(Arrays.asList("DOCENTE", "ESTUDIANTE", "JEFE_DEPARTAMENTO", "ROL_INVALIDO"));
        PersonaRequest request = new PersonaRequest(
                1L,
                "Test",
                "Test",
                "test@unicauca.edu.co",
                rolesString,
                "Depto",
                "Prog"
        );

        when(personaRepoOutPort.findById(1L)).thenReturn(Optional.empty());
        when(personaRepoOutPort.save(any(Persona.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Persona resultado = personaService.guardarPersona(request);

        // Assert
        assertTrue(resultado.tieneRol(EnumRol.DOCENTE));
        assertTrue(resultado.tieneRol(EnumRol.ESTUDIANTE));
        assertTrue(resultado.tieneRol(EnumRol.JEFE_DEPARTAMENTO));
        assertFalse(resultado.tieneRol(EnumRol.COORDINADOR));
        // ROL_INVALIDO debe ser ignorado
    }

    @Test
    void testConvertirRoles_RolesNulos() {
        // Arrange
        PersonaRequest requestNulo = new PersonaRequest(
                2L,
                "Test",
                "Test",
                "test2@unicauca.edu.co",
                null, // Roles nulos
                "Depto",
                "Prog"
        );

        when(personaRepoOutPort.findById(2L)).thenReturn(Optional.empty());
        when(personaRepoOutPort.save(any(Persona.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Persona resultadoNulo = personaService.guardarPersona(requestNulo);

        // Assert
        assertNotNull(resultadoNulo.getRoles());
        assertTrue(resultadoNulo.getRoles().isEmpty());
    }

    @Test
    void testConvertirRoles_RolesVacios() {
        // Arrange
        PersonaRequest requestVacio = new PersonaRequest(
                3L,
                "Test",
                "Test",
                "test3@unicauca.edu.co",
                new HashSet<>(), // Roles vacíos
                "Depto",
                "Prog"
        );

        when(personaRepoOutPort.findById(3L)).thenReturn(Optional.empty());
        when(personaRepoOutPort.save(any(Persona.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Persona resultadoVacio = personaService.guardarPersona(requestVacio);

        // Assert
        assertTrue(resultadoVacio.getRoles().isEmpty());
    }
}