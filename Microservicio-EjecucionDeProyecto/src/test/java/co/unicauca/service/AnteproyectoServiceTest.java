package co.unicauca.service;

import co.unicauca.entity.Anteproyecto;
import co.unicauca.entity.EnumEstadoAnteproyecto;
import co.unicauca.entity.ProyectoGrado;
import co.unicauca.entity.FormatoAVersion;
import co.unicauca.infra.dto.AnteproyectoRequest;
import co.unicauca.infra.dto.AnteproyectoResponse;
import co.unicauca.infra.memento.RequestHistoryManager;
import co.unicauca.infra.memento.RequestMemento;
import co.unicauca.repository.AnteproyectoRepository;
import co.unicauca.repository.ProyectoGradoRepository;
import co.unicauca.repository.FormatoAVersionRepository;
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
class AnteproyectoServiceTest {

    @Mock
    private AnteproyectoRepository anteproyectoRepository;

    @Mock
    private ProyectoGradoRepository proyectoGradoRepository;

    @Mock
    private FormatoAVersionRepository formatoAVersionRepository;

    @Mock
    private RequestHistoryManager historyManager;

    @InjectMocks
    private AnteproyectoService anteproyectoService;

    private AnteproyectoRequest request;
    private Anteproyecto anteproyecto;
    private ProyectoGrado proyecto;
    private FormatoAVersion formatoAVersion;

    @BeforeEach
    void setUp() {
        // Configurar request
        request = new AnteproyectoRequest(
                1L,
                "Título del Anteproyecto",
                LocalDate.now(),
                EnumEstadoAnteproyecto.ENTREGADO.name(),
                "Observaciones del anteproyecto",
                100L
        );

        // Configurar proyecto
        proyecto = new ProyectoGrado();
        proyecto.setId(100L);
        proyecto.setNombre("Proyecto de Grado");
        proyecto.setIdFormatoA(200L);

        // Configurar anteproyecto
        anteproyecto = new Anteproyecto();
        anteproyecto.setId(1L);
        anteproyecto.setTitulo("Título del Anteproyecto");
        anteproyecto.setFecha(LocalDate.now());
        anteproyecto.setEstado(EnumEstadoAnteproyecto.ENTREGADO);
        anteproyecto.setObservaciones("Observaciones del anteproyecto");
        anteproyecto.setProyectoGrado(proyecto);

        // Configurar formato A versión
        formatoAVersion = new FormatoAVersion();
        formatoAVersion.setId(200L);
        formatoAVersion.setTitle("Formato A Título");
        formatoAVersion.setNumeroVersion(1);
        formatoAVersion.setState(co.unicauca.entity.EnumEstado.ENTREGADO);
    }

    @Test
    void procesarAnteproyectoRequest_ConIdExistente_DeberiaActualizar() {
        // Arrange
        when(proyectoGradoRepository.findById(100L)).thenReturn(Optional.of(proyecto));
        when(anteproyectoRepository.findById(1L)).thenReturn(Optional.of(anteproyecto));
        when(anteproyectoRepository.save(any(Anteproyecto.class))).thenReturn(anteproyecto);
        when(historyManager.saveRequestState(anyString(), anyLong(), anyString(), anyMap()))
                .thenReturn(new RequestMemento("ANTEPROYECTO", 1L, "ENTREGADO", 1, new HashMap<>()));

        // Act
        anteproyectoService.procesarAnteproyectoRequest(request);

        // Assert
        verify(proyectoGradoRepository).findById(100L);
        verify(anteproyectoRepository).findById(1L);
        verify(anteproyectoRepository).save(any(Anteproyecto.class));
        verify(historyManager, times(2)).saveRequestState(anyString(), anyLong(), anyString(), anyMap());
    }

    @Test
    void procesarAnteproyectoRequest_ConProyectoExistente_DeberiaActualizarPorProyecto() {
        // Arrange
        AnteproyectoRequest requestSinId = new AnteproyectoRequest(
                null, "Título", LocalDate.now(), "ENTREGADO", "Obs", 100L
        );

        List<Anteproyecto> anteproyectos = Arrays.asList(anteproyecto);

        when(proyectoGradoRepository.findById(100L)).thenReturn(Optional.of(proyecto));
        when(anteproyectoRepository.findAllByProyectoGradoId(100L)).thenReturn(anteproyectos);
        when(anteproyectoRepository.save(any(Anteproyecto.class))).thenReturn(anteproyecto);
        when(historyManager.saveRequestState(anyString(), anyLong(), anyString(), anyMap()))
                .thenReturn(new RequestMemento("ANTEPROYECTO", 1L, "ENTREGADO", 1, new HashMap<>()));

        // Act
        anteproyectoService.procesarAnteproyectoRequest(requestSinId);

        // Assert
        verify(anteproyectoRepository).findAllByProyectoGradoId(100L);
        verify(anteproyectoRepository).save(any(Anteproyecto.class));
    }

    @Test
    void procesarAnteproyectoRequest_SinAnteproyectoExistente_DeberiaCrearNuevo() {
        // Arrange
        AnteproyectoRequest requestNuevo = new AnteproyectoRequest(
                null, "Nuevo Título", LocalDate.now(), "ENTREGADO", "Obs", 100L
        );

        when(proyectoGradoRepository.findById(100L)).thenReturn(Optional.of(proyecto));
        when(anteproyectoRepository.findAllByProyectoGradoId(100L)).thenReturn(new ArrayList<>());
        when(anteproyectoRepository.save(any(Anteproyecto.class))).thenReturn(anteproyecto);
        when(historyManager.saveRequestState(anyString(), anyLong(), anyString(), anyMap()))
                .thenReturn(new RequestMemento("ANTEPROYECTO", 1L, "ENTREGADO", 1, new HashMap<>()));

        // Act
        anteproyectoService.procesarAnteproyectoRequest(requestNuevo);

        // Assert
        verify(anteproyectoRepository).save(any(Anteproyecto.class));
        verify(historyManager).saveRequestState(anyString(), anyLong(), anyString(), anyMap());
    }

    @Test
    void crearAnteproyecto_DeberiaRetornarResponse() {
        // Arrange
        when(proyectoGradoRepository.findById(100L)).thenReturn(Optional.of(proyecto));
        when(anteproyectoRepository.save(any(Anteproyecto.class))).thenReturn(anteproyecto);
        when(historyManager.saveRequestState(anyString(), anyLong(), anyString(), anyMap()))
                .thenReturn(new RequestMemento("ANTEPROYECTO", 1L, "ENTREGADO", 1, new HashMap<>()));

        // Act
        AnteproyectoResponse resultado = anteproyectoService.crearAnteproyecto(request);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Título del Anteproyecto", resultado.titulo());
        verify(anteproyectoRepository).save(any(Anteproyecto.class));
    }

    @Test
    void actualizarAnteproyecto_DeberiaRetornarResponseActualizado() {
        // Arrange
        when(anteproyectoRepository.findById(1L)).thenReturn(Optional.of(anteproyecto));
        when(proyectoGradoRepository.findById(100L)).thenReturn(Optional.of(proyecto));
        when(anteproyectoRepository.save(any(Anteproyecto.class))).thenReturn(anteproyecto);
        when(historyManager.saveRequestState(anyString(), anyLong(), anyString(), anyMap()))
                .thenReturn(new RequestMemento("ANTEPROYECTO", 1L, "ENTREGADO", 1, new HashMap<>()));

        // Act
        AnteproyectoResponse resultado = anteproyectoService.actualizarAnteproyecto(1L, request);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        verify(anteproyectoRepository).findById(1L);
        verify(anteproyectoRepository).save(any(Anteproyecto.class));
    }

    @Test
    void actualizarAnteproyecto_ConAnteproyectoNoEncontrado_DeberiaLanzarExcepcion() {
        // Arrange
        when(anteproyectoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            anteproyectoService.actualizarAnteproyecto(1L, request);
        });
    }

    @Test
    void buscarPorId_ConIdExistente_DeberiaRetornarResponse() {
        // Arrange
        when(anteproyectoRepository.findById(1L)).thenReturn(Optional.of(anteproyecto));

        // Act
        AnteproyectoResponse resultado = anteproyectoService.buscarPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Título del Anteproyecto", resultado.titulo());
        verify(anteproyectoRepository).findById(1L);
    }

    @Test
    void buscarPorId_ConIdInexistente_DeberiaLanzarExcepcion() {
        // Arrange
        when(anteproyectoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            anteproyectoService.buscarPorId(1L);
        });
    }

    @Test
    void listarTodos_DeberiaRetornarListaResponses() {
        // Arrange
        List<Anteproyecto> anteproyectos = Arrays.asList(anteproyecto);
        when(anteproyectoRepository.findAll()).thenReturn(anteproyectos);

        // Act
        List<AnteproyectoResponse> resultados = anteproyectoService.listarTodos();

        // Assert
        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertEquals(1L, resultados.get(0).id());
        verify(anteproyectoRepository).findAll();
    }

    @Test
    void buscarPorProyecto_DeberiaRetornarListaResponses() {
        // Arrange
        List<Anteproyecto> anteproyectos = Arrays.asList(anteproyecto);
        when(anteproyectoRepository.findAllByProyectoGradoId(100L)).thenReturn(anteproyectos);

        // Act
        List<AnteproyectoResponse> resultados = anteproyectoService.buscarPorProyecto(100L);

        // Assert
        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertEquals(1L, resultados.get(0).id());
        verify(anteproyectoRepository).findAllByProyectoGradoId(100L);
    }

    @Test
    void mostrarRelacionCompleta_ConRelacionesExistentes_DeberiaMostrarInformacion() {
        // Arrange
        List<FormatoAVersion> formatoAVersions = Arrays.asList(formatoAVersion);

        when(anteproyectoRepository.findById(1L)).thenReturn(Optional.of(anteproyecto));
        when(formatoAVersionRepository.findByIdFormatoA(200L)).thenReturn(formatoAVersions);

        // Act
        anteproyectoService.mostrarRelacionCompleta(1L);

        // Assert
        verify(anteproyectoRepository).findById(1L);
        verify(formatoAVersionRepository).findByIdFormatoA(200L);
    }

    @Test
    void mostrarRelacionCompleta_ConAnteproyectoNoEncontrado_DeberiaLanzarExcepcion() {
        // Arrange
        when(anteproyectoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            anteproyectoService.mostrarRelacionCompleta(1L);
        });
    }

    @Test
    void mostrarRelacionPorProyecto_ConRelacionesExistentes_DeberiaMostrarInformacion() {
        // Arrange
        List<Anteproyecto> anteproyectos = Arrays.asList(anteproyecto);
        List<FormatoAVersion> formatoAVersions = Arrays.asList(formatoAVersion);

        when(proyectoGradoRepository.findById(100L)).thenReturn(Optional.of(proyecto));
        when(anteproyectoRepository.findAllByProyectoGradoId(100L)).thenReturn(anteproyectos);
        when(formatoAVersionRepository.findByIdFormatoA(200L)).thenReturn(formatoAVersions);

        // Act
        anteproyectoService.mostrarRelacionPorProyecto(100L);

        // Assert
        verify(proyectoGradoRepository).findById(100L);
        verify(anteproyectoRepository).findAllByProyectoGradoId(100L);
        verify(formatoAVersionRepository).findByIdFormatoA(200L);
    }

    @Test
    void mostrarRelacionPorProyecto_ConProyectoNoEncontrado_DeberiaLanzarExcepcion() {
        // Arrange
        when(proyectoGradoRepository.findById(100L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            anteproyectoService.mostrarRelacionPorProyecto(100L);
        });
    }

    @Test
    void listarTodasLasRelaciones_DeberiaMostrarInformacion() {
        // Arrange
        List<Anteproyecto> anteproyectos = Arrays.asList(anteproyecto);
        when(anteproyectoRepository.findAll()).thenReturn(anteproyectos);

        // Act
        anteproyectoService.listarTodasLasRelaciones();

        // Assert
        verify(anteproyectoRepository).findAll();
    }

    @Test
    void obtenerHistorialAnteproyecto_DeberiaRetornarHistorial() {
        // Arrange
        List<RequestMemento> historial = Arrays.asList(
                new RequestMemento("ANTEPROYECTO", 1L, "ENTREGADO", 1, new HashMap<>())
        );
        when(historyManager.getRequestHistory("ANTEPROYECTO", 1L)).thenReturn(historial);

        // Act
        List<RequestMemento> resultado = anteproyectoService.obtenerHistorialAnteproyecto(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(historyManager).getRequestHistory("ANTEPROYECTO", 1L);
    }

    @Test
    void obtenerEstadoAnteproyectoVersion_DeberiaRetornarMemento() {
        // Arrange
        RequestMemento memento = new RequestMemento("ANTEPROYECTO", 1L, "ENTREGADO", 1, new HashMap<>());
        when(historyManager.restoreToRequestVersion("ANTEPROYECTO", 1L, 1)).thenReturn(memento);

        // Act
        RequestMemento resultado = anteproyectoService.obtenerEstadoAnteproyectoVersion(1L, 1);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getVersion());
        verify(historyManager).restoreToRequestVersion("ANTEPROYECTO", 1L, 1);
    }

    @Test
    void restaurarAnteproyectoAVersion_DeberiaCrearNuevoAnteproyecto() {
        // Arrange
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("id", 1L);
        requestData.put("titulo", "Título Restaurado");
        requestData.put("fecha", LocalDate.now());
        requestData.put("estado", "ENTREGADO");
        requestData.put("observaciones", "Observaciones restauradas");
        requestData.put("idProyectoGrado", 100L);

        RequestMemento memento = new RequestMemento("ANTEPROYECTO", 1L, "ENTREGADO", 1, requestData);

        when(historyManager.restoreToRequestVersion("ANTEPROYECTO", 1L, 1)).thenReturn(memento);
        when(proyectoGradoRepository.findById(100L)).thenReturn(Optional.of(proyecto));
        when(anteproyectoRepository.save(any(Anteproyecto.class))).thenReturn(anteproyecto);
        when(historyManager.saveRequestState(anyString(), anyLong(), anyString(), anyMap()))
                .thenReturn(new RequestMemento("ANTEPROYECTO", 1L, "ENTREGADO", 1, new HashMap<>()));

        // Act
        Anteproyecto resultado = anteproyectoService.restaurarAnteproyectoAVersion(1L, 1);

        // Assert
        assertNotNull(resultado);
        verify(historyManager).restoreToRequestVersion("ANTEPROYECTO", 1L, 1);
        verify(anteproyectoRepository).save(any(Anteproyecto.class));
    }

    @Test
    void obtenerTodos_DeberiaRetornarListaResponses() {
        // Arrange
        List<Anteproyecto> anteproyectos = Arrays.asList(anteproyecto);
        when(anteproyectoRepository.findAll()).thenReturn(anteproyectos);

        // Act
        List<AnteproyectoResponse> resultados = anteproyectoService.obtenerTodos();

        // Assert
        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        verify(anteproyectoRepository).findAll();
    }

    @Test
    void existeAnteproyectoParaProyecto_ConAnteproyectos_DeberiaRetornarTrue() {
        // Arrange
        List<Anteproyecto> anteproyectos = Arrays.asList(anteproyecto);
        when(anteproyectoRepository.findAllByProyectoGradoId(100L)).thenReturn(anteproyectos);

        // Act
        boolean resultado = anteproyectoService.existeAnteproyectoParaProyecto(100L);

        // Assert
        assertTrue(resultado);
        verify(anteproyectoRepository).findAllByProyectoGradoId(100L);
    }

    @Test
    void existeAnteproyectoParaProyecto_SinAnteproyectos_DeberiaRetornarFalse() {
        // Arrange
        when(anteproyectoRepository.findAllByProyectoGradoId(100L)).thenReturn(new ArrayList<>());

        // Act
        boolean resultado = anteproyectoService.existeAnteproyectoParaProyecto(100L);

        // Assert
        assertFalse(resultado);
        verify(anteproyectoRepository).findAllByProyectoGradoId(100L);
    }

    @Test
    void crearAnteproyecto_ConDiferentesEstados_DeberiaFuncionarCorrectamente() {
        // Arrange
        AnteproyectoRequest requestAprobado = new AnteproyectoRequest(
                2L, "Título Aprobado", LocalDate.now(), "APROBADO", "Obs", 100L
        );

        Anteproyecto anteproyectoAprobado = new Anteproyecto();
        anteproyectoAprobado.setId(2L);
        anteproyectoAprobado.setTitulo("Título Aprobado");
        anteproyectoAprobado.setFecha(LocalDate.now());
        anteproyectoAprobado.setEstado(EnumEstadoAnteproyecto.APROBADO);
        anteproyectoAprobado.setObservaciones("Obs");
        anteproyectoAprobado.setProyectoGrado(proyecto);

        when(proyectoGradoRepository.findById(100L)).thenReturn(Optional.of(proyecto));
        when(anteproyectoRepository.save(any(Anteproyecto.class))).thenReturn(anteproyectoAprobado);
        when(historyManager.saveRequestState(anyString(), anyLong(), anyString(), anyMap()))
                .thenReturn(new RequestMemento("ANTEPROYECTO", 2L, "APROBADO", 1, new HashMap<>()));

        // Act
        AnteproyectoResponse resultado = anteproyectoService.crearAnteproyecto(requestAprobado);

        // Assert
        assertNotNull(resultado);
        assertEquals(2L, resultado.id());
        verify(anteproyectoRepository).save(any(Anteproyecto.class));
        verify(historyManager).saveRequestState(eq("ANTEPROYECTO"), eq(2L), eq("APROBADO"), anyMap());
    }
}