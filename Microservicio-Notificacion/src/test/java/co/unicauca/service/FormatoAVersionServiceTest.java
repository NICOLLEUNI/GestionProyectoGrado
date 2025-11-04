package co.unicauca.service;

import co.unicauca.entity.EnumRol;
import co.unicauca.entity.Persona;
import co.unicauca.infra.dto.DtoFormatoVersion;
import co.unicauca.repository.PersonaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FormatoAVersionServiceTest {

    @Mock
    private PersonaRepository personaRepository;

    @InjectMocks
    private FormatoAVersionService formatoAVersionService;

    @Test
    void procesarNotificacionVersion() {
        // Arrange
        List<String> estudiantesEmails = Arrays.asList("estudiante@unicauca.edu.co");

        DtoFormatoVersion version = new DtoFormatoVersion(
                1L,                    // versionId
                1L,                    // formatoAId
                1,                     // numeroVersion
                "ENTREGADO",           // estado
                estudiantesEmails,     // estudiantesEmails
                "director@unicauca.edu.co" // directorEmail
        );

        Persona estudiante = new Persona();
        estudiante.setEmail("estudiante@unicauca.edu.co");
        estudiante.setName("Juan Pérez"); // Agregar nombre del estudiante
        estudiante.setPrograma("Ingeniería de Sistemas");

        Persona coordinador = new Persona();
        coordinador.setEmail("coordinador@unicauca.edu.co");
        coordinador.setName("María García"); // Agregar nombre del coordinador

        // Usar Set para los roles
        Set<EnumRol> rolesCoordinador = new HashSet<>();
        rolesCoordinador.add(EnumRol.COORDINADOR);
        coordinador.setRoles(rolesCoordinador);

        when(personaRepository.findByEmail("estudiante@unicauca.edu.co"))
                .thenReturn(Optional.of(estudiante));
        when(personaRepository.findByProgramaAndRolesContaining("Ingeniería de Sistemas", EnumRol.COORDINADOR))
                .thenReturn(Optional.of(coordinador));

        // Act
        formatoAVersionService.procesarNotificacionVersion(version);

        // Assert
        verify(personaRepository).findByEmail("estudiante@unicauca.edu.co");
        verify(personaRepository).findByProgramaAndRolesContaining("Ingeniería de Sistemas", EnumRol.COORDINADOR);
    }
}