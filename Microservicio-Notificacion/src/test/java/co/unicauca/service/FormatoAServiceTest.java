package co.unicauca.service;

import co.unicauca.entity.FormatoA;
import co.unicauca.infra.dto.FormatoACreado;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FormatoAServiceTest {

    @InjectMocks
    private FormatoAService formatoAService;

    @Test
    void procesarNotificacionEvaluado() {
        // Arrange
        FormatoA formatoA = new FormatoA();
        formatoA.setTitulo("Proyecto de Investigación");
        formatoA.setCorreosDocentes(Arrays.asList("docente1@unicauca.edu.co", "docente2@unicauca.edu.co"));
        formatoA.setCorreosEstudiantes(Arrays.asList("estudiante1@unicauca.edu.co", "estudiante2@unicauca.edu.co"));

        // Act
        formatoAService.procesarNotificacionEvaluado(formatoA);

        // Assert - No hay excepciones y el método ejecuta correctamente
        assertTrue(true); // Si llega aquí sin excepciones, el test pasa
    }

    @Test
    void procesarNotificacionEvaluado_SinCorreos_NoDeberiaLanzarExcepcion() {
        // Arrange
        FormatoA formatoA = new FormatoA();
        formatoA.setTitulo("Proyecto sin correos");

        // Act & Assert - No debería lanzar excepciones
        assertDoesNotThrow(() -> formatoAService.procesarNotificacionEvaluado(formatoA));
    }

    @Test
    void procesarNotificacionCreado() {
        // Arrange
        List<String> estudiantesEmails = Arrays.asList("estudiante1@unicauca.edu.co", "estudiante2@unicauca.edu.co");
        FormatoACreado formatoCreado = new FormatoACreado(
                1L,
                "Nuevo Proyecto de Grado",
                estudiantesEmails,
                "director@unicauca.edu.co"
        );

        // Act
        formatoAService.procesarNotificacionCreado(formatoCreado);

        // Assert - No hay excepciones y el método ejecuta correctamente
        assertTrue(true); // Si llega aquí sin excepciones, el test pasa
    }

    @Test
    void procesarNotificacionCreado_SinDirectorEmail_NoDeberiaLanzarExcepcion() {
        // Arrange
        List<String> estudiantesEmails = Arrays.asList("estudiante@unicauca.edu.co");
        FormatoACreado formatoCreado = new FormatoACreado(
                1L,
                "Proyecto sin director",
                estudiantesEmails,
                null // Sin director
        );

        // Act & Assert - No debería lanzar excepciones
        assertDoesNotThrow(() -> formatoAService.procesarNotificacionCreado(formatoCreado));
    }

    @Test
    void procesarNotificacionCreado_SinEstudiantes_NoDeberiaLanzarExcepcion() {
        // Arrange
        FormatoACreado formatoCreado = new FormatoACreado(
                1L,
                "Proyecto sin estudiantes",
                null, // Sin estudiantes
                "director@unicauca.edu.co"
        );

        // Act & Assert - No debería lanzar excepciones
        assertDoesNotThrow(() -> formatoAService.procesarNotificacionCreado(formatoCreado));
    }

    @Test
    void procesarNotificacionCreado_ConEventoNulo_NoDeberiaLanzarExcepcion() {
        // Act & Assert - No debería lanzar excepciones con evento nulo
        assertDoesNotThrow(() -> formatoAService.procesarNotificacionCreado(null));
    }
}