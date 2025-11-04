package co.unicauca.application;



import co.unicauca.identity.dto.request.LoginRequest;
import co.unicauca.identity.dto.request.RegisterRequest;
import co.unicauca.identity.dto.response.LoginResponse;
import co.unicauca.identity.dto.response.PersonaResponse;
import co.unicauca.identity.dto.response.RolesResponse;
import co.unicauca.identity.entity.Persona;
import co.unicauca.identity.enums.EnumDepartamento;
import co.unicauca.identity.enums.EnumPrograma;
import co.unicauca.identity.enums.enumRol;
import co.unicauca.identity.exception.EmailAlreadyExistsException;
import co.unicauca.identity.exception.InvalidCredentialsException;
import co.unicauca.identity.exception.UserNotFoundException;
import co.unicauca.identity.exception.ValidationException;
import co.unicauca.identity.repository.PersonaRepository;
import co.unicauca.identity.security.JwtTokenProvider;
import co.unicauca.identity.service.AuthServiceImpl;
import co.unicauca.identity.service.EventPublisherService;
import co.unicauca.identity.strategy.RoleValidationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Auth Service Tests")
class AuthServiceImplTest {

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RoleValidationContext roleValidationContext;

    @Mock
    private EventPublisherService eventPublisherService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Captor
    private ArgumentCaptor<Persona> personaCaptor;

    @Captor
    private ArgumentCaptor<Specification<Persona>> specificationCaptor;

    private RegisterRequest estudianteRequest;
    private RegisterRequest docenteRequest;
    private RegisterRequest coordinadorRequest;
    private RegisterRequest multipleRolesRequest;
    private LoginRequest loginRequest;
    private Persona personaEstudiante;
    private Persona personaDocente;

    @BeforeEach
    void setUp() {
        // Setup requests
        estudianteRequest = new RegisterRequest(
                "Juan", "Pérez", "3123456789",
                EnumSet.of(enumRol.ESTUDIANTE),
                EnumPrograma.INGENIERIA_DE_SISTEMAS, null,
                "juan.perez@unicauca.edu.co", "Password123!"
        );

        docenteRequest = new RegisterRequest(
                "María", "Gómez", "3123456790",
                EnumSet.of(enumRol.DOCENTE),
                null, EnumDepartamento.SISTEMAS,
                "maria.gomez@unicauca.edu.co", "Password123!"
        );

        coordinadorRequest = new RegisterRequest(
                "Carlos", "López", "3123456791",
                EnumSet.of(enumRol.COORDINADOR),
                EnumPrograma.INGENIERIA_ELECTRONICA_Y_TELECOMUNICACIONES, null,
                "carlos.lopez@unicauca.edu.co", "Password123!"
        );

        multipleRolesRequest = new RegisterRequest(
                "Ana", "Rodríguez", "3123456792",
                EnumSet.of(enumRol.ESTUDIANTE, enumRol.DOCENTE),
                EnumPrograma.AUTOMATICA_INDUSTRIAL, EnumDepartamento.ELECTRONICA,
                "ana.rodriguez@unicauca.edu.co", "Password123!"
        );

        loginRequest = new LoginRequest(
                "juan.perez@unicauca.edu.co", "Password123!"
        );

        // Setup entities
        personaEstudiante = new Persona(
                "Juan", "Pérez", "3123456789",
                "juan.perez@unicauca.edu.co", "hashedPassword123",
                EnumSet.of(enumRol.ESTUDIANTE),
                EnumPrograma.INGENIERIA_DE_SISTEMAS, null
        );
        personaEstudiante.setIdUsuario(1L);

        personaDocente = new Persona(
                "María", "Gómez", "3123456790",
                "maria.gomez@unicauca.edu.co", "hashedPassword456",
                EnumSet.of(enumRol.DOCENTE),
                null, EnumDepartamento.SISTEMAS
        );
        personaDocente.setIdUsuario(2L);
    }

    @Nested
    @DisplayName("Register Tests")
    class RegisterTests {

        @Test
        @DisplayName("Should register student successfully")
        void shouldRegisterStudentSuccessfully() {
            // Arrange
            when(personaRepository.existsByEmailIgnoreCase(estudianteRequest.email())).thenReturn(false);
            when(passwordEncoder.encode(estudianteRequest.password())).thenReturn("hashedPassword123");
            when(personaRepository.save(any(Persona.class))).thenReturn(personaEstudiante);
            doNothing().when(roleValidationContext).validateRoles(any(), any(), any());
            doNothing().when(eventPublisherService).publishUserRegisteredEvent(any(Persona.class));

            // Act
            PersonaResponse result = authService.register(estudianteRequest);

            // Assert
            assertNotNull(result);
            assertEquals("Juan", result.name());
            assertEquals("Pérez", result.lastname());
            assertEquals("juan.perez@unicauca.edu.co", result.email());
            assertTrue(result.roles().contains("ESTUDIANTE"));
            assertEquals("INGENIERIA_DE_SISTEMAS", result.programa());
            assertNull(result.department());

            verify(personaRepository).existsByEmailIgnoreCase(estudianteRequest.email());
            verify(passwordEncoder).encode(estudianteRequest.password());
            verify(personaRepository).save(personaCaptor.capture());
            verify(roleValidationContext).validateRoles(estudianteRequest.roles(), estudianteRequest.programa(), estudianteRequest.departamento());
            verify(eventPublisherService).publishUserRegisteredEvent(personaEstudiante);

            // Verify captured persona
            Persona capturedPersona = personaCaptor.getValue();
            assertEquals("Juan", capturedPersona.getName());
            assertEquals("hashedPassword123", capturedPersona.getPassword());
            assertTrue(capturedPersona.getRoles().contains(enumRol.ESTUDIANTE));
        }

        @Test
        @DisplayName("Should register teacher successfully")
        void shouldRegisterTeacherSuccessfully() {
            // Arrange
            when(personaRepository.existsByEmailIgnoreCase(docenteRequest.email())).thenReturn(false);
            when(passwordEncoder.encode(docenteRequest.password())).thenReturn("hashedPassword456");
            when(personaRepository.save(any(Persona.class))).thenReturn(personaDocente);
            doNothing().when(roleValidationContext).validateRoles(any(), any(), any());
            doNothing().when(eventPublisherService).publishUserRegisteredEvent(any(Persona.class));

            // Act
            PersonaResponse result = authService.register(docenteRequest);

            // Assert
            assertNotNull(result);
            assertEquals("María", result.name());
            assertEquals("Gómez", result.lastname());
            assertEquals("maria.gomez@unicauca.edu.co", result.email());
            assertTrue(result.roles().contains("DOCENTE"));
            assertEquals("SISTEMAS", result.department());
            assertNull(result.programa());

            verify(personaRepository).save(personaCaptor.capture());
            Persona capturedPersona = personaCaptor.getValue();
            assertEquals(EnumDepartamento.SISTEMAS, capturedPersona.getDepartamento());
        }

        @Test
        @DisplayName("Should register user with multiple roles successfully")
        void shouldRegisterUserWithMultipleRolesSuccessfully() {
            // Arrange
            Persona personaMultiple = new Persona(
                    "Ana", "Rodríguez", "3123456792",
                    "ana.rodriguez@unicauca.edu.co", "hashedPassword789",
                    EnumSet.of(enumRol.ESTUDIANTE, enumRol.DOCENTE),
                    EnumPrograma.AUTOMATICA_INDUSTRIAL, EnumDepartamento.ELECTRONICA
            );
            personaMultiple.setIdUsuario(3L);

            when(personaRepository.existsByEmailIgnoreCase(multipleRolesRequest.email())).thenReturn(false);
            when(passwordEncoder.encode(multipleRolesRequest.password())).thenReturn("hashedPassword789");
            when(personaRepository.save(any(Persona.class))).thenReturn(personaMultiple);
            doNothing().when(roleValidationContext).validateRoles(any(), any(), any());
            doNothing().when(eventPublisherService).publishUserRegisteredEvent(any(Persona.class));

            // Act
            PersonaResponse result = authService.register(multipleRolesRequest);

            // Assert
            assertNotNull(result);
            assertTrue(result.roles().contains("ESTUDIANTE"));
            assertTrue(result.roles().contains("DOCENTE"));
            assertEquals("AUTOMATICA_INDUSTRIAL", result.programa());
            assertEquals("ELECTRONICA", result.department());

            verify(roleValidationContext).validateRoles(
                    multipleRolesRequest.roles(),
                    multipleRolesRequest.programa(),
                    multipleRolesRequest.departamento()
            );
        }

        @Test
        @DisplayName("Should throw exception when email already exists")
        void shouldThrowExceptionWhenEmailAlreadyExists() {
            // Arrange
            when(personaRepository.existsByEmailIgnoreCase(estudianteRequest.email())).thenReturn(true);

            // Act & Assert
            EmailAlreadyExistsException exception = assertThrows(
                    EmailAlreadyExistsException.class,
                    () -> authService.register(estudianteRequest)
            );

            assertTrue(exception.getMessage().contains("juan.perez@unicauca.edu.co"));
            verify(personaRepository, never()).save(any(Persona.class));
            verify(eventPublisherService, never()).publishUserRegisteredEvent(any(Persona.class));
        }

        @Test
        @DisplayName("Should throw validation exception when role validation fails")
        void shouldThrowValidationExceptionWhenRoleValidationFails() {
            // Arrange
            when(personaRepository.existsByEmailIgnoreCase(estudianteRequest.email())).thenReturn(false);
            doThrow(new ValidationException("Programa requerido para estudiantes"))
                    .when(roleValidationContext).validateRoles(any(), any(), any());

            // Act & Assert
            ValidationException exception = assertThrows(
                    ValidationException.class,
                    () -> authService.register(estudianteRequest)
            );

            assertTrue(exception.getMessage().contains("Programa requerido"));
            verify(personaRepository, never()).save(any(Persona.class));
        }
    }

    @Nested
    @DisplayName("Login Tests")
    class LoginTests {

        @Test
        @DisplayName("Should login successfully with valid credentials")
        void shouldLoginSuccessfullyWithValidCredentials() {
            // Arrange
            when(personaRepository.findByEmailIgnoreCase(loginRequest.email()))
                    .thenReturn(Optional.of(personaEstudiante));
            when(passwordEncoder.matches(loginRequest.password(), personaEstudiante.getPassword()))
                    .thenReturn(true);
            when(jwtTokenProvider.generateToken(personaEstudiante)).thenReturn("jwt-token-123");
            doNothing().when(eventPublisherService).publishLoginSuccessEvent(any(Persona.class));

            // Act
            LoginResponse result = authService.login(loginRequest);

            // Assert
            assertNotNull(result);
            assertEquals("jwt-token-123", result.token());
            assertEquals("juan.perez@unicauca.edu.co", result.persona().email());
            assertEquals("Juan", result.persona().name());

            verify(personaRepository).findByEmailIgnoreCase(loginRequest.email());
            verify(passwordEncoder).matches(loginRequest.password(), personaEstudiante.getPassword());
            verify(jwtTokenProvider).generateToken(personaEstudiante);
            verify(eventPublisherService).publishLoginSuccessEvent(personaEstudiante);
        }

        @Test
        @DisplayName("Should throw exception when user not found")
        void shouldThrowExceptionWhenUserNotFound() {
            // Arrange
            when(personaRepository.findByEmailIgnoreCase(loginRequest.email()))
                    .thenReturn(Optional.empty());

            // Act & Assert
            InvalidCredentialsException exception = assertThrows(
                    InvalidCredentialsException.class,
                    () -> authService.login(loginRequest)
            );

            assertTrue(exception.getMessage().contains("juan.perez@unicauca.edu.co"));
            verify(passwordEncoder, never()).matches(any(), any());
            verify(jwtTokenProvider, never()).generateToken(any());
            verify(eventPublisherService, never()).publishLoginSuccessEvent(any(Persona.class));
        }

        @Test
        @DisplayName("Should throw exception when password is incorrect")
        void shouldThrowExceptionWhenPasswordIsIncorrect() {
            // Arrange
            when(personaRepository.findByEmailIgnoreCase(loginRequest.email()))
                    .thenReturn(Optional.of(personaEstudiante));
            when(passwordEncoder.matches(loginRequest.password(), personaEstudiante.getPassword()))
                    .thenReturn(false);
            doNothing().when(eventPublisherService).publishLoginFailedEvent(anyString());

            // Act & Assert
            InvalidCredentialsException exception = assertThrows(
                    InvalidCredentialsException.class,
                    () -> authService.login(loginRequest)
            );

            assertTrue(exception.getMessage().contains("juan.perez@unicauca.edu.co"));
            verify(eventPublisherService).publishLoginFailedEvent(loginRequest.email());
            verify(jwtTokenProvider, never()).generateToken(any());
        }

        @Test
        @DisplayName("Should publish login failed event on invalid credentials")
        void shouldPublishLoginFailedEventOnInvalidCredentials() {
            // Arrange
            when(personaRepository.findByEmailIgnoreCase(loginRequest.email()))
                    .thenReturn(Optional.of(personaEstudiante));
            when(passwordEncoder.matches(loginRequest.password(), personaEstudiante.getPassword()))
                    .thenReturn(false);
            doNothing().when(eventPublisherService).publishLoginFailedEvent(anyString());

            // Act & Assert
            assertThrows(InvalidCredentialsException.class,
                    () -> authService.login(loginRequest));

            verify(eventPublisherService).publishLoginFailedEvent(loginRequest.email());
        }
    }

    @Nested
    @DisplayName("Profile Tests")
    class ProfileTests {

        @Test
        @DisplayName("Should get profile successfully")
        void shouldGetProfileSuccessfully() {
            // Arrange
            when(personaRepository.findById(1L)).thenReturn(Optional.of(personaEstudiante));

            // Act
            PersonaResponse result = authService.getProfile(1L);

            // Assert
            assertNotNull(result);
            assertEquals(1L, result.id());
            assertEquals("Juan", result.name());
            assertEquals("Pérez", result.lastname());
            verify(personaRepository).findById(1L);
        }

        @Test
        @DisplayName("Should throw exception when profile not found")
        void shouldThrowExceptionWhenProfileNotFound() {
            // Arrange
            when(personaRepository.findById(99L)).thenReturn(Optional.empty());

            // Act & Assert
            UserNotFoundException exception = assertThrows(
                    UserNotFoundException.class,
                    () -> authService.getProfile(99L)
            );

            assertTrue(exception.getMessage().contains("99"));
            verify(personaRepository).findById(99L);
        }
    }

    @Nested
    @DisplayName("Utility Methods Tests")
    class UtilityMethodsTests {

        @Test
        @DisplayName("Should get available roles and programs")
        void shouldGetAvailableRolesAndPrograms() {
            // Act
            RolesResponse result = authService.getAvailableRolesAndPrograms();

            // Assert
            assertNotNull(result);
            assertNotNull(result.roles());
            assertNotNull(result.programas());
            assertNotNull(result.departamentos());

            // Verify all roles are present
            assertTrue(result.roles().contains("ESTUDIANTE"));
            assertTrue(result.roles().contains("DOCENTE"));
            assertTrue(result.roles().contains("COORDINADOR"));
            assertTrue(result.roles().contains("JEFE_DEPARTAMENTO"));

            // Verify all programs are present
            assertTrue(result.programas().contains("INGENIERIA_DE_SISTEMAS"));
            assertTrue(result.programas().contains("INGENIERIA_ELECTRONICA_Y_TELECOMUNICACIONES"));

            // Verify all departments are present
            assertTrue(result.departamentos().contains("SISTEMAS"));
            assertTrue(result.departamentos().contains("ELECTRONICA"));
        }

        @Test
        @DisplayName("Should get user ID by email")
        void shouldGetUserIdByEmail() {
            // Arrange
            when(personaRepository.findByEmailIgnoreCase("test@unicauca.edu.co"))
                    .thenReturn(Optional.of(personaEstudiante));

            // Act
            Long result = authService.getUserIdByEmail("test@unicauca.edu.co");

            // Assert
            assertEquals(1L, result);
            verify(personaRepository).findByEmailIgnoreCase("test@unicauca.edu.co");
        }

        @Test
        @DisplayName("Should throw exception when user ID not found by email")
        void shouldThrowExceptionWhenUserIdNotFoundByEmail() {
            // Arrange
            when(personaRepository.findByEmailIgnoreCase("nonexistent@unicauca.edu.co"))
                    .thenReturn(Optional.empty());

            // Act & Assert
            UserNotFoundException exception = assertThrows(
                    UserNotFoundException.class,
                    () -> authService.getUserIdByEmail("nonexistent@unicauca.edu.co")
            );

            assertTrue(exception.getMessage().contains("nonexistent@unicauca.edu.co"));
        }

        @Test
        @DisplayName("Should check if email is registered")
        void shouldCheckIfEmailIsRegistered() {
            // Arrange
            when(personaRepository.existsByEmailIgnoreCase("existing@unicauca.edu.co"))
                    .thenReturn(true);
            when(personaRepository.existsByEmailIgnoreCase("new@unicauca.edu.co"))
                    .thenReturn(false);

            // Act & Assert
            assertTrue(authService.isEmailRegistered("existing@unicauca.edu.co"));
            assertFalse(authService.isEmailRegistered("new@unicauca.edu.co"));

            verify(personaRepository, times(2)).existsByEmailIgnoreCase(anyString());
        }
    }

    @Nested
    @DisplayName("Search Tests")
    class SearchTests {

        @Test
        @DisplayName("Should search users with query only")
        void shouldSearchUsersWithQueryOnly() {
            // Arrange
            Page<Persona> personaPage = new PageImpl<>(java.util.List.of(personaEstudiante, personaDocente));
            when(personaRepository.findAll(any(Specification.class), any(Pageable.class)))
                    .thenReturn(personaPage);

            // Act
            Page<PersonaResponse> result = authService.searchUsers(
                    "Juan", null, null, null, 0, 10
            );

            // Assert
            assertNotNull(result);
            assertEquals(2, result.getTotalElements());
            verify(personaRepository).findAll(specificationCaptor.capture(), any(Pageable.class));

            // Verify specification was built correctly
            Specification<Persona> capturedSpec = specificationCaptor.getValue();
            assertNotNull(capturedSpec);
        }

        @Test
        @DisplayName("Should search users with role filter")
        void shouldSearchUsersWithRoleFilter() {
            // Arrange
            Page<Persona> personaPage = new PageImpl<>(java.util.List.of(personaEstudiante));
            when(personaRepository.findAll(any(Specification.class), any(Pageable.class)))
                    .thenReturn(personaPage);

            // Act
            Page<PersonaResponse> result = authService.searchUsers(
                    null, enumRol.ESTUDIANTE, null, null, 0, 10
            );

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
        }

        @Test
        @DisplayName("Should search users with programa filter")
        void shouldSearchUsersWithProgramaFilter() {
            // Arrange
            Page<Persona> personaPage = new PageImpl<>(java.util.List.of(personaEstudiante));
            when(personaRepository.findAll(any(Specification.class), any(Pageable.class)))
                    .thenReturn(personaPage);

            // Act
            Page<PersonaResponse> result = authService.searchUsers(
                    null, null, EnumPrograma.INGENIERIA_DE_SISTEMAS, null, 0, 10
            );

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
        }

        @Test
        @DisplayName("Should search users with all filters")
        void shouldSearchUsersWithAllFilters() {
            // Arrange
            Page<Persona> personaPage = new PageImpl<>(java.util.List.of(personaEstudiante));
            when(personaRepository.findAll(any(Specification.class), any(Pageable.class)))
                    .thenReturn(personaPage);

            // Act
            Page<PersonaResponse> result = authService.searchUsers(
                    "Juan", enumRol.ESTUDIANTE,
                    EnumPrograma.INGENIERIA_DE_SISTEMAS, null, 0, 10
            );

            // Assert
            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
        }

        @Test
        @DisplayName("Should return empty page when no users found")
        void shouldReturnEmptyPageWhenNoUsersFound() {
            // Arrange
            Page<Persona> emptyPage = Page.empty();
            when(personaRepository.findAll(any(Specification.class), any(Pageable.class)))
                    .thenReturn(emptyPage);

            // Act
            Page<PersonaResponse> result = authService.searchUsers(
                    "Nonexistent", null, null, null, 0, 10
            );

            // Assert
            assertNotNull(result);
            assertEquals(0, result.getTotalElements());
            assertTrue(result.getContent().isEmpty());
        }
    }

    @Nested
    @DisplayName("Mapping Tests")
    class MappingTests {

        @Test
        @DisplayName("Should map persona to response with student role")
        void shouldMapPersonaToResponseWithStudentRole() {
            // Act
            PersonaResponse result = authService.mapPersonaToResponse(personaEstudiante);

            // Assert
            assertNotNull(result);
            assertEquals(1L, result.id());
            assertEquals("Juan", result.name());
            assertEquals("Pérez", result.lastname());
            assertEquals("juan.perez@unicauca.edu.co", result.email());
            assertTrue(result.roles().contains("ESTUDIANTE"));
            assertEquals("INGENIERIA_DE_SISTEMAS", result.programa());
            assertNull(result.department()); // Student doesn't show department
        }

        @Test
        @DisplayName("Should map persona to response with teacher role")
        void shouldMapPersonaToResponseWithTeacherRole() {
            // Act
            PersonaResponse result = authService.mapPersonaToResponse(personaDocente);

            // Assert
            assertNotNull(result);
            assertEquals(2L, result.id());
            assertEquals("María", result.name());
            assertEquals("Gómez", result.lastname());
            assertEquals("maria.gomez@unicauca.edu.co", result.email());
            assertTrue(result.roles().contains("DOCENTE"));
            assertEquals("SISTEMAS", result.department());
            assertNull(result.programa()); // Teacher doesn't show program
        }

        @Test
        @DisplayName("Should map persona with multiple roles correctly")
        void shouldMapPersonaWithMultipleRolesCorrectly() {
            // Arrange
            Persona personaMultiple = new Persona(
                    "Ana", "Rodríguez", "3123456792",
                    "ana.rodriguez@unicauca.edu.co", "hashedPassword789",
                    EnumSet.of(enumRol.ESTUDIANTE, enumRol.DOCENTE),
                    EnumPrograma.AUTOMATICA_INDUSTRIAL, EnumDepartamento.ELECTRONICA
            );
            personaMultiple.setIdUsuario(3L);

            // Act
            PersonaResponse result = authService.mapPersonaToResponse(personaMultiple);

            // Assert
            assertNotNull(result);
            assertTrue(result.roles().contains("ESTUDIANTE"));
            assertTrue(result.roles().contains("DOCENTE"));
            // With multiple roles, both should be shown
            assertEquals("AUTOMATICA_INDUSTRIAL", result.programa());
            assertEquals("ELECTRONICA", result.department());
        }
    }

    @Test
    @DisplayName("Should handle coordinator registration correctly")
    void shouldHandleCoordinatorRegistrationCorrectly() {
        // Arrange
        Persona personaCoordinador = new Persona(
                "Carlos", "López", "3123456791",
                "carlos.lopez@unicauca.edu.co", "hashedPasswordCoord",
                EnumSet.of(enumRol.COORDINADOR),
                EnumPrograma.INGENIERIA_ELECTRONICA_Y_TELECOMUNICACIONES, null
        );
        personaCoordinador.setIdUsuario(4L);

        when(personaRepository.existsByEmailIgnoreCase(coordinadorRequest.email())).thenReturn(false);
        when(passwordEncoder.encode(coordinadorRequest.password())).thenReturn("hashedPasswordCoord");
        when(personaRepository.save(any(Persona.class))).thenReturn(personaCoordinador);
        doNothing().when(roleValidationContext).validateRoles(any(), any(), any());
        doNothing().when(eventPublisherService).publishUserRegisteredEvent(any(Persona.class));

        // Act
        PersonaResponse result = authService.register(coordinadorRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Carlos", result.name());
        assertTrue(result.roles().contains("COORDINADOR"));
        assertEquals("INGENIERIA_ELECTRONICA_Y_TELECOMUNICACIONES", result.programa());
        assertNull(result.department()); // Coordinator doesn't show department

        verify(roleValidationContext).validateRoles(
                coordinadorRequest.roles(),
                coordinadorRequest.programa(),
                coordinadorRequest.departamento()
        );
    }
}
