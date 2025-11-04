package co.unicauca.identity.service;

import co.unicauca.identity.messaging.dto.UsuarioMessage;
import co.unicauca.identity.config.RabbitMQConfig;
import co.unicauca.identity.entity.Persona;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Servicio de publicación para 4 MICROSERVICIOS CONSUMIDORES
 * Envía mensajes a todas las colas SIN TypeId header
 */
@Service
@Slf4j
public class EventPublisherService {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    // ✅ LISTA DE LAS 4 COLAS PARA LOS MICROSERVICIOS CONSUMIDORES
    private final List<String> todasLasColas = Arrays.asList(
            RabbitMQConfig.USUARIO_QUEUE,
            RabbitMQConfig.NOTIFICACION_QUEUE,
            RabbitMQConfig.REPORTES_QUEUE,
            RabbitMQConfig.AUDITORIA_QUEUE
    );

    public EventPublisherService(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        log.info("✅ EventPublisherService inicializado con {} colas", todasLasColas.size());
    }

    /**
     * ✅ CORREGIDO: Publica evento de usuario registrado a las 4 colas
     */
    public void publishUserRegisteredEvent(Persona persona) {
        try {
            UsuarioMessage usuarioMessage = UsuarioMessage.fromEntity(persona);
            Message message = createMessageWithoutTypeId(usuarioMessage);

            // ✅ ENVIAR A LAS 4 COLAS
            enviarATodasLasColas(message, persona, "REGISTRO");

            log.info("✅ USUARIO REGISTRADO ENVIADO A {} COLAS: ID={}, Email={}",
                    todasLasColas.size(), persona.getIdUsuario(), persona.getEmail());

        } catch (Exception e) {
            log.error("❌ ERROR enviando usuario a las colas: {}", e.getMessage(), e);
            log.warn("📧 FALLBACK - Usuario registrado (sin RabbitMQ): {}", persona.getEmail());
        }
    }

    /**
     * ✅ CORREGIDO: Publica evento de login exitoso a las 4 colas
     */
    public void publishLoginSuccessEvent(Persona persona) {
        try {
            UsuarioMessage usuarioMessage = UsuarioMessage.fromEntity(persona);
            Message message = createMessageWithoutTypeId(usuarioMessage);

            // ✅ ENVIAR A LAS 4 COLAS
            enviarATodasLasColas(message, persona, "LOGIN_EXITOSO");

            log.info("✅ LOGIN EXITOSO ENVIADO A {} COLAS: Email={}",
                    todasLasColas.size(), persona.getEmail());

        } catch (Exception e) {
            log.error("❌ ERROR enviando evento de login exitoso: {}", e.getMessage(), e);
        }
    }

    /**
     * ✅ CORREGIDO: Publica evento de login fallido a las 4 colas
     */
    public void publishLoginFailedEvent(String email) {
        try {
            // ✅ USAR UsuarioMessage (NO PersonaRequest) - Compatible con todos los microservicios
            UsuarioMessage failedLogin = new UsuarioMessage(
                    null, // ID no disponible
                    "Unknown",
                    "User",
                    email,
                    java.util.Set.of(), // Roles vacíos
                    null,
                    null,
                    null
            );

            Message message = createMessageWithoutTypeId(failedLogin);

            // ✅ ENVIAR A LAS 4 COLAS
            for (String queue : todasLasColas) {
                try {
                    rabbitTemplate.send(queue, message);
                    log.debug("✅ LOGIN FALLIDO ENVIADO A COLA {}: Email={}", queue, email);
                } catch (Exception queueError) {
                    log.error("❌ ERROR enviando a cola {}: {}", queue, queueError.getMessage());
                }
            }

            log.info("✅ LOGIN FALLIDO ENVIADO A {} COLAS: Email={}", todasLasColas.size(), email);

        } catch (Exception e) {
            log.error("❌ ERROR enviando evento de login fallido: {}", e.getMessage(), e);
        }
    }

    /**
     * ✅ CORREGIDO: Publica evento de actualización de usuario a las 4 colas
     */
    public void publishUserUpdatedEvent(Persona persona) {
        try {
            UsuarioMessage usuarioMessage = UsuarioMessage.fromEntity(persona);
            Message message = createMessageWithoutTypeId(usuarioMessage);

            // ✅ ENVIAR A LAS 4 COLAS
            enviarATodasLasColas(message, persona, "ACTUALIZACION");

            log.info("✅ ACTUALIZACIÓN USUARIO ENVIADA A {} COLAS: ID={}, Email={}",
                    todasLasColas.size(), persona.getIdUsuario(), persona.getEmail());

        } catch (Exception e) {
            log.error("❌ ERROR enviando actualización de usuario: {}", e.getMessage(), e);
        }
    }

    /**
     * ✅ NUEVO: Método centralizado para enviar a todas las colas
     */
    private void enviarATodasLasColas(Message message, Persona persona, String tipoEvento) {
        int exitosas = 0;
        int fallidas = 0;

        for (String queue : todasLasColas) {
            try {
                rabbitTemplate.send(queue, message);
                exitosas++;
                log.debug("✅ {} ENVIADO A COLA {}: ID={}", tipoEvento, queue, persona.getIdUsuario());
            } catch (Exception queueError) {
                fallidas++;
                log.error("❌ ERROR enviando {} a cola {}: {}", tipoEvento, queue, queueError.getMessage());
            }
        }

        if (fallidas > 0) {
            log.warn("📊 RESUMEN {}: {} exitosas, {} fallidas - ID={}",
                    tipoEvento, exitosas, fallidas, persona.getIdUsuario());
        } else {
            log.info("📊 RESUMEN {}: Todas las {} colas exitosas - ID={}",
                    tipoEvento, exitosas, persona.getIdUsuario());
        }
    }

    /**
     * ✅ ÚNICO MÉTODO: Crear mensaje SIN __TypeId__ header - Compatible con todos los microservicios
     */
    private Message createMessageWithoutTypeId(Object object) throws Exception {
        byte[] jsonBytes = objectMapper.writeValueAsBytes(object);

        return MessageBuilder
                .withBody(jsonBytes)
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setContentEncoding("UTF-8")
                // ❌ NO INCLUIR TypeId header para máxima compatibilidad
                .build();
    }

    /**
     * ✅ MEJORADO: Verificar conexión RabbitMQ para todas las colas
     */
    public boolean isRabbitMQAvailable() {
        log.info("🔍 Verificando conectividad con {} colas RabbitMQ...", todasLasColas.size());

        boolean todasDisponibles = true;
        for (String queue : todasLasColas) {
            try {
                rabbitTemplate.execute(channel -> {
                    channel.queueDeclarePassive(queue);
                    return null;
                });
                log.debug("✅ Cola {} disponible", queue);
            } catch (Exception e) {
                log.error("❌ Cola {} no disponible: {}", queue, e.getMessage());
                todasDisponibles = false;
            }
        }

        log.info("📊 Estado RabbitMQ: {}", todasDisponibles ? "TODAS LAS COLAS DISPONIBLES" : "ALGUNAS COLAS CON PROBLEMAS");
        return todasDisponibles;
    }

    /**
     * ✅ NUEVO: Método para obtener información de las colas configuradas
     */
    public void logConfiguracionColas() {
        log.info("🔧 CONFIGURACIÓN RABBITMQ - {} colas activas:", todasLasColas.size());
        for (int i = 0; i < todasLasColas.size(); i++) {
            log.info("   {}. {} → Microservicio {}",
                    i + 1,
                    todasLasColas.get(i),
                    obtenerNombreMicroservicio(todasLasColas.get(i)));
        }
    }

    /**
     * ✅ NUEVO: Método auxiliar para nombres descriptivos de microservicios
     */
    private String obtenerNombreMicroservicio(String queueName) {
        switch (queueName) {
            case RabbitMQConfig.USUARIO_QUEUE:
                return "Gestión de Usuarios";
            case RabbitMQConfig.NOTIFICACION_QUEUE:
                return "Sistema de Notificaciones";
            case RabbitMQConfig.REPORTES_QUEUE:
                return "Generador de Reportes";
            case RabbitMQConfig.AUDITORIA_QUEUE:
                return "Sistema de Auditoría";
            default:
                return "Desconocido";
        }
    }
}