package co.unicauca.service;

import co.unicauca.entity.ProyectoGrado;
import co.unicauca.infra.dto.ProyectoGradoRequest;
import co.unicauca.infra.dto.ProyectoGradoResponse;
import co.unicauca.infra.memento.RequestHistoryManager;
import co.unicauca.infra.memento.RequestMemento;
import co.unicauca.repository.ProyectoGradoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProyectoGradoServiceTest {

    @Mock
    private ProyectoGradoRepository proyectoRepository;

    @Mock
    private RequestHistoryManager historyManager;

    @InjectMocks
    private ProyectoGradoService proyectoService;

    private ProyectoGradoRequest request;
    private ProyectoGrado proyecto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        request = new ProyectoGradoRequest(
                null,
                "Proyecto de prueba",
                LocalDate.now(),
                List.of("estudiante1@uni.edu"),
                123L
        );

        proyecto = new ProyectoGrado();
        proyecto.setId(1L);
        proyecto.setNombre(request.nombre());
        proyecto.setFecha(request.fecha().atStartOfDay());
        proyecto.setEstudiantesEmail(request.estudiantesEmail());
        proyecto.setIdFormatoA(request.IdFormatoA());
        proyecto.setEstado("ENTREGADO");
    }

    @Test
    void crearProyecto_insertaProyectoYGuardaMemento() {
        when(proyectoRepository.save(any(ProyectoGrado.class))).thenReturn(proyecto);
        when(historyManager.saveRequestState(anyString(), anyLong(), anyString(), anyMap()))
                .thenReturn(new RequestMemento("PROYECTO_GRADO", 1L, "ENTREGADO", 1, new HashMap<>()));

        ProyectoGradoResponse response = proyectoService.crearProyecto(request);

        assertNotNull(response);
        assertEquals(proyecto.getNombre(), response.nombre());
        verify(proyectoRepository, times(1)).save(any(ProyectoGrado.class));
        verify(historyManager, times(1)).saveRequestState(anyString(), anyLong(), anyString(), anyMap());
    }

    @Test
    void actualizarProyecto_actualizaCamposYGuardaMemento() {
        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));
        when(proyectoRepository.save(any(ProyectoGrado.class))).thenReturn(proyecto);
        when(historyManager.saveRequestState(anyString(), anyLong(), anyString(), anyMap()))
                .thenReturn(new RequestMemento("PROYECTO_GRADO", 1L, "ENTREGADO", 1, new HashMap<>()));

        ProyectoGradoResponse response = proyectoService.actualizarProyecto(1L, request);

        assertNotNull(response);
        assertEquals(proyecto.getNombre(), response.nombre());
        // Cambiado a 1 vez porque solo se llama una vez en el servicio
        verify(proyectoRepository, times(1)).save(any(ProyectoGrado.class));
    }

    @Test
    void buscarPorId_retornaProyectoExistente() {
        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto));

        ProyectoGradoResponse response = proyectoService.buscarPorId(1L);

        assertNotNull(response);
        assertEquals(proyecto.getId(), response.id());
    }

    @Test
    void listarTodos_retornaListaProyectos() {
        when(proyectoRepository.findAll()).thenReturn(List.of(proyecto));

        List<ProyectoGradoResponse> responses = proyectoService.listarTodos();

        assertEquals(1, responses.size());
    }

    @Test
    void buscarPorFormatoAExternoId_retornaOptional() {
        when(proyectoRepository.findByIdFormatoA(123L)).thenReturn(Optional.of(proyecto));

        Optional<ProyectoGrado> result = proyectoService.buscarPorFormatoAExternoId(123L);

        assertTrue(result.isPresent());
        assertEquals(proyecto.getId(), result.get().getId());
    }

    @Test
    void obtenerHistorialProyecto_retornaListaMementos() {
        RequestMemento memento = new RequestMemento("PROYECTO_GRADO", 1L, "ENTREGADO", 1, new HashMap<>());
        when(historyManager.getRequestHistory("PROYECTO_GRADO", 1L))
                .thenReturn(List.of(memento));

        List<RequestMemento> historial = proyectoService.obtenerHistorialProyecto(1L);

        assertEquals(1, historial.size());
        assertEquals(1, historial.get(0).getVersion());
    }

    @Test
    void obtenerEstadoProyectoVersion_retornaMemento() {
        RequestMemento memento = new RequestMemento("PROYECTO_GRADO", 1L, "ENTREGADO", 2, new HashMap<>());
        when(historyManager.restoreToRequestVersion("PROYECTO_GRADO", 1L, 2))
                .thenReturn(memento);

        RequestMemento resultado = proyectoService.obtenerEstadoProyectoVersion(1L, 2);

        assertEquals(2, resultado.getVersion());
    }

    @Test
    void restaurarProyectoAVersion_guardaNuevaEntidad() {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("id", 1L);
        requestData.put("nombre", "Restaurado");
        requestData.put("fecha", LocalDate.now());
        requestData.put("estudiantesEmail", List.of("estudiante1@uni.edu"));
        requestData.put("IdFormatoA", 123L);
        requestData.put("estado", "ENTREGADO");

        RequestMemento memento = new RequestMemento("PROYECTO_GRADO", 1L, "ENTREGADO", 1, requestData);
        when(historyManager.restoreToRequestVersion("PROYECTO_GRADO", 1L, 1))
                .thenReturn(memento);

        when(proyectoRepository.save(any(ProyectoGrado.class))).thenAnswer(invocation -> {
            ProyectoGrado p = invocation.getArgument(0);
            p.setId(2L); // nueva ID
            return p;
        });

        when(historyManager.saveRequestState(anyString(), anyLong(), anyString(), anyMap()))
                .thenReturn(new RequestMemento("PROYECTO_GRADO", 2L, "ENTREGADO", 2, new HashMap<>()));

        ProyectoGrado restaurado = proyectoService.restaurarProyectoAVersion(1L, 1);

        assertNotNull(restaurado);
        assertEquals(2L, restaurado.getId());
    }
}
