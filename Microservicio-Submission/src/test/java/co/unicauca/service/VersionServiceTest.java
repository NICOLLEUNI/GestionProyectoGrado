package co.unicauca.service;

import co.unicauca.entity.EnumEstado;
import co.unicauca.entity.EnumModalidad;
import co.unicauca.entity.FormatoA;
import co.unicauca.entity.FormatoAVersion;
import co.unicauca.infra.dto.FormatoAEditRequest;
import co.unicauca.infra.dto.FormatoARequest;
import co.unicauca.repository.FormatoVersionRepository;
import co.unicauca.infra.messaging.RabbitMQPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VersionServiceTest {

    private VersionService versionService;
    private FormatoVersionRepository formatoVersionRepository;
    private RabbitMQPublisher rabbitMQPublisher;

    @BeforeEach
    void setUp() {
        formatoVersionRepository = mock(FormatoVersionRepository.class);
        rabbitMQPublisher = mock(RabbitMQPublisher.class);
        versionService = new VersionService(formatoVersionRepository, rabbitMQPublisher);
    }

    @Test
    void crearVersionInicial() {
        FormatoA formatoA = new FormatoA();
        formatoA.setId(1L);
        formatoA.setTitle("Test Proyecto");
        formatoA.setMode(EnumModalidad.INVESTIGACION);
        formatoA.setGeneralObjetive("Objetivo General");
        formatoA.setSpecificObjetives("Objetivos Específicos");
        formatoA.setArchivoPDF("ruta/pdf");
        formatoA.setCartaLaboral("ruta/carta");
        formatoA.setState(EnumEstado.ENTREGADO);

        when(formatoVersionRepository.save(any(FormatoAVersion.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        FormatoAVersion version = versionService.crearVersionInicial(formatoA);

        assertNotNull(version);
        assertEquals(1, version.getNumeroVersion());
        assertEquals(LocalDate.now(), version.getFecha());
        assertEquals("Test Proyecto", version.getTitle());
        assertEquals(EnumModalidad.INVESTIGACION, version.getMode());
        verify(formatoVersionRepository, times(1)).save(any(FormatoAVersion.class));
        verify(rabbitMQPublisher, times(1)).publicarVersionCreada(any());
    }

    @Test
    void actualizarRutasArchivos() {
        FormatoA formatoA = new FormatoA();
        formatoA.setId(1L);
        formatoA.setArchivoPDF("nueva/ruta/pdf");
        formatoA.setCartaLaboral("nueva/ruta/carta");

        FormatoAVersion ultimaVersion = new FormatoAVersion();
        ultimaVersion.setId(10L);
        ultimaVersion.setFormatoA(formatoA);

        when(formatoVersionRepository.findTopByFormatoAOrderByNumeroVersionDesc(any()))
                .thenReturn(ultimaVersion);

        versionService.actualizarRutasArchivos(formatoA);

        assertEquals("nueva/ruta/pdf", ultimaVersion.getArchivoPDF());
        assertEquals("nueva/ruta/carta", ultimaVersion.getCartaLaboral());
        verify(formatoVersionRepository, times(1)).save(ultimaVersion);
    }

    @Test
    void crearVersionConEvaluacion() {
        FormatoA formatoA = new FormatoA();
        formatoA.setId(1L);
        formatoA.setTitle("Proyecto Evaluado");
        formatoA.setMode(EnumModalidad.PRACTICA_PROFESIONAL);
        formatoA.setState(EnumEstado.RECHAZADO);
        formatoA.setGeneralObjetive("General");
        formatoA.setSpecificObjetives("Especificos");

        when(formatoVersionRepository.findMaxVersionByFormatoAId(1L)).thenReturn(1);
        when(formatoVersionRepository.save(any(FormatoAVersion.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        FormatoARequest request = mock(FormatoARequest.class);
        FormatoAVersion version = versionService.crearVersionConEvaluacion(formatoA, request);

        assertEquals(2, version.getNumeroVersion());
        assertEquals(EnumEstado.RECHAZADO, version.getState());
        assertEquals(EnumModalidad.PRACTICA_PROFESIONAL, version.getMode());
        verify(formatoVersionRepository, times(1)).save(any(FormatoAVersion.class));
    }

    @Test
    void crearVersionReenviada() {
        FormatoA formatoA = new FormatoA();
        formatoA.setId(1L);
        formatoA.setMode(EnumModalidad.INVESTIGACION);
        formatoA.setCounter(1);

        FormatoAEditRequest request = new FormatoAEditRequest(
                1L, "ruta/pdf", "ruta/carta", "Nuevo objetivo general", "Nuevos específicos"
        );

        when(formatoVersionRepository.findMaxVersionByFormatoAId(1L)).thenReturn(3);
        when(formatoVersionRepository.save(any(FormatoAVersion.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        FormatoAVersion version = versionService.crearVersionReenviada(formatoA, request);

        assertEquals(4, version.getNumeroVersion());
        assertEquals("Nuevo objetivo general", version.getGeneralObjetive());
        assertEquals(EnumEstado.ENTREGADO, version.getState());
        assertEquals(EnumModalidad.INVESTIGACION, version.getMode());
        verify(formatoVersionRepository, times(1)).save(any(FormatoAVersion.class));
    }

    @Test
    void obtenerProximaVersion() {
        FormatoA formatoA = new FormatoA();
        formatoA.setId(1L);

        when(formatoVersionRepository.findMaxVersionByFormatoAId(1L)).thenReturn(2);
        assertEquals(3, versionService.obtenerProximaVersion(formatoA));
    }

    @Test
    void eliminarVersionesPorFormatoA() {
        Long formatoAId = 1L;
        List<FormatoAVersion> lista = List.of(new FormatoAVersion(), new FormatoAVersion());

        when(formatoVersionRepository.findByFormatoAId(formatoAId)).thenReturn(lista);

        versionService.eliminarVersionesPorFormatoA(formatoAId);

        verify(formatoVersionRepository, times(1)).deleteAll(lista);
    }
}
