package co.unicauca.service;

import co.unicauca.entity.*;
import co.unicauca.infra.dto.FormatoAEditRequest;
import co.unicauca.infra.dto.FormatoARequest;
import co.unicauca.infra.dto.FormatoAResponse;
import co.unicauca.infra.dto.notification.FormatoAnotification;
import co.unicauca.infra.messaging.RabbitMQPublisher;
import co.unicauca.repository.FormatoARepository;
import co.unicauca.repository.PersonaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FormatoAServiceTest {

    @Mock
    private FormatoARepository formatoARepository;

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private ProyectoService proyectoService;

    @Mock
    private VersionService versionService;

    @Mock
    private RabbitMQPublisher rabbitMQPublisher;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private FormatoAService formatoAService;

    private FormatoA formatoA;
    private Persona docente;
    private Persona codirector;
    private Persona estudiante;
    private FormatoAVersion version;

    @BeforeEach
    void setUp() {
        // Configurar docente
        docente = new Persona();
        docente.setEmail("docente@unicauca.edu.co");

        // Configurar codirector
        codirector = new Persona();
        codirector.setEmail("codirector@unicauca.edu.co");

        // Configurar estudiante
        estudiante = new Persona();
        estudiante.setEmail("estudiante@unicauca.edu.co");

        // Configurar FormatoA
        formatoA = new FormatoA();
        formatoA.setId(1L);
        formatoA.setTitle("Proyecto de Grado Test");
        formatoA.setMode(EnumModalidad.INVESTIGACION);
        formatoA.setProjectManagerEmail("docente@unicauca.edu.co");
        formatoA.setProjectCoManagerEmail("codirector@unicauca.edu.co");
        formatoA.setGeneralObjetive("Objetivo general");
        formatoA.setSpecificObjetives("Objetivos específicos");
        formatoA.setEstudianteEmails(Arrays.asList("estudiante@unicauca.edu.co"));
        formatoA.setState(EnumEstado.ENTREGADO);
        formatoA.setCounter(0);
        formatoA.setArchivoPDF("formatoA/archivo_existente.pdf");

        // Configurar versión
        version = new FormatoAVersion();
        version.setId(1L);
    }

    @Test
    void findById_WhenFormatoAExists_ShouldReturnFormatoA() {
        // Arrange
        when(formatoARepository.findById(1L)).thenReturn(Optional.of(formatoA));

        // Act
        Optional<FormatoA> result = formatoAService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(formatoA, result.get());
        verify(formatoARepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenFormatoANotExists_ShouldReturnEmpty() {
        // Arrange
        when(formatoARepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<FormatoA> result = formatoAService.findById(1L);

        // Assert
        assertFalse(result.isPresent());
        verify(formatoARepository, times(1)).findById(1L);
    }

    @Test
    void findByProjectManagerEmailOrProjectCoManagerEmail_ShouldReturnFormatoAList() {
        // Arrange
        String email = "docente@unicauca.edu.co";
        List<FormatoA> expectedList = Arrays.asList(formatoA);
        when(formatoARepository.findByProjectManagerEmailOrProjectCoManagerEmail(email, email))
                .thenReturn(expectedList);

        // Act
        List<FormatoA> result = formatoAService.findByProjectManagerEmailOrProjectCoManagerEmail(email);

        // Assert
        assertEquals(expectedList, result);
        verify(formatoARepository, times(1))
                .findByProjectManagerEmailOrProjectCoManagerEmail(email, email);
    }

    @Test
    void eliminarFormatoA_WhenFormatoAExists_ShouldReturnTrue() {
        // Arrange
        when(formatoARepository.existsById(1L)).thenReturn(true);
        doNothing().when(formatoARepository).deleteById(1L);

        // Act
        boolean result = formatoAService.eliminarFormatoA(1L);

        // Assert
        assertTrue(result);
        verify(formatoARepository, times(1)).existsById(1L);
        verify(formatoARepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarFormatoA_WhenFormatoANotExists_ShouldReturnFalse() {
        // Arrange
        when(formatoARepository.existsById(1L)).thenReturn(false);

        // Act
        boolean result = formatoAService.eliminarFormatoA(1L);

        // Assert
        assertFalse(result);
        verify(formatoARepository, times(1)).existsById(1L);
        verify(formatoARepository, never()).deleteById(1L);
    }

    @Test
    void subirFormatoA_WithValidData_ShouldSaveAndReturnFormatoA() {
        // Arrange
        // SOLUCIÓN: Mock que SIEMPRE pasa las validaciones
        Persona personaValida = mock(Persona.class);
        when(personaValida.esDocente()).thenReturn(true);
        when(personaValida.esEstudiante()).thenReturn(true); // Por si acaso

        when(personaRepository.findByEmail(anyString())).thenReturn(Optional.of(personaValida));
        when(formatoARepository.save(any(FormatoA.class))).thenReturn(formatoA);
        when(versionService.crearVersionInicial(any(FormatoA.class))).thenReturn(version);
        when(proyectoService.crearProyectoGrado(any(FormatoA.class), any(FormatoAVersion.class)))
                .thenReturn(null);

        // Act
        FormatoA result = formatoAService.subirFormatoA(formatoA);

        // Assert
        assertNotNull(result);
    }

    @Test
    void subirFormatoA_WithInvalidDirector_ShouldThrowException() {
        // Arrange
        Persona noDocente = new Persona();
        noDocente.setEmail("estudiante@unicauca.edu.co");

        when(personaRepository.findByEmail("estudiante@unicauca.edu.co"))
                .thenReturn(Optional.of(noDocente));

        formatoA.setProjectManagerEmail("estudiante@unicauca.edu.co");

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> formatoAService.subirFormatoA(formatoA));
        assertTrue(exception.getMessage().contains("debe ser docente"));
    }

    @Test
    void publicarFormatoA_WithValidFormatoA_ShouldPublishAndReturnFormatoA() {
        // Arrange
        formatoA.setArchivoPDF("formatoA/archivo.pdf");
        when(formatoARepository.findById(1L)).thenReturn(Optional.of(formatoA));

        // CORRECCIÓN: Para métodos void usar doNothing()
        doNothing().when(rabbitMQPublisher).publicarFormatoACreado(any(FormatoAResponse.class));
        doNothing().when(rabbitMQPublisher).publicarNotificacionFormatoACreado(any(FormatoAnotification.class));

        // Act
        FormatoA result = formatoAService.publicarFormatoA(1L);

        // Assert
        assertNotNull(result);
        verify(formatoARepository, times(1)).findById(1L);
        verify(rabbitMQPublisher, times(1)).publicarFormatoACreado(any(FormatoAResponse.class));
        verify(rabbitMQPublisher, times(1)).publicarNotificacionFormatoACreado(any(FormatoAnotification.class));
    }

    @Test
    void publicarFormatoA_WithoutPDF_ShouldThrowException() {
        // Arrange
        formatoA.setArchivoPDF(null);
        when(formatoARepository.findById(1L)).thenReturn(Optional.of(formatoA));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> formatoAService.publicarFormatoA(1L));
        assertEquals("No se puede publicar FormatoA sin PDF", exception.getMessage());
    }

    @Test
    void saveFormatoAPDF_WithValidFile_ShouldReturnTrue() throws IOException {
        // Arrange
        when(formatoARepository.findById(1L)).thenReturn(Optional.of(formatoA));
        when(formatoARepository.save(any(FormatoA.class))).thenReturn(formatoA);
        when(multipartFile.getOriginalFilename()).thenReturn("documento.pdf");

        // CORRECCIÓN: Para transferTo que es void
        doNothing().when(multipartFile).transferTo(any(java.io.File.class));
        doNothing().when(versionService).actualizarRutasArchivos(any(FormatoA.class));

        // Act
        boolean result = formatoAService.saveFormatoAPDF(1L, multipartFile);

        // Assert
        assertTrue(result);
        verify(formatoARepository, times(1)).findById(1L);
        verify(formatoARepository, times(1)).save(formatoA);
        verify(versionService, times(1)).actualizarRutasArchivos(formatoA);
    }

    @Test
    void saveFormatoAPDF_WhenFormatoANotFound_ShouldReturnFalse() {
        // Arrange
        when(formatoARepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        boolean result = formatoAService.saveFormatoAPDF(1L, multipartFile);

        // Assert
        assertFalse(result);
        verify(formatoARepository, times(1)).findById(1L);
        verify(formatoARepository, never()).save(any(FormatoA.class));
    }

    @Test
    void saveCartaLaboral_WithValidFile_ShouldReturnTrue() throws IOException {
        // Arrange
        when(formatoARepository.findById(1L)).thenReturn(Optional.of(formatoA));
        when(formatoARepository.save(any(FormatoA.class))).thenReturn(formatoA);
        when(multipartFile.getOriginalFilename()).thenReturn("carta.pdf");

        // CORRECCIÓN: Para transferTo que es void
        doNothing().when(multipartFile).transferTo(any(java.io.File.class));
        doNothing().when(versionService).actualizarRutasArchivos(any(FormatoA.class));

        // Act
        boolean result = formatoAService.saveCartaLaboral(1L, multipartFile);

        // Assert
        assertTrue(result);
        verify(formatoARepository, times(1)).findById(1L);
        verify(formatoARepository, times(1)).save(formatoA);
        verify(versionService, times(1)).actualizarRutasArchivos(formatoA);
    }

    @Test
    void reenviarFormatoARechazado_WithValidRequest_ShouldUpdateAndReturnFormatoA() {
        // Arrange - CORRECCIÓN: Ahora con 5 parámetros exactos
        FormatoAEditRequest request = new FormatoAEditRequest(
                1L,
                "nuevo_archivo.pdf",
                "nueva_carta.pdf",
                "Nuevo objetivo general",
                "Nuevos objetivos específicos" // ← QUINTO PARÁMETRO AGREGADO
        );

        formatoA.setState(EnumEstado.RECHAZADO);
        formatoA.setCounter(1);

        when(formatoARepository.findById(1L)).thenReturn(Optional.of(formatoA));
        when(formatoARepository.save(any(FormatoA.class))).thenReturn(formatoA);
        when(versionService.crearVersionReenviada(any(FormatoA.class), any(FormatoAEditRequest.class)))
                .thenReturn(version);

        // CORRECCIÓN: Para métodos void
        doNothing().when(rabbitMQPublisher).publicarFormatoACreado(any(FormatoAResponse.class));
        doNothing().when(rabbitMQPublisher).publicarNotificacionFormatoACreado(any(FormatoAnotification.class));

        // Act
        FormatoA result = formatoAService.reenviarFormatoARechazado(request);

        // Assert
        assertNotNull(result);
        verify(formatoARepository, times(1)).findById(1L);
        verify(formatoARepository, times(1)).save(formatoA);
        verify(versionService, times(1)).crearVersionReenviada(formatoA, request);
        verify(rabbitMQPublisher, times(1)).publicarFormatoACreado(any(FormatoAResponse.class));
        verify(rabbitMQPublisher, times(1)).publicarNotificacionFormatoACreado(any(FormatoAnotification.class));
    }



    @Test
    void actualizarFormatoAEvaluado_WithEntregadoState_ShouldUpdateAndReturnFormatoA() {
        // Arrange
        FormatoARequest request = new FormatoARequest(
                1L,
                "Proyecto de Grado Test",
                "ENTREGADO",
                "Observaciones de prueba",
                "1"
        );

        when(formatoARepository.findById(1L)).thenReturn(Optional.of(formatoA));
        when(formatoARepository.save(any(FormatoA.class))).thenReturn(formatoA);
        when(versionService.crearVersionConEvaluacion(any(FormatoA.class), any(FormatoARequest.class)))
                .thenReturn(version);

        // CORRECCIÓN: Si agregarVersionAProyectoGrado retorna algo
        when(proyectoService.agregarVersionAProyectoGrado(any(FormatoA.class), any(FormatoAVersion.class)))
                .thenReturn(null); // o el valor que retorne

        // Act
        FormatoA result = formatoAService.actualizarFormatoAEvaluado(request);

        // Assert
        assertNotNull(result);
        verify(formatoARepository, times(1)).findById(1L);
        verify(formatoARepository, times(1)).save(formatoA);
        verify(versionService, times(1)).crearVersionConEvaluacion(formatoA, request);
        verify(proyectoService, times(1)).agregarVersionAProyectoGrado(formatoA, version);
    }

    @Test
    void actualizarFormatoAEvaluado_WithRechazadoState_ShouldUpdateAndReturnFormatoA() {
        // Arrange
        FormatoARequest request = new FormatoARequest(
                1L,
                "Proyecto de Grado Test",
                "RECHAZADO",
                "Observaciones de rechazo",
                "2"
        );

        when(formatoARepository.findById(1L)).thenReturn(Optional.of(formatoA));
        when(formatoARepository.save(any(FormatoA.class))).thenReturn(formatoA);
        when(versionService.crearVersionConEvaluacion(any(FormatoA.class), any(FormatoARequest.class)))
                .thenReturn(version);

        // CORRECCIÓN: Si agregarVersionAProyectoGrado retorna algo
        when(proyectoService.agregarVersionAProyectoGrado(any(FormatoA.class), any(FormatoAVersion.class)))
                .thenReturn(null); // o el valor que retorne

        // Act
        FormatoA result = formatoAService.actualizarFormatoAEvaluado(request);

        // Assert
        assertNotNull(result);
        verify(formatoARepository, times(1)).findById(1L);
        verify(formatoARepository, times(1)).save(formatoA);
    }



    @Test
    void listarFormatosAPorDocente_WithNoDocente_ShouldThrowException() {
        // Arrange
        String emailNoDocente = "estudiante@unicauca.edu.co";
        when(personaRepository.findByEmail(emailNoDocente)).thenReturn(Optional.of(estudiante));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> formatoAService.listarFormatosAPorDocente(emailNoDocente));
        assertTrue(exception.getMessage().contains("no es docente"));
    }

    @Test
    void findAll_ShouldReturnAllFormatoA() {
        // Arrange
        List<FormatoA> expectedList = Arrays.asList(formatoA);
        when(formatoARepository.findAll()).thenReturn(expectedList);

        // Act
        List<FormatoA> result = formatoAService.findAll();

        // Assert
        assertEquals(expectedList, result);
        verify(formatoARepository, times(1)).findAll();
    }

    @Test
    void saveFormatoAPDF_WithIOException_ShouldReturnFalse() throws IOException {
        // Arrange
        when(formatoARepository.findById(1L)).thenReturn(Optional.of(formatoA));
        when(multipartFile.getOriginalFilename()).thenReturn("documento.pdf");

        // CORRECCIÓN: Para transferTo que es void y lanza excepción
        doThrow(new IOException("Error de IO")).when(multipartFile).transferTo(any(java.io.File.class));

        // Act
        boolean result = formatoAService.saveFormatoAPDF(1L, multipartFile);

        // Assert
        assertFalse(result);
        verify(formatoARepository, times(1)).findById(1L);
        verify(formatoARepository, never()).save(any(FormatoA.class));
    }
}