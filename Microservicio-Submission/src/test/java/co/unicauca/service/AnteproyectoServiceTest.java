package co.unicauca.service;

import co.unicauca.entity.*;
import co.unicauca.infra.dto.AnteproyectoResponse;
import co.unicauca.infra.dto.notification.AnteproyectoNotification;
import co.unicauca.infra.messaging.RabbitMQPublisher;
import co.unicauca.repository.AnteproyectoRepository;
import co.unicauca.repository.ProyectoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnteproyectoServiceTest {

    @Mock
    private AnteproyectoRepository anteproyectoRepository;

    @Mock
    private ProyectoRepository proyectoRepository;

    @Mock
    private RabbitMQPublisher rabbitMQPublisher;

    @InjectMocks
    private AnteproyectoService anteproyectoService;

    private Anteproyecto anteproyecto;
    private ProyectoGrado proyecto;
    private FormatoA formatoA;

    @BeforeEach
    void setUp() {
        // Configurar FormatoA
        formatoA = new FormatoA();
        formatoA.setId(1L);
        formatoA.setTitle("Proyecto de Grado Test");
        formatoA.setProjectManagerEmail("director@unicauca.edu.co");
        formatoA.setState(EnumEstado.APROBADO);

        // Configurar ProyectoGrado
        proyecto = new ProyectoGrado();
        proyecto.setId(1L);
        proyecto.setNombre("Proyecto de Grado Test");
        proyecto.setEstado("ACTIVO");
        proyecto.setFormatoAActual(formatoA);

        // Configurar Anteproyecto
        anteproyecto = new Anteproyecto();
        anteproyecto.setId(1L);
        anteproyecto.setTitulo("Proyecto de Grado Test");
        anteproyecto.setEstado(EnumEstadoAnteproyecto.ENTREGADO);
        anteproyecto.setFechaCreacion(LocalDate.now());
    }

    @Test
    void subirAnteproyecto_WithValidData_ShouldSaveAndReturnAnteproyecto() {
        // Arrange
        when(proyectoRepository.findByFormatoAActualTitleAndFormatoAActualState(
                "Proyecto de Grado Test", EnumEstado.APROBADO))
                .thenReturn(Optional.of(proyecto));

        when(anteproyectoRepository.save(any(Anteproyecto.class))).thenReturn(anteproyecto);
        when(proyectoRepository.save(any(ProyectoGrado.class))).thenReturn(proyecto);

        doNothing().when(rabbitMQPublisher).publicarAnteproyectoCreado(any(AnteproyectoResponse.class));
        doNothing().when(rabbitMQPublisher).publicarNotificacionAnteproyectoCreado(any(AnteproyectoNotification.class));

        // Act
        Anteproyecto result = anteproyectoService.subirAnteproyecto(anteproyecto);

        // Assert
        assertNotNull(result);
        assertEquals(anteproyecto, result);

        verify(proyectoRepository, times(1)).findByFormatoAActualTitleAndFormatoAActualState(
                "Proyecto de Grado Test", EnumEstado.APROBADO);
        verify(anteproyectoRepository, times(2)).save(any(Anteproyecto.class)); // Se guarda 2 veces
        verify(proyectoRepository, times(1)).save(proyecto);
        verify(rabbitMQPublisher, times(1)).publicarAnteproyectoCreado(any(AnteproyectoResponse.class));
        verify(rabbitMQPublisher, times(1)).publicarNotificacionAnteproyectoCreado(any(AnteproyectoNotification.class));
    }

    @Test
    void subirAnteproyecto_WhenProjectNotFound_ShouldThrowException() {
        // Arrange
        when(proyectoRepository.findByFormatoAActualTitleAndFormatoAActualState(
                "Proyecto de Grado Test", EnumEstado.APROBADO))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> anteproyectoService.subirAnteproyecto(anteproyecto));

        assertTrue(exception.getMessage().contains("No se encontró proyecto con FormatoA aprobado"));
        verify(proyectoRepository, times(1)).findByFormatoAActualTitleAndFormatoAActualState(
                "Proyecto de Grado Test", EnumEstado.APROBADO);
        verify(anteproyectoRepository, never()).save(any(Anteproyecto.class));
    }

    @Test
    void subirAnteproyecto_WhenProjectAlreadyHasAnteproyecto_ShouldThrowException() {
        // Arrange
        proyecto.setAnteproyecto(anteproyecto); // Proyecto ya tiene anteproyecto

        when(proyectoRepository.findByFormatoAActualTitleAndFormatoAActualState(
                "Proyecto de Grado Test", EnumEstado.APROBADO))
                .thenReturn(Optional.of(proyecto));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> anteproyectoService.subirAnteproyecto(anteproyecto));

        assertEquals("El proyecto ya tiene un anteproyecto asignado", exception.getMessage());
        verify(proyectoRepository, times(1)).findByFormatoAActualTitleAndFormatoAActualState(
                "Proyecto de Grado Test", EnumEstado.APROBADO);
        verify(anteproyectoRepository, never()).save(any(Anteproyecto.class));
    }

    @Test
    void subirAnteproyecto_WhenProjectNotActive_ShouldThrowException() {
        // Arrange
        proyecto.setEstado("INACTIVO"); // Proyecto no está activo

        when(proyectoRepository.findByFormatoAActualTitleAndFormatoAActualState(
                "Proyecto de Grado Test", EnumEstado.APROBADO))
                .thenReturn(Optional.of(proyecto));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> anteproyectoService.subirAnteproyecto(anteproyecto));

        assertEquals("El proyecto debe estar ACTIVO", exception.getMessage());
        verify(proyectoRepository, times(1)).findByFormatoAActualTitleAndFormatoAActualState(
                "Proyecto de Grado Test", EnumEstado.APROBADO);
        verify(anteproyectoRepository, never()).save(any(Anteproyecto.class));
    }

    @Test
    void subirAnteproyecto_WhenRabbitMQFails_ShouldStillReturnAnteproyecto() {
        // Arrange
        when(proyectoRepository.findByFormatoAActualTitleAndFormatoAActualState(
                "Proyecto de Grado Test", EnumEstado.APROBADO))
                .thenReturn(Optional.of(proyecto));

        when(anteproyectoRepository.save(any(Anteproyecto.class))).thenReturn(anteproyecto);
        when(proyectoRepository.save(any(ProyectoGrado.class))).thenReturn(proyecto);

        // Simular error en RabbitMQ
        doThrow(new RuntimeException("Error de conexión")).when(rabbitMQPublisher)
                .publicarAnteproyectoCreado(any(AnteproyectoResponse.class));

        // Act
        Anteproyecto result = anteproyectoService.subirAnteproyecto(anteproyecto);

        // Assert - Debe retornar el anteproyecto aunque falle RabbitMQ
        assertNotNull(result);
        assertEquals(anteproyecto, result);

        verify(proyectoRepository, times(1)).findByFormatoAActualTitleAndFormatoAActualState(
                "Proyecto de Grado Test", EnumEstado.APROBADO);
        verify(anteproyectoRepository, times(2)).save(any(Anteproyecto.class));
        verify(proyectoRepository, times(1)).save(proyecto);
    }

    @Test
    void subirAnteproyecto_ShouldSetCorrectDatesAndState() {
        // Arrange
        when(proyectoRepository.findByFormatoAActualTitleAndFormatoAActualState(
                "Proyecto de Grado Test", EnumEstado.APROBADO))
                .thenReturn(Optional.of(proyecto));

        when(anteproyectoRepository.save(any(Anteproyecto.class))).thenReturn(anteproyecto);
        when(proyectoRepository.save(any(ProyectoGrado.class))).thenReturn(proyecto);

        doNothing().when(rabbitMQPublisher).publicarAnteproyectoCreado(any(AnteproyectoResponse.class));
        doNothing().when(rabbitMQPublisher).publicarNotificacionAnteproyectoCreado(any(AnteproyectoNotification.class));

        // Act
        Anteproyecto result = anteproyectoService.subirAnteproyecto(anteproyecto);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getFechaCreacion());
        assertEquals(LocalDate.now(), result.getFechaCreacion());
        assertEquals(EnumEstadoAnteproyecto.ENTREGADO, result.getEstado());
    }

    @Test
    void subirAnteproyecto_ShouldEstablishBidirectionalRelationship() {
        // Arrange
        when(proyectoRepository.findByFormatoAActualTitleAndFormatoAActualState(
                "Proyecto de Grado Test", EnumEstado.APROBADO))
                .thenReturn(Optional.of(proyecto));

        // CORRECCIÓN: Usar thenAnswer para capturar el objeto guardado
        Anteproyecto[] anteproyectoGuardado = new Anteproyecto[1];
        when(anteproyectoRepository.save(any(Anteproyecto.class))).thenAnswer(invocation -> {
            anteproyectoGuardado[0] = invocation.getArgument(0);
            return anteproyectoGuardado[0];
        });

        when(proyectoRepository.save(any(ProyectoGrado.class))).thenReturn(proyecto);

        doNothing().when(rabbitMQPublisher).publicarAnteproyectoCreado(any(AnteproyectoResponse.class));
        doNothing().when(rabbitMQPublisher).publicarNotificacionAnteproyectoCreado(any(AnteproyectoNotification.class));

        // Act
        Anteproyecto result = anteproyectoService.subirAnteproyecto(anteproyecto);

        // Assert - Verificar después de que se complete el método
        assertNotNull(result);
        assertNotNull(result.getProyectoGrado());
        assertEquals(proyecto, result.getProyectoGrado());
        assertEquals(result, proyecto.getAnteproyecto()); // Verificar relación bidireccional

        verify(anteproyectoRepository, times(2)).save(any(Anteproyecto.class));
        verify(proyectoRepository, times(1)).save(proyecto);
    }

    @Test
    void subirAnteproyecto_ShouldSaveTwiceForRelationshipEstablishment() {
        // Arrange
        when(proyectoRepository.findByFormatoAActualTitleAndFormatoAActualState(
                "Proyecto de Grado Test", EnumEstado.APROBADO))
                .thenReturn(Optional.of(proyecto));

        // Contar cuántas veces se guarda
        final int[] saveCount = {0};
        when(anteproyectoRepository.save(any(Anteproyecto.class))).thenAnswer(invocation -> {
            saveCount[0]++;
            Anteproyecto saved = invocation.getArgument(0);

            // En el segundo guardado debería tener la relación establecida
            if (saveCount[0] == 2) {
                assertNotNull(saved.getProyectoGrado());
                assertEquals(proyecto, saved.getProyectoGrado());
            }
            return saved;
        });

        when(proyectoRepository.save(any(ProyectoGrado.class))).thenReturn(proyecto);
        doNothing().when(rabbitMQPublisher).publicarAnteproyectoCreado(any(AnteproyectoResponse.class));
        doNothing().when(rabbitMQPublisher).publicarNotificacionAnteproyectoCreado(any(AnteproyectoNotification.class));

        // Act
        Anteproyecto result = anteproyectoService.subirAnteproyecto(anteproyecto);

        // Assert
        assertNotNull(result);
        assertEquals(2, saveCount[0]); // Debe guardarse 2 veces
        verify(anteproyectoRepository, times(2)).save(any(Anteproyecto.class));
    }
}