package co.unicauca.identity.config;

import co.unicauca.identity.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


/**
 * SecurityConfig
 * ---------------------------------------------------------------------
 * Esta clase define toda la configuración de seguridad del microservicio.
 *
 * Aquí se especifica:
 *  - Qué endpoints son públicos
 *  - Qué endpoints requieren autenticación JWT
 *  - Qué filtros se aplican y en qué orden
 *  - Uso de sesiones sin estado (STATELESS)
 *  - Configuración de CORS para permitir peticiones de frontends
 *  - Registro del PasswordEncoder (BCrypt)
 *  - Registro del AuthenticationManager (maneja login)
 *
 * Es la pieza central que integra:
 *  JwtAuthenticationFilter + JwtTokenProvider + UserDetailsService
 *
 * En otras palabras:
 *  ES LA COLUMNA VERTEBRAL DE TODA LA SEGURIDAD.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RateLimitingConfig rateLimitingConfig;


    /**
     * SecurityFilterChain:
     * -------------------------------------------------------------
     * Define la cadena de filtros y reglas de autorización.
     *
     * Resumen del comportamiento:
     *  - CORS habilitado
     *  - CSRF deshabilitado (porque usamos JWT)
     *  - Sesión STATELESS (cada petición debe traer token)
     *  - Endpoints públicos configurados
     *  - Todas las demás rutas requieren JWT válido
     *  - Se agrega JwtAuthenticationFilter ANTES del filtro por defecto
     *  - Se agrega RateLimiting DESPUÉS del filtro JWT
     */


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Habilita CORS para permitir que frontends accedan al backend
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
// Deshabilita CSRF porque usamos JWT (no cookies de sesión)
                .csrf(csrf -> csrf.disable())
                // Todas las solicitudes son STATELESS: no hay sesiones en servidor
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Reglas de autorización para cada endpoint
                .authorizeHttpRequests(auth -> auth

                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                // Endpoints públicos  (no requieren JWT)
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login",
                                "/api/auth/roles",
                                "/api/auth/verify-token"
                        ).permitAll()

                                // Swagger UI — necesario para documentación
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api-docs/**",
                                "/swagger-resources/**",
                                "/swagger-ui/index.html",
                                "/webjars/**"
                        ).permitAll()
//se agrego    // Perfil requiere persona autenticada
                                .requestMatchers("/api/auth/profile").authenticated()

                        // H2 console
                        .requestMatchers("/h2-console/**").permitAll()
              //se agrego


                        // Health checks
                        .requestMatchers(
                                "/actuator/health",
                                "/actuator/info"
                        ).permitAll()

                        // Todo lo demás requiere autenticación
                        .anyRequest().authenticated()
                )
                // Inserta el filtro de JWT ANTES del filtro estándar de login
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // Filtro de rate limiting después del JWT
                .addFilterAfter(rateLimitingConfig.rateLimitingFilter(), JwtAuthenticationFilter.class)

                // permitir que H2 se renderice en iframe (same origin)
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    /**
     * Configuración de CORS:
     * Permite peticiones desde dominios específicos (frontends)
     */

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",  // React frontend
                "http://localhost:3001",  // Otro frontend
                "http://localhost:4200"   // Angular frontend
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers",
                "X-Total-Count"
        ));
        configuration.setExposedHeaders(Arrays.asList(
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials",
                "X-Total-Count",
                "X-Page-Number",
                "X-Page-Size",
                "X-Total-Pages"
        ));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // 1 hora

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    /**
     * Encoder de contraseñas:
     * BCrypt es estándar de seguridad moderno.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); // Strength 12 para mayor seguridad
    }
    /**
     * AuthenticationManager:
     * Necesario para el proceso de login tradicional (username/password).
     */

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}