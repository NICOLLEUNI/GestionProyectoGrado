package co.unicauca.identity.security;

import co.unicauca.identity.entity.Persona;
import co.unicauca.identity.exception.UserNotFoundException;
import co.unicauca.identity.repository.PersonaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Servicio encargado de cargar usuarios desde la base de datos
 * para que Spring Security pueda autenticar y autorizar.
 *
 * Esta clase implementa UserDetailsService, lo que significa que
 * Spring Security la usará automáticamente cuando necesite:
 *  - Buscar un usuario por email (username)
 *  - Validar la contraseña durante login
 *  - Recuperar roles para construir el contexto de seguridad
 *
 * SINGLE_TABLE:
 * Adaptado al modelo único Persona, el cual contiene roles directamente.
 */
@Service
@Slf4j
public class PersonaDetailsServiceImpl implements UserDetailsService {

    private final PersonaRepository personaRepository;

    public PersonaDetailsServiceImpl(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    /**
     * Método principal usado por Spring Security.
     *
     * Se ejecuta cuando el sistema necesita validar credenciales
     * (durante login o al rehidratar el usuario desde un token JWT).
     *
     * @param email Email del usuario (funciona como "username").
     * @return UserDetails que contiene email, password y roles.
     * @throws UsernameNotFoundException si el usuario no existe.
     */


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Cargando usuario por email: {}", email);

        Persona persona = personaRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado con email: {}", email);
                    return new UsernameNotFoundException("Usuario no encontrado con email: " + email);
                });

        log.debug("Usuario encontrado: {} {} ({})",
                persona.getName(), persona.getLastname(), persona.getEmail());

        return createUserDetails(persona);
    }

    /**
     * Convierte una entidad Persona en un objeto UserDetails
     * compatible con Spring Security.
     *
     * Conversión más importante:
     * ▪ Roles (enum) → GrantedAuthority (formato requerido por Spring)
     * Ejemplo: ADMIN → ROLE_ADMIN
     */
    private UserDetails createUserDetails(Persona persona) {
        Collection<GrantedAuthority> authorities = persona.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.name()))
                .collect(Collectors.toList());

        log.debug("Autoridades asignadas para {}: {}",
                persona.getEmail(), authorities);

        // Construcción del objeto UserDetails
        return User.builder()
                .username(persona.getEmail()) // identificador del usuario
                .password(persona.getPassword()) // contraseña encriptada
                .authorities(authorities) // roles convertidos
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    /**
     * Método adicional para cargar UserDetails por ID
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long userId) {
        log.debug("Cargando usuario por ID: {}", userId);

        Persona persona = personaRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado con ID: {}", userId);
                    return new UserNotFoundException(userId);
                });

        return createUserDetails(persona);
    }

    /**
     * Verifica si un usuario existe por email
     */
    @Transactional(readOnly = true)
    public boolean userExists(String email) {
        return personaRepository.existsByEmailIgnoreCase(email);
    }
}