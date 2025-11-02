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
 * Implementación de UserDetailsService para cargar detalles de Persona
 * SINGLE_TABLE: Adaptado para la entidad Persona única
 */
@Service
@Slf4j
public class PersonaDetailsServiceImpl implements UserDetailsService {

    private final PersonaRepository personaRepository;

    public PersonaDetailsServiceImpl(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

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
     * Crea UserDetails a partir de una Persona
     * SINGLE_TABLE: Usa los roles directamente de la entidad Persona
     */
    private UserDetails createUserDetails(Persona persona) {
        Collection<GrantedAuthority> authorities = persona.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.name()))
                .collect(Collectors.toList());

        log.debug("Autoridades asignadas para {}: {}",
                persona.getEmail(), authorities);

        return User.builder()
                .username(persona.getEmail())
                .password(persona.getPassword())
                .authorities(authorities)
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