package co.unicauca.service;

import co.unicauca.entity.FormatoAVersion;
import co.unicauca.entity.EnumEstado;
import co.unicauca.entity.EnumModalidad;
import co.unicauca.entity.ProyectoGrado;
import co.unicauca.infra.dto.FormatoAVersionRequest;
import co.unicauca.infra.dto.FormatoAVersionResponse;
import co.unicauca.infra.memento.RequestHistoryManager;
import co.unicauca.infra.memento.RequestMemento;
import co.unicauca.repository.FormatoAVersionRepository;
import co.unicauca.repository.ProyectoGradoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FormatoAVersionServiceTest {

    @Mock
    private FormatoAVersionRepository versionRepository;

    @Mock
    private ProyectoGradoRepository proyectoGradoRepository;

    @Mock
    private RequestHistoryManager historyManager;

    @InjectMocks
    private FormatoAVersionService formatoAVersionService;

    private FormatoAVersionRequest request;
    private FormatoAVersion version;
    private ProyectoGrado proyecto;

    @BeforeEach
    void setUp() {
        // Usar los valores REALES de los enums
        request = new FormatoAVersionRequest(
                1L,
                1,
                LocalDate.now(),
                "Título del Proyecto",
                EnumModalidad.INVESTIGACION.name(), // Cambiado de PRESENCIAL a INVESTIGACION
                EnumEstado.ENTREGADO.name(),
                "Observaciones iniciales",
                1,
                100L
        );

        version = new FormatoAVersion();
        version.setId(1L);
        version.setNumeroVersion(1);
        version.setFecha(LocalDate.now());
        version.setTitle("Título del Proyecto");
        version.setMode(EnumModalidad.INVESTIGACION); // Cambiado de PRESENCIAL a INVESTIGACION
        version.setState(EnumEstado.ENTREGADO);
        version.setObservations("Observaciones iniciales");
        version.setCounter(1);
        version.setIdFormatoA(100L);

        proyecto = new ProyectoGrado();
        proyecto.setId(1L);
        proyecto.setIdFormatoA(100L);
    }

    @Test
    void crearVersion_ConIdExistente_DeberiaActualizarVersion() {
        // Arrange
        when(versionRepository.findById(1L)).thenReturn(Optional.of(version));
        when(versionRepository.save(any(FormatoAVersion.class))).thenReturn(version);
        when(historyManager.saveRequestState(anyString(), anyLong(), anyString(), anyMap()))
                .thenReturn(new RequestMemento("FORMATO_A", 1L, EnumEstado.ENTREGADO.name(), 1, new HashMap<>()));

        // Act
        FormatoAVersion resultado = formatoAVersionService.crearVersion(request);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(versionRepository).findById(1L);
        verify(versionRepository).save(version);
        verify(historyManager).saveRequestState(anyString(), anyLong(), anyString(), anyMap());
    }

    @Test
    void crearVersion_SinId_DeberiaLanzarExcepcion() {
        // Arrange
        FormatoAVersionRequest requestSinId = new FormatoAVersionRequest(
                null, 1, LocalDate.now(), "Título",
                EnumModalidad.INVESTIGACION.name(), EnumEstado.ENTREGADO.name(), "Obs", 1, 100L
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            formatoAVersionService.crearVersion(requestSinId);
        });
    }

    @Test
    void procesarVersionRecibida_ConVersionesExistentes_DeberiaCrearNuevaVersion() {
        // Arrange
        List<FormatoAVersion> versiones = Arrays.asList(version);
        when(versionRepository.findByIdFormatoAOrderByNumeroVersionDesc(100L)).thenReturn(versiones);
        when(versionRepository.save(any(FormatoAVersion.class))).thenReturn(version);
        when(versionRepository.findMaxId()).thenReturn(10L);
        when(historyManager.saveRequestState(anyString(), anyLong(), anyString(), anyMap()))
                .thenReturn(new RequestMemento("FORMATO_A", 2L, EnumEstado.ENTREGADO.name(), 1, new HashMap<>()));

        // Act
        formatoAVersionService.procesarVersionRecibida(request);

        // Assert
        verify(versionRepository).findByIdFormatoAOrderByNumeroVersionDesc(100L);
        verify(versionRepository).save(any(FormatoAVersion.class));
        verify(historyManager).saveRequestState(anyString(), anyLong(), anyString(), anyMap());
    }

    @Test
    void procesarVersionRecibida_SinVersionesExistentes_DeberiaCrearVersionInicial() {
        // Arrange
        when(versionRepository.findByIdFormatoAOrderByNumeroVersionDesc(100L)).thenReturn(new ArrayList<>());
        when(versionRepository.save(any(FormatoAVersion.class))).thenReturn(version);
        // No usar findMaxId() si no es necesario para evitar UnnecessaryStubbing
        when(historyManager.saveRequestState(anyString(), anyLong(), anyString(), anyMap()))
                .thenReturn(new RequestMemento("FORMATO_A", 1L, EnumEstado.ENTREGADO.name(), 1, new HashMap<>()));

        // Act
        formatoAVersionService.procesarVersionRecibida(request);

        // Assert
        verify(versionRepository).findByIdFormatoAOrderByNumeroVersionDesc(100L);
        verify(versionRepository).save(any(FormatoAVersion.class));
        verify(historyManager).saveRequestState(anyString(), anyLong(), anyString(), anyMap());
    }

    @Test
    void buscarPorId_ConIdExistente_DeberiaRetornarVersion() {
        // Arrange
        when(versionRepository.findById(1L)).thenReturn(Optional.of(version));

        // Act
        FormatoAVersionResponse resultado = formatoAVersionService.buscarPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals(1, resultado.numVersion());
        verify(versionRepository).findById(1L);
    }

    @Test
    void buscarPorId_ConIdInexistente_DeberiaLanzarExcepcion() {
        // Arrange
        when(versionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            formatoAVersionService.buscarPorId(1L);
        });
    }

    @Test
    void actualizarVersion_DeberiaCrearNuevaVersion() {
        // Arrange
        FormatoAVersion nuevaVersion = new FormatoAVersion();
        nuevaVersion.setId(2L);
        nuevaVersion.setNumeroVersion(2);
        nuevaVersion.setMode(EnumModalidad.INVESTIGACION); // Asegurar que mode no sea null
        nuevaVersion.setState(EnumEstado.ENTREGADO); // Asegurar que state no sea null

        when(versionRepository.findById(1L)).thenReturn(Optional.of(version));
        when(versionRepository.save(any(FormatoAVersion.class))).thenReturn(nuevaVersion);
        when(versionRepository.findMaxId()).thenReturn(10L);
        when(historyManager.saveRequestState(anyString(), anyLong(), anyString(), anyMap()))
                .thenReturn(new RequestMemento("FORMATO_A", 2L, EnumEstado.ENTREGADO.name(), 1, new HashMap<>()));

        // Act
        FormatoAVersionResponse resultado = formatoAVersionService.actualizarVersion(1L, request);

        // Assert
        assertNotNull(resultado);
        verify(versionRepository).findById(1L);
        verify(versionRepository).save(any(FormatoAVersion.class));
        verify(historyManager).saveRequestState(anyString(), anyLong(), anyString(), anyMap());
    }

    @Test
    void obtenerHistorialVersiones_DeberiaRetornarHistorial() {
        // Arrange
        List<RequestMemento> historial = Arrays.asList(
                new RequestMemento("FORMATO_A", 100L, EnumEstado.ENTREGADO.name(), 1, new HashMap<>())
        );
        when(historyManager.getRequestHistory("FORMATO_A", 100L)).thenReturn(historial);

        // Act
        List<RequestMemento> resultado = formatoAVersionService.obtenerHistorialVersiones(100L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(historyManager).getRequestHistory("FORMATO_A", 100L);
    }

    @Test
    void obtenerEstadoVersion_DeberiaRetornarMemento() {
        // Arrange
        RequestMemento memento = new RequestMemento("FORMATO_A", 100L, EnumEstado.ENTREGADO.name(), 1, new HashMap<>());
        when(historyManager.restoreToRequestVersion("FORMATO_A", 100L, 1)).thenReturn(memento);

        // Act
        RequestMemento resultado = formatoAVersionService.obtenerEstadoVersion(100L, 1);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getVersion());
        verify(historyManager).restoreToRequestVersion("FORMATO_A", 100L, 1);
    }

    @Test
    void obtenerUltimoEstado_ConHistorial_DeberiaRetornarUltimoMemento() {
        // Arrange
        RequestMemento memento = new RequestMemento("FORMATO_A", 100L, EnumEstado.APROBADO.name(), 2, new HashMap<>());
        when(historyManager.getLastRequest("FORMATO_A", 100L)).thenReturn(memento);

        // Act
        RequestMemento resultado = formatoAVersionService.obtenerUltimoEstado(100L);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.getVersion());
        assertEquals(EnumEstado.APROBADO.name(), resultado.getEstado());
        verify(historyManager).getLastRequest("FORMATO_A", 100L);
    }

    @Test
    void obtenerUltimoEstado_SinHistorial_DeberiaRetornarNull() {
        // Arrange
        when(historyManager.getLastRequest("FORMATO_A", 100L)).thenReturn(null);

        // Act
        RequestMemento resultado = formatoAVersionService.obtenerUltimoEstado(100L);

        // Assert
        assertNull(resultado);
        verify(historyManager).getLastRequest("FORMATO_A", 100L);
    }

    @Test
    void restaurarAVersion_DeberiaCrearNuevaVersionRestaurada() {
        // Arrange
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("id", 1L);
        requestData.put("numVersion", 1);
        requestData.put("fecha", LocalDate.now());
        requestData.put("titulo", "Título Restaurado");
        requestData.put("modalidad", EnumModalidad.INVESTIGACION.name());
        requestData.put("estado", EnumEstado.ENTREGADO.name());
        requestData.put("observaciones", "Observaciones restauradas");
        requestData.put("counter", 1);
        requestData.put("idFormatoA", 100L);

        RequestMemento memento = new RequestMemento("FORMATO_A", 100L, EnumEstado.ENTREGADO.name(), 1, requestData);

        List<FormatoAVersion> versiones = Arrays.asList(version);
        when(historyManager.restoreToRequestVersion("FORMATO_A", 100L, 1)).thenReturn(memento);
        when(versionRepository.findByIdFormatoAOrderByNumeroVersionDesc(100L)).thenReturn(versiones);
        when(versionRepository.save(any(FormatoAVersion.class))).thenReturn(version);
        when(historyManager.saveRequestState(anyString(), anyLong(), anyString(), anyMap()))
                .thenReturn(new RequestMemento("FORMATO_A", 2L, EnumEstado.ENTREGADO.name(), 1, new HashMap<>()));

        // Act
        FormatoAVersion resultado = formatoAVersionService.restaurarAVersion(100L, 1);

        // Assert
        assertNotNull(resultado);
        verify(historyManager).restoreToRequestVersion("FORMATO_A", 100L, 1);
        verify(versionRepository).save(any(FormatoAVersion.class));
        verify(historyManager).saveRequestState(anyString(), anyLong(), anyString(), anyMap());
    }

    @Test
    void buscarPorFormatoA_DeberiaRetornarListaVersiones() {
        // Arrange
        List<FormatoAVersion> versiones = Arrays.asList(version);
        when(versionRepository.findByIdFormatoAOrderByNumeroVersionDesc(100L)).thenReturn(versiones);

        // Act
        List<FormatoAVersion> resultado = formatoAVersionService.buscarPorFormatoA(100L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(versionRepository).findByIdFormatoAOrderByNumeroVersionDesc(100L);
    }

    @Test
    void buscarUltimaVersionPorFormatoA_ConVersiones_DeberiaRetornarUltimaVersion() {
        // Arrange
        List<FormatoAVersion> versiones = Arrays.asList(version);
        when(versionRepository.findByIdFormatoAOrderByNumeroVersionDesc(100L)).thenReturn(versiones);

        // Act
        Optional<FormatoAVersion> resultado = formatoAVersionService.buscarUltimaVersionPorFormatoA(100L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verify(versionRepository).findByIdFormatoAOrderByNumeroVersionDesc(100L);
    }

    @Test
    void buscarUltimaVersionPorFormatoA_SinVersiones_DeberiaRetornarEmpty() {
        // Arrange
        when(versionRepository.findByIdFormatoAOrderByNumeroVersionDesc(100L)).thenReturn(new ArrayList<>());

        // Act
        Optional<FormatoAVersion> resultado = formatoAVersionService.buscarUltimaVersionPorFormatoA(100L);

        // Assert
        assertFalse(resultado.isPresent());
        verify(versionRepository).findByIdFormatoAOrderByNumeroVersionDesc(100L);
    }

    @Test
    void obtenerHistorialCompletoPorFormatoA_DeberiaRetornarHistorialOrdenado() {
        // Arrange
        List<FormatoAVersion> versiones = Arrays.asList(version);
        when(versionRepository.findByIdFormatoAOrderByNumeroVersionAsc(100L)).thenReturn(versiones);

        // Act
        List<FormatoAVersion> resultado = formatoAVersionService.obtenerHistorialCompletoPorFormatoA(100L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(versionRepository).findByIdFormatoAOrderByNumeroVersionAsc(100L);
    }

    @Test
    void findByProyectoId_ConProyectoYFormatoA_DeberiaRetornarUltimaVersion() {
        // Arrange
        when(proyectoGradoRepository.findById(1L)).thenReturn(Optional.of(proyecto));
        List<FormatoAVersion> versiones = Arrays.asList(version);
        when(versionRepository.findByIdFormatoAOrderByNumeroVersionDesc(100L)).thenReturn(versiones);

        // Act
        FormatoAVersion resultado = formatoAVersionService.findByProyectoId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(proyectoGradoRepository).findById(1L);
        verify(versionRepository).findByIdFormatoAOrderByNumeroVersionDesc(100L);
    }

    @Test
    void findByProyectoId_ConProyectoSinFormatoA_DeberiaLanzarExcepcion() {
        // Arrange
        proyecto.setIdFormatoA(null);
        when(proyectoGradoRepository.findById(1L)).thenReturn(Optional.of(proyecto));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            formatoAVersionService.findByProyectoId(1L);
        });
    }

    @Test
    void findByProyectoId_ConProyectoInexistente_DeberiaLanzarExcepcion() {
        // Arrange
        when(proyectoGradoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            formatoAVersionService.findByProyectoId(1L);
        });
    }

    @Test
    void findByProyectoId_SinVersiones_DeberiaLanzarExcepcion() {
        // Arrange
        when(proyectoGradoRepository.findById(1L)).thenReturn(Optional.of(proyecto));
        when(versionRepository.findByIdFormatoAOrderByNumeroVersionDesc(100L)).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            formatoAVersionService.findByProyectoId(1L);
        });
    }

    @Test
    void crearVersion_ConIdNuevo_DeberiaCrearNuevaVersion() {
        // Arrange
        when(versionRepository.findById(2L)).thenReturn(Optional.empty());
        when(versionRepository.save(any(FormatoAVersion.class))).thenReturn(version);
        when(historyManager.saveRequestState(anyString(), anyLong(), anyString(), anyMap()))
                .thenReturn(new RequestMemento("FORMATO_A", 2L, EnumEstado.ENTREGADO.name(), 1, new HashMap<>()));

        FormatoAVersionRequest requestNuevo = new FormatoAVersionRequest(
                2L, 1, LocalDate.now(), "Nuevo Título",
                EnumModalidad.INVESTIGACION.name(), EnumEstado.ENTREGADO.name(), "Obs", 1, 100L
        );

        // Act
        FormatoAVersion resultado = formatoAVersionService.crearVersion(requestNuevo);

        // Assert
        assertNotNull(resultado);
        verify(versionRepository).findById(2L);
        verify(versionRepository).save(any(FormatoAVersion.class));
        verify(historyManager).saveRequestState(anyString(), anyLong(), anyString(), anyMap());
    }

    @Test
    void crearVersion_ConDiferentesModalidades_DeberiaFuncionarCorrectamente() {
        // Arrange
        FormatoAVersionRequest requestPractica = new FormatoAVersionRequest(
                3L, 1, LocalDate.now(), "Título Práctica",
                EnumModalidad.PRACTICA_PROFESIONAL.name(), EnumEstado.APROBADO.name(), "Obs", 1, 100L
        );

        FormatoAVersion versionPractica = new FormatoAVersion();
        versionPractica.setId(3L);
        versionPractica.setNumeroVersion(1);
        versionPractica.setFecha(LocalDate.now());
        versionPractica.setTitle("Título Práctica");
        versionPractica.setMode(EnumModalidad.PRACTICA_PROFESIONAL);
        versionPractica.setState(EnumEstado.APROBADO);
        versionPractica.setObservations("Obs");
        versionPractica.setCounter(1);
        versionPractica.setIdFormatoA(100L);

        when(versionRepository.findById(3L)).thenReturn(Optional.empty());
        when(versionRepository.save(any(FormatoAVersion.class))).thenReturn(versionPractica);
        when(historyManager.saveRequestState(anyString(), anyLong(), anyString(), anyMap()))
                .thenReturn(new RequestMemento("FORMATO_A", 3L, EnumEstado.APROBADO.name(), 1, new HashMap<>()));

        // Act
        FormatoAVersion resultado = formatoAVersionService.crearVersion(requestPractica);

        // Assert
        assertNotNull(resultado);
        verify(versionRepository).findById(3L);
        verify(versionRepository).save(any(FormatoAVersion.class));
        verify(historyManager).saveRequestState(eq("FORMATO_A"), eq(3L), eq(EnumEstado.APROBADO.name()), anyMap());
    }
}