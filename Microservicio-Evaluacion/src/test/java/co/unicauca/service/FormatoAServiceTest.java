package co.unicauca.service;

import co.unicauca.entity.EnumEstado;
import co.unicauca.entity.FormatoA;
import co.unicauca.entity.Persona;
import co.unicauca.infra.dto.notificacion.FormatoAReponseNotificacion;
import co.unicauca.infra.dto.FormatoARequest;
import co.unicauca.infra.dto.FormatoAResponse;
import co.unicauca.repository.FormatoARepository;
import co.unicauca.repository.PersonaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class FormatoAServiceTest {

    @Mock
    private FormatoARepository formatoARepository;

    @Mock
    private PersonaRepository personaRepository;

    @InjectMocks
    private FormatoAService formatoAService;

    private FormatoA formatoA;
    private Persona estudiante;
    private Persona director;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        estudiante = new Persona();
        estudiante.setIdUsuario(1L);
        estudiante.setEmail("estudiante@unicauca.edu.co");
        estudiante.setPrograma("Ingeniería de Sistemas");

        director = new Persona();
        director.setIdUsuario(2L);
        director.setEmail("director@unicauca.edu.co");

        formatoA = new FormatoA();
        formatoA.setId(1L);
        formatoA.setTitle("Proyecto de Prueba");
        formatoA.setState(EnumEstado.ENTREGADO);
        formatoA.setEstudiantes(List.of(estudiante));
        formatoA.setProjectManager(director);
        formatoA.setCounter(0);
    }

    @Test
    void guardarFormatoA() {
        // Configurar mocks
        when(personaRepository.findByEmail("director@unicauca.edu.co")).thenReturn(Optional.of(director));
        when(personaRepository.findByEmail("estudiante@unicauca.edu.co")).thenReturn(Optional.of(estudiante));
        when(formatoARepository.save(any(FormatoA.class))).thenAnswer(inv -> inv.getArgument(0));

        // Usar el orden correcto de parámetros del record FormatoARequest
        FormatoARequest request = new FormatoARequest(
                null,                                   // id
                "Proyecto de Prueba",                   // title
                "Presencial",                           // mode
                "director@unicauca.edu.co",             // projectManagerEmail
                null,                                   // projectCoManagerEmail
                "Objetivo general",                     // generalObjetive
                "Objetivos específicos",                // specificObjetives
                "archivo.pdf",                          // archivoPDF
                "carta.pdf",                            // cartaLaboral
                List.of("estudiante@unicauca.edu.co"),  // estudiante
                0                                       // counter
        );

        FormatoA resultado = formatoAService.guardarFormatoA(request);

        assertNotNull(resultado);
        assertEquals("Proyecto de Prueba", resultado.getTitle());
        assertEquals(EnumEstado.ENTREGADO, resultado.getState());
        verify(formatoARepository, times(1)).save(any(FormatoA.class));
    }

    @Test
    void findAll() {
        when(formatoARepository.findAll()).thenReturn(List.of(formatoA));

        List<FormatoA> lista = formatoAService.findAll();

        assertEquals(1, lista.size());
        assertEquals("Proyecto de Prueba", lista.get(0).getTitle());
    }

    @Test
    void actualizarEstado() {
        when(formatoARepository.findById(1L)).thenReturn(Optional.of(formatoA));
        when(formatoARepository.save(any(FormatoA.class))).thenReturn(formatoA);

        Optional<FormatoA> actualizado = formatoAService.actualizarEstado(1L, EnumEstado.RECHAZADO, "Faltan datos");

        assertTrue(actualizado.isPresent());
        assertEquals(EnumEstado.RECHAZADO, actualizado.get().getState());
        assertEquals("Faltan datos", actualizado.get().getObservations());
        assertEquals(1, actualizado.get().getCounter());
    }

    @Test
    void mapToFormatoAResponse() {
        formatoA.setObservations("OK");
        FormatoAResponse response = formatoAService.mapToFormatoAResponse(formatoA);

        assertNotNull(response);
        assertEquals("Proyecto de Prueba", response.title());
        assertEquals("ENTREGADO", response.state());
    }

    @Test
    void mapToFormatoAResponseNotificacion() {
        formatoA.setProjectManager(director);
        formatoA.setEstudiantes(List.of(estudiante));

        FormatoAReponseNotificacion notificacion = formatoAService.mapToFormatoAResponseNotificacion(formatoA);

        assertNotNull(notificacion);
        assertTrue(notificacion.correosEstudiantes().contains("estudiante@unicauca.edu.co"));
        assertTrue(notificacion.correosDocentes().contains("director@unicauca.edu.co"));
    }

    @Test
    void findById() {
        when(formatoARepository.findById(1L)).thenReturn(Optional.of(formatoA));

        FormatoA encontrado = formatoAService.findById(1L);

        assertNotNull(encontrado);
        assertEquals("Proyecto de Prueba", encontrado.getTitle());
    }

    @Test
    void listarFormatosPorPrograma() {
        when(formatoARepository.findAll()).thenReturn(List.of(formatoA));

        List<FormatoA> resultado = formatoAService.listarFormatosPorPrograma("Ingeniería de Sistemas");

        assertEquals(1, resultado.size());
        assertEquals("Proyecto de Prueba", resultado.get(0).getTitle());
    }
}