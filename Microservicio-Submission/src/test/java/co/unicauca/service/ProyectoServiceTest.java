package co.unicauca.service;

import co.unicauca.entity.FormatoA;
import co.unicauca.entity.FormatoAVersion;
import co.unicauca.entity.ProyectoGrado;
import co.unicauca.infra.dto.ProyectoGradoResponse;
import co.unicauca.infra.messaging.RabbitMQPublisher;
import co.unicauca.repository.ProyectoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProyectoServiceTest {

    private ProyectoRepository proyectoRepository;
    private RabbitMQPublisher rabbitMQPublisher;
    private ProyectoService proyectoService;

    @BeforeEach
    void setUp() {
        proyectoRepository = mock(ProyectoRepository.class);
        rabbitMQPublisher = mock(RabbitMQPublisher.class);
        proyectoService = new ProyectoService(proyectoRepository, rabbitMQPublisher);
    }

    @Test
    void crearProyectoGrado_success() {
        // Arrange
        FormatoA formatoA = new FormatoA();
        formatoA.setId(1L);
        formatoA.setTitle("Proyecto de Grado Ejemplo");
        formatoA.setEstudianteEmails(Arrays.asList("estudiante@correo.com"));

        FormatoAVersion version = new FormatoAVersion();
        version.setId(1L);

        ProyectoGrado proyectoMock = new ProyectoGrado();
        proyectoMock.setId(100L);
        proyectoMock.setNombre(formatoA.getTitle());
        proyectoMock.setFechaCreacion(LocalDate.now());
        proyectoMock.setEstudiantesEmail(new ArrayList<>(formatoA.getEstudianteEmails()));
        proyectoMock.setFormatoAActual(formatoA);  // ✅ IMPORTANTE: Inicializar formatoAActual

        when(proyectoRepository.existsByEstudianteEmailAndEstadoNot(anyString(), anyString()))
                .thenReturn(false);

        when(proyectoRepository.save(any(ProyectoGrado.class)))
                .thenReturn(proyectoMock);

        // Act
        ProyectoGrado result = proyectoService.crearProyectoGrado(formatoA, version);

        // Assert
        assertNotNull(result);
        assertEquals("Proyecto de Grado Ejemplo", result.getNombre());
        assertEquals(1L, result.getFormatoAActual().getId());  // ✅ Test de confirmación
        verify(rabbitMQPublisher).publicarProyectoGradoCreado(any(ProyectoGradoResponse.class));
    }
    // ✅ TEST 2: Error por estudiante con proyecto activo
    @Test
    void crearProyectoGrado_estudianteConProyectoActivo_lanzaExcepcion() {
        FormatoA formatoA = new FormatoA();
        formatoA.setEstudianteEmails(List.of("est1@mail.com"));

        when(proyectoRepository.existsByEstudianteEmailAndEstadoNot(anyString(), anyString()))
                .thenReturn(true);

        assertThrows(RuntimeException.class, () ->
                proyectoService.crearProyectoGrado(formatoA, new FormatoAVersion())
        );

        verify(proyectoRepository, never()).save(any());
        verify(rabbitMQPublisher, never()).publicarProyectoGradoCreado(any());
    }

    // ✅ TEST 3: Agregar versión al proyecto ya existente
    @Test
    void agregarVersionAProyectoGrado_success() {
        FormatoA formatoA = new FormatoA();
        formatoA.setId(10L);

        ProyectoGrado proyecto = new ProyectoGrado();
        proyecto.setHistorialFormatosA(new java.util.ArrayList<>());

        when(proyectoRepository.findByFormatoAActualId(10L))
                .thenReturn(Optional.of(proyecto));

        when(proyectoRepository.save(any())).thenReturn(proyecto);

        FormatoAVersion nuevaVersion = new FormatoAVersion();
        ProyectoGrado updated = proyectoService.agregarVersionAProyectoGrado(formatoA, nuevaVersion);

        assertEquals(1, updated.getHistorialFormatosA().size());
        verify(proyectoRepository, times(1)).save(proyecto);
    }

    // ✅ TEST 4: Error al agregar versión si no existe proyecto
    @Test
    void agregarVersionAProyectoGrado_proyectoNoExiste_lanzaExcepcion() {
        when(proyectoRepository.findByFormatoAActualId(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                proyectoService.agregarVersionAProyectoGrado(new FormatoA(), new FormatoAVersion())
        );
    }

    // ✅ TEST 5: Eliminar proyecto por FormatoA
    @Test
    void eliminarProyectoPorFormatoA_success() {
        ProyectoGrado proyecto = new ProyectoGrado();
        when(proyectoRepository.findByFormatoAActualId(5L))
                .thenReturn(Optional.of(proyecto));

        proyectoService.eliminarProyectoPorFormatoA(5L);

        verify(proyectoRepository, times(1)).delete(proyecto);
    }

    // ✅ TEST 6: Error al eliminar proyecto inexistente
    @Test
    void eliminarProyectoPorFormatoA_noExiste_lanzaExcepcion() {
        when(proyectoRepository.findByFormatoAActualId(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                proyectoService.eliminarProyectoPorFormatoA(99L)
        );

        verify(proyectoRepository, never()).delete(any());
    }

}
