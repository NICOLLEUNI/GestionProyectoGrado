package co.unicauca.identity.controller;

import co.unicauca.identity.dto.request.LoginRequest;
import co.unicauca.identity.dto.request.RegisterRequest;
import co.unicauca.identity.dto.response.ApiResponse;
import co.unicauca.identity.dto.response.LoginResponse;
import co.unicauca.identity.dto.response.PersonaResponse;
import co.unicauca.identity.dto.response.RolesResponse;
import co.unicauca.identity.enums.EnumDepartamento;
import co.unicauca.identity.enums.EnumPrograma;
import co.unicauca.identity.enums.enumRol;
import co.unicauca.identity.service.AuthService;
// ✅ AGREGAR ESTOS IMPORTS
import co.unicauca.identity.entity.Persona;
import co.unicauca.identity.exception.UserNotFoundException;
import co.unicauca.identity.repository.PersonaRepository;
import lombok.extern.slf4j.Slf4j; // ✅ Asegurar que este import esté

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import co.unicauca.identity.entity.Persona;
import co.unicauca.identity.repository.PersonaRepository;

import co.unicauca.identity.entity.Persona;
import co.unicauca.identity.exception.InvalidCredentialsException;
import co.unicauca.identity.exception.UserNotFoundException;
import co.unicauca.identity.repository.PersonaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
/**
 * Controlador REST para operaciones de autenticación
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Operaciones de registro, login y gestión de identidad")
public class AuthController {
    // ✅ AGREGAR LOGGER TRADICIONAL
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final PersonaRepository personaRepository;
    private final PasswordEncoder passwordEncoder;
    // ✅ Constructor con la inyección de dependencias
    public AuthController(AuthService authService, PersonaRepository personaRepository, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.personaRepository = personaRepository;
        this.passwordEncoder = passwordEncoder;
    }


    // Agregar este endpoint temporal
    @GetMapping("/debug/user/{id}")
    public ResponseEntity<Persona> debugGetUser(@PathVariable Long id) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return ResponseEntity.ok(persona);
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario")
    public ResponseEntity<ApiResponse<PersonaResponse>> register(@Valid @RequestBody RegisterRequest request) {
        PersonaResponse registeredUser = authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(registeredUser, "Usuario registrado exitosamente"));
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(loginResponse, "Login exitoso"));
    }

    @GetMapping("/profile")
    @Operation(summary = "Obtener perfil de usuario")
    public ResponseEntity<ApiResponse<PersonaResponse>> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        // ✅ CORREGIDO: Usar el servicio directamente
        String userEmail = userDetails.getUsername();
        Long userId = authService.getUserIdByEmail(userEmail);
        PersonaResponse profile = authService.getProfile(userId);
        return ResponseEntity.ok(ApiResponse.success(profile));
    }

    @GetMapping("/roles")
    @Operation(summary = "Obtener roles y programas disponibles")
    public ResponseEntity<ApiResponse<RolesResponse>> getRoles() {
        RolesResponse rolesAndPrograms = authService.getAvailableRolesAndPrograms();
        return ResponseEntity.ok(ApiResponse.success(rolesAndPrograms));
    }

    // ✅ CORREGIDO: Endpoint de búsqueda con parámetros correctos
    @GetMapping("/users/search")
    @Operation(summary = "Buscar usuarios")
    public ResponseEntity<ApiResponse<Page<PersonaResponse>>> searchUsers(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) enumRol rol, // ✅ USAR enumRol directamente
            @RequestParam(required = false) EnumPrograma programa,
            @RequestParam(required = false) EnumDepartamento departamento,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PersonaResponse> result = authService.searchUsers(query, rol, programa, departamento, page, size);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // ✅ AGREGAR: Endpoint para verificar disponibilidad de email
    @GetMapping("/check-email")
    @Operation(summary = "Verificar disponibilidad de email")
    public ResponseEntity<ApiResponse<Boolean>> checkEmailAvailability(
            @RequestParam String email) {
        boolean isAvailable = !authService.isEmailRegistered(email);
        return ResponseEntity.ok(ApiResponse.success(isAvailable));
    }

    /**
     * ✅ CORREGIDO: Endpoint temporal para debug del login
     */
    @PostMapping("/debug-login")
    @Operation(summary = "Debug login - sin JWT")
    public ResponseEntity<ApiResponse<String>> debugLogin(@Valid @RequestBody LoginRequest request) {
        try {
            log.info("=== DEBUG LOGIN INICIADO ===");
            log.info("Email: {}", request.email());

            // Buscar usuario directamente
            Persona persona = personaRepository.findByEmailIgnoreCase(request.email())
                    .orElseThrow(() -> {
                        log.error("DEBUG: Usuario no encontrado");
                        return new InvalidCredentialsException(request.email());
                    });

            log.info("DEBUG: Usuario encontrado - ID: {}, Email: {}",
                    persona.getIdUsuario(), persona.getEmail());

            // Verificar contraseña
            boolean passwordMatches = passwordEncoder.matches(request.password(), persona.getPassword());
            log.info("DEBUG: Contraseña coincide: {}", passwordMatches);

            if (!passwordMatches) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Credenciales inválidas"));
            }

            log.info("DEBUG: Login exitoso");
            return ResponseEntity.ok(ApiResponse.success("Login debug exitoso"));

        } catch (Exception e) {
            log.error("DEBUG: Error en login: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error debug: " + e.getMessage()));
        }
    }


}