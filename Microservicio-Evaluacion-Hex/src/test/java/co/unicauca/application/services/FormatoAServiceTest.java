package co.unicauca.application.services;

import co.unicauca.application.ports.output.FormatoARepoOutPort;
import co.unicauca.application.ports.output.PersonaRepoOutPort;
import co.unicauca.domain.entities.*;
import co.unicauca.infrastructure.dto.request.FormatoARequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FormatoAServiceTest {

    private FormatoARepoOutPort formatoRepoMock;
    private PersonaRepoOutPort personaRepoMock;
    private FormatoAService service;

    @BeforeEach
    void init() {
        formatoRepoMock = mock(FormatoARepoOutPort.class);
        personaRepoMock = mock(PersonaRepoOutPort.class);
        service = new FormatoAService(formatoRepoMock, personaRepoMock);
    }

    // ========================================================================
    // ✔ TEST: guardarFormatoA()
    // ========================================================================

    @Test
    void guardarFormatoA() {

        Persona director = new Persona(
                1L, "Carlos", "Arteaga",
                "carlos.arteaga@unicauca.edu.co",
                "ELECTRONICA",
                null,
                EnumSet.of(EnumRol.DOCENTE)
        );

        Persona codirector = new Persona(
                2L, "Maria", "Soto",
                "maria.soto@unicauca.edu.co",
                "SISTEMAS",
                null,
                EnumSet.of(EnumRol.DOCENTE)
        );

        Persona estudiante = new Persona(
                3L, "Nicolle", "Montaño",
                "nicolle.montano@unicauca.edu.co",
                "SISTEMAS",
                null,
                EnumSet.of(EnumRol.ESTUDIANTE)
        );

        when(personaRepoMock.findByEmail("carlos.arteaga@unicauca.edu.co"))
                .thenReturn(Optional.of(director));

        when(personaRepoMock.findByEmail("maria.soto@unicauca.edu.co"))
                .thenReturn(Optional.of(codirector));

        when(personaRepoMock.findByEmail("nicolle.montano@unicauca.edu.co"))
                .thenReturn(Optional.of(estudiante));

        ArgumentCaptor<FormatoA> captor = ArgumentCaptor.forClass(FormatoA.class);
        when(formatoRepoMock.save(captor.capture()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        FormatoARequest req = new FormatoARequest(
                1L,
                "Proyecto IA",
                "PRACTICA",
                "carlos.arteaga@unicauca.edu.co",
                "maria.soto@unicauca.edu.co",
                "Objetivo general",
                "Objetivos específicos",
                "archivo.pdf",
                "carta.pdf",
                List.of("nicolle.montano@unicauca.edu.co"),
                0
        );

        FormatoA saved = service.guardarFormatoA(req);
        FormatoA capturado = captor.getValue();

        assertNotNull(saved);
        assertNotNull(capturado);
        assertEquals("Proyecto IA", capturado.getTitle());
        assertEquals(EnumEstado.ENTREGADO, capturado.getState());
        assertEquals(director, capturado.getProjectManager());
        assertEquals(codirector, capturado.getProjectCoManager());
        assertEquals(1, capturado.getEstudiantes().size());
        assertEquals(estudiante, capturado.getEstudiantes().get(0));
    }

    // ========================================================================
    // ✔ TEST: actualizarEstado()
    // ========================================================================

    @Test
    void actualizarEstado() {

        FormatoA formato = new FormatoA();
        formato.asignarId(1L);
        formato.asignarEstado(EnumEstado.ENTREGADO);

        when(formatoRepoMock.findById(1L))
                .thenReturn(Optional.of(formato));

        when(formatoRepoMock.save(any(FormatoA.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Optional<FormatoA> actualizado = service.actualizarEstado(1L, EnumEstado.APROBADO,"Bien");

        assertEquals(EnumEstado.APROBADO, actualizado.get().getState());
        verify(formatoRepoMock, times(1)).save(any(FormatoA.class));
    }

    // ========================================================================
    // ✔ TEST: listarTodos()
    // ========================================================================

    @Test
    void listarTodos() {

        when(formatoRepoMock.findAll())
                .thenReturn(List.of(new FormatoA(), new FormatoA()));

        List<FormatoA> lista = service.listarTodos();

        assertNotNull(lista);
        assertEquals(2, lista.size());
        verify(formatoRepoMock, times(1)).findAll();
    }

    // ========================================================================
    // ✔ TEST: findById()
    // ========================================================================

    @Test
    void findById() {

        FormatoA formato = new FormatoA();
        formato.asignarId(10L);

        when(formatoRepoMock.findById(10L))
                .thenReturn(Optional.of(formato));

        Optional<FormatoA> encontrado = Optional.ofNullable(service.findById(10L));

        assertTrue(encontrado.isPresent());
        assertEquals(10L, encontrado.get().getId());
    }
}
