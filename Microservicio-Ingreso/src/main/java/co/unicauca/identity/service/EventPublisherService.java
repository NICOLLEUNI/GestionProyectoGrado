package co.unicauca.identity.service;

import co.unicauca.identity.entity.Persona;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Servicio para publicación de eventos asíncronos
 * NO CAMBIA con SINGLE_TABLE - Mismo funcionamiento
 */
@Service
@Slf4j
public class EventPublisherService {

    public void publishUserRegisteredEvent(Persona persona) {
        log.info("📧 EVENTO SINGLE_TABLE: Usuario registrado - ID: {}, Email: {}, Roles: {}",
                persona.getIdUsuario(), persona.getEmail(), persona.getRoles());

        // Estructura preparada para RabbitMQ/Kafka futura
    }

    public void publishLoginSuccessEvent(Persona persona) {
        log.info("🔐 EVENTO SINGLE_TABLE: Login exitoso - Email: {}", persona.getEmail());
    }

    public void publishLoginFailedEvent(String email) {
        log.warn("🚫 EVENTO SINGLE_TABLE: Intento de login fallido - Email: {}", email);
    }
}