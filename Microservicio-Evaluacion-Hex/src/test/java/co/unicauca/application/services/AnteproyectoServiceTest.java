package co.unicauca.application.services;

import co.unicauca.application.ports.output.AnteproyectoRepoOutPort;
import co.unicauca.application.ports.output.PersonaRepoOutPort;
import co.unicauca.application.services.AnteproyectoService;
import co.unicauca.domain.entities.Anteproyecto;
import co.unicauca.infrastructure.dto.request.AnteproyectoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnteproyectoServiceTest {

    @Mock
    private AnteproyectoRepoOutPort anteproyectoRepoOutPort;

    @Mock
    private PersonaRepoOutPort personaRepoOutPort;

    @InjectMocks
    private AnteproyectoService anteproyectoService;

    private Anteproyecto anteproyecto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        anteproyecto = new Anteproyecto();
        anteproyecto.asignarTitulo("Sistema de gestión académica");
        anteproyecto.asignarEstado("ENTREGADO");
        anteproyecto.asignarIdProyecto(10L);
        anteproyecto.asignarArchivoPDF("pendiente.pdf");
        anteproyecto.asignarFechaCreacion(LocalDate.now());
    }

    @Test
    void guardarAnteproyecto_DeberiaGuardarCorrectamente() {
        // Arrange: el request ahora tiene id primero
        AnteproyectoRequest request = new AnteproyectoRequest(
                null,                                // id
                "Sistema de gestión académica",      // titulo
                LocalDate.of(2025, 11, 1),           // fecha
                "ENTREGADO",                          // estado
                10L                                  // idProyectoGrado
        );

        when(anteproyectoRepoOutPort.save(any(Anteproyecto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Anteproyecto resultado = anteproyectoService.guardarAnteproyecto(request);

        // Assert
        assertNotNull(resultado);
        assertEquals("Sistema de gestión académica", resultado.getTitulo());
        assertEquals("ENTREGADO", resultado.getEstado());
        assertEquals(10L, resultado.getIdProyectoGrado());
        assertEquals("pendiente.pdf", resultado.getArchivoPDF());
        assertEquals(LocalDate.of(2025, 11, 1), resultado.getFechaCreacion());

        verify(anteproyectoRepoOutPort, times(1)).save(any(Anteproyecto.class));
    }

    @Test
    void guardarAnteproyecto_SiNoTieneFecha_UsaFechaActual() {
        LocalDate hoy = LocalDate.now();

        AnteproyectoRequest request = new AnteproyectoRequest(
                null,
                "Proyecto sin fecha",
                null,        // SIN FECHA → usa LocalDate.now()
                "ENTREGADO",        // SIN ESTADO → PENDIENTE por defecto
                20L
        );

        when(anteproyectoRepoOutPort.save(any(Anteproyecto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Anteproyecto resultado = anteproyectoService.guardarAnteproyecto(request);

        assertNotNull(resultado);
        assertEquals("Proyecto sin fecha", resultado.getTitulo());
        assertEquals("ENTREGADO", resultado.getEstado());
        assertEquals(20L, resultado.getIdProyectoGrado());
        assertEquals("pendiente.pdf", resultado.getArchivoPDF());
        assertEquals(hoy, resultado.getFechaCreacion());

        verify(anteproyectoRepoOutPort).save(any(Anteproyecto.class));
    }

    @Test
    void listarAnteproyectos_DeberiaRetornarLista() {
        when(anteproyectoRepoOutPort.findAll()).thenReturn(List.of(anteproyecto));

        List<Anteproyecto> lista = anteproyectoService.listarAnteproyectos();

        assertEquals(1, lista.size());
        assertEquals("Sistema de gestión académica", lista.get(0).getTitulo());
    }

    @Test
    void buscarPorId_DeberiaRetornarAnteproyecto() {
        when(anteproyectoRepoOutPort.findById(1L)).thenReturn(Optional.of(anteproyecto));

        Anteproyecto encontrado = anteproyectoService.buscarPorId(1L);

        assertNotNull(encontrado);
        assertEquals("Sistema de gestión académica", encontrado.getTitulo());
    }

    @Test
    void buscarPorId_CuandoNoExiste_DeberiaLanzarExcepcion() {
        when(anteproyectoRepoOutPort.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                anteproyectoService.buscarPorId(99L)
        );

        assertEquals("Anteproyecto no encontrado con ID: 99", ex.getMessage());
    }
}
