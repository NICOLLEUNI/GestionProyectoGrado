package co.unicauca.service;

import co.unicauca.entity.Anteproyecto;
import co.unicauca.infra.dto.AnteproyectoRequest;
import co.unicauca.repository.AnteproyectoRepository;
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
    private AnteproyectoRepository anteproyectoRepository;

    @InjectMocks
    private AnteproyectoService anteproyectoService;

    private Anteproyecto anteproyecto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        anteproyecto = new Anteproyecto();
        anteproyecto.setId(1L);
        anteproyecto.setTitulo("Sistema de gestión académica");
        anteproyecto.setEstado("PENDIENTE");
        anteproyecto.setIdProyectoGrado(10L);
        anteproyecto.setArchivoPDF("pendiente.pdf");
        anteproyecto.setFechaCreacion(LocalDate.now());
    }

    @Test
    void guardarAnteproyecto_DeberiaGuardarCorrectamente() {
        // Arrange
        AnteproyectoRequest request = new AnteproyectoRequest(
                null,
                "Sistema de gestión académica",
                LocalDate.of(2025, 11, 1),
                "APROBADO",
                10L
        );

        when(anteproyectoRepository.save(any(Anteproyecto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Anteproyecto resultado = anteproyectoService.guardarAnteproyecto(request);

        // Assert
        assertNotNull(resultado);
        assertEquals("Sistema de gestión académica", resultado.getTitulo());
        assertEquals("APROBADO", resultado.getEstado());
        assertEquals(10L, resultado.getIdProyectoGrado());
        assertEquals("pendiente.pdf", resultado.getArchivoPDF());
        assertEquals(LocalDate.of(2025, 11, 1), resultado.getFechaCreacion());
        verify(anteproyectoRepository, times(1)).save(any(Anteproyecto.class));
    }

    @Test
    void guardarAnteproyecto_SiNoTieneFecha_UsaFechaActual() {
        // Arrange
        AnteproyectoRequest request = new AnteproyectoRequest(
                null,
                "Proyecto sin fecha",
                null, // estado nulo -> debería asignar "PENDIENTE"
                null,
                20L// fecha nula -> debería asignar LocalDate.now()
        );

        when(anteproyectoRepository.save(any(Anteproyecto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Anteproyecto resultado = anteproyectoService.guardarAnteproyecto(request);

        // Assert
        assertNotNull(resultado);
        assertEquals("Proyecto sin fecha", resultado.getTitulo());
        assertEquals("PENDIENTE", resultado.getEstado()); // valor por defecto
        assertEquals(20L, resultado.getIdProyectoGrado());
        assertEquals("pendiente.pdf", resultado.getArchivoPDF());
        assertNotNull(resultado.getFechaCreacion());
        verify(anteproyectoRepository, times(1)).save(any(Anteproyecto.class));
    }

    @Test
    void listarAnteproyectos_DeberiaRetornarLista() {
        when(anteproyectoRepository.findAll()).thenReturn(List.of(anteproyecto));

        List<Anteproyecto> lista = anteproyectoService.listarAnteproyectos();

        assertEquals(1, lista.size());
        assertEquals("Sistema de gestión académica", lista.get(0).getTitulo());
        verify(anteproyectoRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_DeberiaRetornarAnteproyecto() {
        when(anteproyectoRepository.findById(1L)).thenReturn(Optional.of(anteproyecto));

        Anteproyecto encontrado = anteproyectoService.buscarPorId(1L);

        assertNotNull(encontrado);
        assertEquals("Sistema de gestión académica", encontrado.getTitulo());
        verify(anteproyectoRepository, times(1)).findById(1L);
    }

    @Test
    void buscarPorId_CuandoNoExiste_DeberiaLanzarExcepcion() {
        when(anteproyectoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            anteproyectoService.buscarPorId(99L);
        });

        assertEquals("Anteproyecto no encontrado con ID: 99", ex.getMessage());
    }
}