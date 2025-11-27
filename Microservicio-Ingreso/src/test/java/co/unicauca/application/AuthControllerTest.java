package co.unicauca.application;

import co.unicauca.identity.controller.AuthController;
import co.unicauca.identity.dto.request.LoginRequest;
import co.unicauca.identity.dto.request.RegisterRequest;
import co.unicauca.identity.dto.response.ApiResponse;
import co.unicauca.identity.dto.response.LoginResponse;
import co.unicauca.identity.dto.response.PersonaResponse;
import co.unicauca.identity.dto.response.RolesResponse;
import co.unicauca.identity.entity.Persona;
import co.unicauca.identity.enums.EnumDepartamento;
import co.unicauca.identity.enums.EnumPrograma;
import co.unicauca.identity.enums.enumRol;
import co.unicauca.identity.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private PersonaResponse personaResponse;
    private LoginResponse loginResponse;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();

        // Setup request objects
        Set<enumRol> roles = EnumSet.of(enumRol.ESTUDIANTE);
        registerRequest = new RegisterRequest(
                "Juan",
                "Pérez",
                "3123456789",
                roles,
                EnumPrograma.INGENIERIA_DE_SISTEMAS,
                null,
                "juan.perez@unicauca.edu.co",
                "Password123!"
        );

        loginRequest = new LoginRequest(
                "juan.perez@unicauca.edu.co",
                "Password123!"
        );

        // Setup response objects
        personaResponse = PersonaResponse.builder()
                .id(1L)
                .name("Juan")
                .lastname("Pérez")
                .email("juan.perez@unicauca.edu.co")
                .roles(Set.of("ESTUDIANTE"))
                .department(null)
                .programa("INGENIERIA_DE_SISTEMAS")
                .phone("3123456789")
                .build();



        loginResponse = new LoginResponse(personaResponse, "jwt-token");
    }

    @Test
    void testRegister_Success() throws Exception {
        // Arrange
        when(authService.register(any(RegisterRequest.class))).thenReturn(personaResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Juan"))
                .andExpect(jsonPath("$.data.email").value("juan.perez@unicauca.edu.co"));
    }

    @Test
    void testLogin_Success() throws Exception {
        // Arrange
        when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").value("jwt-token"))
                .andExpect(jsonPath("$.data.persona.email").value("juan.perez@unicauca.edu.co"));
    }

    @Test
    void testGetProfile_Success() {
        // Arrange
        UserDetails userDetails = User.withUsername("juan.perez@unicauca.edu.co")
                .password("password")
                .authorities("ROLE_ESTUDIANTE")
                .build();

        when(authService.getUserIdByEmail("juan.perez@unicauca.edu.co")).thenReturn(1L);
        when(authService.getProfile(1L)).thenReturn(personaResponse);

        // Act
        ResponseEntity<ApiResponse<PersonaResponse>> response = authController.getProfile(userDetails);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().success());
        assertEquals("Juan", response.getBody().data().name());
    }

    @Test
    void testGetRoles() {
        // Arrange
        RolesResponse rolesResponse = RolesResponse.builder()
                .roles(List.of("ESTUDIANTE", "DOCENTE"))
                .programas(List.of("INGENIERIA_DE_SISTEMAS"))
                .departamentos(List.of("SISTEMAS"))
                .build();

        when(authService.getAvailableRolesAndPrograms()).thenReturn(rolesResponse);

        // Act
        ResponseEntity<ApiResponse<RolesResponse>> response = authController.getRoles();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().success());
        assertTrue(response.getBody().data().roles().contains("ESTUDIANTE"));
    }

    @Test
    void testSearchUsers() {
        // Arrange
        Page<PersonaResponse> personaPage = new PageImpl<>(List.of(personaResponse));
        when(authService.searchUsers(anyString(), any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(personaPage);

        // Act
        ResponseEntity<ApiResponse<Page<PersonaResponse>>> response = authController.searchUsers(
                "Juan", enumRol.ESTUDIANTE, EnumPrograma.INGENIERIA_DE_SISTEMAS,
                null, 0, 10
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().success());
        assertEquals(1, response.getBody().data().getTotalElements());
    }

    @Test
    void testCheckEmailAvailability() {
        // Arrange
        when(authService.isEmailRegistered("test@unicauca.edu.co")).thenReturn(false);

        // Act
        ResponseEntity<ApiResponse<Boolean>> response = authController.checkEmailAvailability("test@unicauca.edu.co");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().success());
        assertTrue(response.getBody().data());
    }
}