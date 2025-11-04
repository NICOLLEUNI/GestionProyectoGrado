package co.unicauca.service;

import co.unicauca.entity.EnumRol;
import co.unicauca.entity.Persona;
import co.unicauca.infra.dto.AnteproyectoCreado;
import co.unicauca.repository.PersonaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnteproyectoServiceTest {

    @Mock
    private PersonaRepository personaRepository;

    @InjectMocks
    private AnteproyectoService anteproyectoService;

    @Test
    void procesarNotificacionCreado() {
        // Arrange
        AnteproyectoCreado evento = new AnteproyectoCreado(
                1L,
                "Nuevo Anteproyecto de Investigación",
                "docente@unicauca.edu.co"
        );

        Persona docente = new Persona();
        docente.setEmail("docente@unicauca.edu.co");
        docente.setName("Carlos");
        docente.setLastname("Ruiz");
        docente.setDepartment("Ingeniería de Sistemas");

        Persona jefe = new Persona();
        jefe.setEmail("jefe@unicauca.edu.co");
        jefe.setName("Ana");
        jefe.setLastname("Gómez");
        jefe.setDepartment("Ingeniería de Sistemas");

        when(personaRepository.findByEmail("docente@unicauca.edu.co"))
                .thenReturn(Optional.of(docente));
        when(personaRepository.findByDepartmentAndRolesContaining("Ingeniería de Sistemas", EnumRol.JEFE_DEPARTAMENTO))
                .thenReturn(Arrays.asList(jefe));

        // Act
        anteproyectoService.procesarNotificacionCreado(evento);

        // Assert
        verify(personaRepository).findByEmail("docente@unicauca.edu.co");
        verify(personaRepository).findByDepartmentAndRolesContaining("Ingeniería de Sistemas", EnumRol.JEFE_DEPARTAMENTO);
    }

    @Test
    void procesarNotificacionCreado_ConDocenteNoEncontrado_NoDeberiaProcesar() {
        // Arrange
        AnteproyectoCreado evento = new AnteproyectoCreado(
                1L,
                "Anteproyecto sin docente",
                "docente_inexistente@unicauca.edu.co"
        );

        when(personaRepository.findByEmail("docente_inexistente@unicauca.edu.co"))
                .thenReturn(Optional.empty());

        // Act
        anteproyectoService.procesarNotificacionCreado(evento);

        // Assert
        verify(personaRepository).findByEmail("docente_inexistente@unicauca.edu.co");
        verify(personaRepository, never()).findByDepartmentAndRolesContaining(anyString(), any(EnumRol.class));
    }

    @Test
    void procesarNotificacionCreado_ConJefeNoEncontrado_NoDeberiaEnviarNotificacion() {
        // Arrange
        AnteproyectoCreado evento = new AnteproyectoCreado(
                1L,
                "Anteproyecto sin jefe",
                "docente@unicauca.edu.co"
        );

        Persona docente = new Persona();
        docente.setEmail("docente@unicauca.edu.co");
        docente.setName("Carlos");
        docente.setLastname("Ruiz");
        docente.setDepartment("Departamento sin jefe");

        when(personaRepository.findByEmail("docente@unicauca.edu.co"))
                .thenReturn(Optional.of(docente));
        when(personaRepository.findByDepartmentAndRolesContaining("Departamento sin jefe", EnumRol.JEFE_DEPARTAMENTO))
                .thenReturn(Arrays.asList());

        // Act
        anteproyectoService.procesarNotificacionCreado(evento);

        // Assert
        verify(personaRepository).findByEmail("docente@unicauca.edu.co");
        verify(personaRepository).findByDepartmentAndRolesContaining("Departamento sin jefe", EnumRol.JEFE_DEPARTAMENTO);
    }

    @Test
    void procesarNotificacionCreado_ConEventoNulo_NoDeberiaProcesar() {
        // Act
        anteproyectoService.procesarNotificacionCreado(null);

        // Assert
        verify(personaRepository, never()).findByEmail(anyString());
        verify(personaRepository, never()).findByDepartmentAndRolesContaining(anyString(), any(EnumRol.class));
    }

    @Test
    void procesarNotificacionCreado_ConDirectorEmailNulo_NoDeberiaProcesar() {
        // Arrange
        AnteproyectoCreado evento = new AnteproyectoCreado(
                1L,
                "Anteproyecto sin director",
                null
        );

        // Act
        anteproyectoService.procesarNotificacionCreado(evento);

        // Assert
        verify(personaRepository, never()).findByEmail(anyString());
        verify(personaRepository, never()).findByDepartmentAndRolesContaining(anyString(), any(EnumRol.class));
    }
}