package co.unicauca.identity.service;
import co.unicauca.identity.messaging.dto.UsuarioMessage;
import co.unicauca.identity.config.RabbitMQConfig;
import co.unicauca.identity.entity.Persona;
import co.unicauca.identity.messaging.dto.PersonaRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * Servicio de publicación COMPATIBLE con TODOS los microservicios
 * Envía mensajes SIN TypeId header para evitar conflictos de paquetes
 */
@Service
@Slf4j
public class EventPublisherService {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public EventPublisherService(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Publica evento de usuario registrado SIN TypeId header
     * Compatible con cualquier microservicio que tenga PersonaRequest
     */

    public void publishUserRegisteredEvent(Persona persona) {
        try {
            // ✅ USAR NUEVO DTO ESPECÍFICO
            UsuarioMessage usuarioMessage = UsuarioMessage.fromEntity(persona);
            String json = objectMapper.writeValueAsString(usuarioMessage);
            // ✅ CREAR MENSAJE SIN TYPE_ID HEADER
            Message message = createMessageWithoutTypeId(usuarioMessage);

            // ✅ ENVIAR A LA COLA COMPARTIDA
            rabbitTemplate.send(RabbitMQConfig.USUARIO_QUEUE, message);

            log.info("✅ USUARIO ENVIADO A COLA (USUARIO_MESSAGE): ID={}, Email={}, Department={}, Programa={}",
                    persona.getIdUsuario(), persona.getEmail(),
                    usuarioMessage.department(), usuarioMessage.programa());

        } catch (Exception e) {
            log.error("❌ ERROR enviando usuario a cola compartida: {}", e.getMessage(), e);
            log.warn("📧 FALLBACK - Usuario registrado (sin RabbitMQ): {}", persona.getEmail());
        }
    }




    /**
     * ✅ MÉTODO NUEVO: Crear mensaje SIN __TypeId__ header
     */
    private Message createMessageWithoutTypeId(Object object) throws Exception {
        byte[] jsonBytes = objectMapper.writeValueAsBytes(object);

        return MessageBuilder
                .withBody(jsonBytes)
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setContentEncoding("UTF-8")
                // ❌ NO INCLUIR TypeId header
                .build();
    }

    /**
     * Publica evento de login exitoso SIN TypeId header
     */

    public void publishLoginSuccessEvent(Persona persona) {
        try {
            UsuarioMessage usuarioMessage = UsuarioMessage.fromEntity(persona);
            Message message = createMessageWithoutTypeId(usuarioMessage);

            rabbitTemplate.send(RabbitMQConfig.USUARIO_QUEUE, message);

            log.info("✅ LOGIN EXITOSO ENVIADO A COLA: Email={}", persona.getEmail());

        } catch (Exception e) {
            log.error("❌ ERROR enviando evento de login: {}", e.getMessage(), e);
        }
    }
    /**
     * Publica evento de login fallido SIN TypeId header
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

            Message message = createMessageWithoutTypeId(failedLogin);
            rabbitTemplate.send(RabbitMQConfig.USUARIO_QUEUE, message);

            log.info("✅ LOGIN FALLIDO ENVIADO A COLA (SIN TYPE_ID): Email={}", email);

        } catch (Exception e) {
            log.error("❌ ERROR enviando evento de login fallido: {}", e.getMessage(), e);
            log.warn("🚫 FALLBACK - Login fallido: {}", email);
        }
    }

    /**
     * Publica evento de actualización de usuario SIN TypeId header
     */
    public void publishUserUpdatedEvent(Persona persona) {
        try {
            PersonaRequest personaRequest = PersonaRequest.fromEntity(persona);
            Message message = createMessageWithoutTypeId(personaRequest);

            rabbitTemplate.send(RabbitMQConfig.USUARIO_QUEUE, message);

            log.info("✅ ACTUALIZACIÓN USUARIO ENVIADA A COLA (SIN TYPE_ID): ID={}, Email={}",
                    persona.getIdUsuario(), persona.getEmail());

        } catch (Exception e) {
            log.error("❌ ERROR enviando actualización de usuario: {}", e.getMessage(), e);
            log.info("📝 FALLBACK - Usuario actualizado: {}", persona.getEmail());
        }
    }

    /**
     * ✅ MÉTODO NUEVO: Crear mensaje SIN __TypeId__ header
     * Esto evita conflictos entre microservicios con diferentes paquetes
     */
    private Message createMessageWithoutTypeId(PersonaRequest personaRequest) throws Exception {
        // Convertir objeto a JSON bytes
        byte[] jsonBytes = objectMapper.writeValueAsBytes(personaRequest);

        // Crear mensaje con propiedades básicas, SIN TypeId header
        return MessageBuilder
                .withBody(jsonBytes)
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setContentEncoding("UTF-8")
                // ❌ NO INCLUIR: .setHeader("__TypeId__", "co.unicauca.identity.messaging.dto.PersonaRequest")
                .build();
    }

    /**
     * ✅ MÉTODO NUEVO: Verificar conexión RabbitMQ
     */
    public boolean isRabbitMQAvailable() {
        try {
            rabbitTemplate.execute(channel -> {
                channel.queueDeclarePassive(RabbitMQConfig.USUARIO_QUEUE);
                return null;
            });
            return true;
        } catch (Exception e) {
            log.warn("⚠️ RabbitMQ no disponible: {}", e.getMessage());
            return false;
        }
    }
}