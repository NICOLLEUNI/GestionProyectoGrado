package co.unicauca.identity.service;

import co.unicauca.identity.config.RabbitMQConfig;
import co.unicauca.identity.entity.Persona;
import co.unicauca.identity.messaging.dto.PersonaRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * Servicio de publicación COMPATIBLE con el microservicio de Notificación
 * Envía mensajes a la MISMA COLA que el microservicio de Notificación espera
 */
@Service
@Slf4j
public class EventPublisherService {

    private final RabbitTemplate rabbitTemplate;

    public EventPublisherService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Publica evento de usuario registrado a la COLA COMPARTIDA
     * El microservicio de Notificación escucha esta misma cola
     */
    public void publishUserRegisteredEvent(Persona persona) {
        try {
            // ✅ Crear DTO COMPATIBLE con el microservicio de Notificación
            PersonaRequest personaRequest = PersonaRequest.fromEntity(persona);

            // ✅ Enviar a la MISMA COLA que el microservicio de Notificación
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.USUARIO_QUEUE,
                    personaRequest
            );

            log.info("✅ USUARIO ENVIADO A COLA COMPARTIDA: ID={}, Email={}, Roles={}",
                    persona.getIdUsuario(), persona.getEmail(), persona.getRoles());

        } catch (Exception e) {
            log.error("❌ ERROR enviando usuario a cola compartida: {}", e.getMessage(), e);
            // Fallback: Log local sin interrumpir el flujo principal
            log.warn("📧 FALLBACK - Usuario registrado (sin RabbitMQ): {}", persona.getEmail());
        }
    }

    /**
     * Publica evento de login exitoso (si es necesario para otros microservicios)
     */
    public void publishLoginSuccessEvent(Persona persona) {
        try {
            PersonaRequest personaRequest = PersonaRequest.fromEntity(persona);

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.USUARIO_QUEUE,
                    personaRequest
            );

            log.info("✅ LOGIN EXITOSO ENVIADO A COLA: Email={}", persona.getEmail());

        } catch (Exception e) {
            log.error("❌ ERROR enviando evento de login: {}", e.getMessage(), e);
            log.info("🔐 FALLBACK - Login exitoso: {}", persona.getEmail());
        }
    }

    /**
     * Publica evento de login fallido (para analytics/monitoring)
     */
    public void publishLoginFailedEvent(String email) {
        try {
            // Crear un DTO básico para login fallido
            PersonaRequest failedLogin = new PersonaRequest(
                    null, // ID no disponible
                    "Unknown",
                    "User",
                    email,
                    java.util.Set.of(), // Roles vacíos
                    null,
                    null
            );

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.USUARIO_QUEUE,
                    failedLogin
            );

            log.info("✅ LOGIN FALLIDO ENVIADO A COLA: Email={}", email);

        } catch (Exception e) {
            log.error("❌ ERROR enviando evento de login fallido: {}", e.getMessage(), e);
            log.warn("🚫 FALLBACK - Login fallido: {}", email);
        }
    }

    /**
     * Publica evento de actualización de usuario
     */
    public void publishUserUpdatedEvent(Persona persona) {
        try {
            PersonaRequest personaRequest = PersonaRequest.fromEntity(persona);

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.USUARIO_QUEUE,
                    personaRequest
            );

            log.info("✅ ACTUALIZACIÓN USUARIO ENVIADA A COLA: ID={}, Email={}",
                    persona.getIdUsuario(), persona.getEmail());

        } catch (Exception e) {
            log.error("❌ ERROR enviando actualización de usuario: {}", e.getMessage(), e);
            log.info("📝 FALLBACK - Usuario actualizado: {}", persona.getEmail());
        }
    }
}