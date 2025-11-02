package co.unicauca.identity.service;

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
import co.unicauca.identity.repository.PersonaRepository;
import co.unicauca.identity.security.JwtTokenProvider;
import co.unicauca.identity.strategy.RoleValidationContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementación de servicios de autenticación - SINGLE_TABLE optimized
 * Elimina complejidad de múltiples repositorios y fábrica
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final PersonaRepository personaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RoleValidationContext roleValidationContext;
    private final EventPublisherService eventPublisherService;

    public AuthServiceImpl(PersonaRepository personaRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider,
                           RoleValidationContext roleValidationContext,
                           EventPublisherService eventPublisherService) {
        this.personaRepository = personaRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.roleValidationContext = roleValidationContext;
        this.eventPublisherService = eventPublisherService;
    }
    @Override
    @Transactional
    public PersonaResponse register(RegisterRequest request) {
        // ✅ AGREGAR: Logging detallado al inicio
        log.info("=== INICIANDO REGISTRO DETALLADO ===");
        log.info("Email: {}", request.email());
        log.info("Roles: {}", request.roles());
        log.info("Programa recibido en request: {}", request.programa());
        log.info("Departamento recibido en request: {}", request.departamento());

        // 1. Validar email único (SIMPLE - una tabla)
        if (isEmailRegistered(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        // 2. Validaciones específicas por rol (USANDO RoleValidationContext)
        roleValidationContext.validateRoles(request.roles(), request.programa(), request.departamento());

        // 3. Hashear contraseña
        String hashedPassword = passwordEncoder.encode(request.password());

        // ✅ AGREGAR: Logging antes de crear la entidad
        log.info("=== ANTES DE CREAR PERSONA ===");
        log.info("Programa a asignar: {}", request.programa());
        log.info("Departamento a asignar: {}", request.departamento());

        // 4. ✅ CREACIÓN SIMPLE - Una sola entidad vs múltiples entidades
        Persona persona = new Persona(
                request.name(),
                request.lastname(),
                request.phone(),
                request.email(),
                hashedPassword,
                request.roles(),
                request.programa(),
                request.departamento()
        );

        // ✅ AGREGAR: Logging después de crear la entidad
        log.info("=== DESPUÉS DE CREAR PERSONA ===");
        log.info("Persona.programa: {}", persona.getPrograma());
        log.info("Persona.departamento: {}", persona.getDepartamento());

        // 5. ✅ GUARDADO SIMPLE - Un solo repositorio vs múltiples repositorios
        Persona savedPersona = personaRepository.save(persona);

        // ✅ AGREGAR: Logging después de guardar en BD
        log.info("=== DESPUÉS DE GUARDAR EN BD ===");
        log.info("Usuario registrado exitosamente SINGLE_TABLE: {}", savedPersona.getEmail());
        log.info("ID guardado: {}", savedPersona.getIdUsuario());
        log.info("Programa guardado: {}", savedPersona.getPrograma());
        log.info("Departamento guardado: {}", savedPersona.getDepartamento());

        // 6. Publicar evento de registro
        eventPublisherService.publishUserRegisteredEvent(savedPersona);

        // ✅ AGREGAR: Logging antes del mapeo final
        PersonaResponse response = mapPersonaToResponse(savedPersona);
        log.info("=== RESPUESTA FINAL ===");
        log.info("Response.programa: {}", response.programa());
        log.info("Response.departament: {}", response.department());
        log.info("=== REGISTRO COMPLETADO ===");

        return response;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("=== INICIANDO PROCESO DE LOGIN ===");
        log.info("Email recibido: {}", request.email());

        try {
            // ✅ BÚSQUEDA DEL USUARIO
            log.info("Buscando usuario en BD...");
            Persona persona = personaRepository.findByEmailIgnoreCase(request.email())
                    .orElseThrow(() -> {
                        log.error("Usuario no encontrado con email: {}", request.email());
                        return new InvalidCredentialsException(request.email());
                    });

            log.info("Usuario encontrado: ID={}, Email={}", persona.getIdUsuario(), persona.getEmail());

            // ✅ VERIFICACIÓN DE CONTRASEÑA
            log.info("Verificando contraseña...");
            if (!passwordEncoder.matches(request.password(), persona.getPassword())) {
                log.error("Contraseña incorrecta para email: {}", request.email());
                eventPublisherService.publishLoginFailedEvent(request.email());
                throw new InvalidCredentialsException(request.email());
            }
            log.info("Contraseña validada correctamente");

            // ✅ GENERACIÓN DEL TOKEN JWT
            log.info("Generando token JWT...");
            String token = jwtTokenProvider.generateToken(persona);
            log.info("Token generado exitosamente");

            // ✅ MAPEO A RESPONSE
            log.info("Mapeando Persona a PersonaResponse...");
            PersonaResponse personaResponse = mapPersonaToResponse(persona);
            log.info("Mapeo completado - Programa: {}, Departamento: {}",
                    personaResponse.programa(), personaResponse.department());

            log.info("Login exitoso para: {}", persona.getEmail());
            eventPublisherService.publishLoginSuccessEvent(persona);

            // ✅ CONSTRUCCIÓN DE LA RESPUESTA
            LoginResponse loginResponse = LoginResponse.builder()
                    .persona(personaResponse)
                    .token(token)
                    .build();

            log.info("=== LOGIN COMPLETADO EXITOSAMENTE ===");
            return loginResponse;

        } catch (Exception e) {
            log.error("ERROR durante el login para email {}: {}", request.email(), e.getMessage(), e);
            throw e; // Re-lanzar la excepción para que GlobalExceptionHandler la maneje
        }
    }

    @Override
    public PersonaResponse getProfile(Long userId) {
        Persona persona = personaRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return mapPersonaToResponse(persona);
    }

    @Override
    public RolesResponse getAvailableRolesAndPrograms() {
        return RolesResponse.builder()
                .roles(Arrays.stream(enumRol.values())
                        .map(Enum::name)
                        .toList())
                .programas(Arrays.stream(EnumPrograma.values())
                        .map(Enum::name)
                        .toList())
                .departamentos(Arrays.stream(EnumDepartamento.values())
                        .map(Enum::name)
                        .toList())
                .build();
    }

    @Override
    public Long getUserIdByEmail(String email) {
        Persona persona = personaRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        return persona.getIdUsuario();
    }

    @Override
    public Page<PersonaResponse> searchUsers(String query, enumRol rol, EnumPrograma programa,
                                             EnumDepartamento departamento, int page, int size) {

        // ✅ CONSULTA SIMPLE - Specification en una tabla vs múltiples tablas
        Specification<Persona> spec = buildSearchSpecification(query, rol, programa, departamento);
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        Page<Persona> personas = personaRepository.findAll(spec, pageable);
        return personas.map(this::mapPersonaToResponse);
    }

    @Override
    public boolean isEmailRegistered(String email) {
        return personaRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public PersonaResponse mapPersonaToResponse(Persona persona) {
        // Convertir roles a Set<String>
        Set<String> rolesAsStrings = persona.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());

        // Convertir enums a strings
        String departmentStr = persona.getDepartamento() != null ? persona.getDepartamento().name() : null;
        String programaStr = persona.getPrograma() != null ? persona.getPrograma().name() : null;

        // Usar el factory method create con lógica condicional
        return PersonaResponse.create(
                persona.getIdUsuario(),
                persona.getName(),
                persona.getLastname(),
                persona.getEmail(),
                rolesAsStrings,
                departmentStr,  // ✅ department (NO departament)
                programaStr
        );
    }

    /*
     * Construye Specification para búsquedas dinámicas
     * SINGLE_TABLE: Consultas simplificadas en una tabla
     */
    private Specification<Persona> buildSearchSpecification(String query, enumRol rol,
                                                            EnumPrograma programa, EnumDepartamento departamento) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            var predicates = criteriaBuilder.conjunction();

            // Búsqueda por texto
            if (query != null && !query.trim().isEmpty()) {
                String searchTerm = "%" + query.toLowerCase() + "%";
                var textPredicate = criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchTerm),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("lastname")), searchTerm),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), searchTerm)
                );
                predicates = criteriaBuilder.and(predicates, textPredicate);
            }

            // Filtro por rol
            if (rol != null) {
                var rolPredicate = criteriaBuilder.isMember(rol, root.get("roles"));
                predicates = criteriaBuilder.and(predicates, rolPredicate);
            }

            // Filtro por programa (solo aplica si es estudiante)
            if (programa != null) {
                var programaPredicate = criteriaBuilder.equal(root.get("programa"), programa);
                predicates = criteriaBuilder.and(predicates, programaPredicate);
            }

            // Filtro por departamento
            if (departamento != null) {
                var departamentoPredicate = criteriaBuilder.equal(root.get("departamento"), departamento);
                predicates = criteriaBuilder.and(predicates, departamentoPredicate);
            }

            return predicates;
        };
    }
}