package co.unicauca.infra.messaging;

import co.unicauca.infra.dto.AnteproyectoResponse;
import co.unicauca.infra.dto.FormatoAResponse;
import co.unicauca.infra.dto.FormatoAVersionResponse;
import co.unicauca.infra.dto.ProyectoGradoResponse;
import co.unicauca.infra.dto.notification.AnteproyectoNotification;
import co.unicauca.infra.dto.notification.FormatoAnotification;
import co.unicauca.infra.dto.notification.VersionNotification;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import co.unicauca.infra.config.RabbitMQConfig;


@Component
public class RabbitMQPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // ================== CON EXCHANGE (Múltiples consumidores) ==================

    public void publicarFormatoACreado(FormatoAResponse formatoAResponse) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.FORMATOA_CREADO_EXCHANGE,
                RabbitMQConfig.FORMATOA_CREADO_ROUTING_KEY,
                formatoAResponse
        );
        System.out.println("📤 FormatoAResponse publicado para EVALUACIÓN y NOTIFICACIONES");
    }

    public void publicarAnteproyectoCreado(AnteproyectoResponse anteproyectoResponse) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ANTEPROYECTO_CREADO_EXCHANGE,
                RabbitMQConfig.ANTEPROYECTO_CREADO_ROUTING_KEY,
                anteproyectoResponse
        );
        System.out.println("📤 AnteproyectoResponse publicado para EVALUACIÓN y NOTIFICACIONES");
    }

    // ================== CON COLAS DIRECTAS (Un solo consumidor) ==================

    public void publicarVersionCreada(FormatoAVersionResponse versionResponse) { // ← CAMBIADO A EXCHANGE
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.FORMATOAVERSION_CREADA_EXCHANGE,
                RabbitMQConfig.FORMATOAVERSION_CREADA_ROUTING_KEY,
                versionResponse
        );
        System.out.println("📤 FormatoAVersionResponse publicado para HISTÓRICO y NOTIFICACIONES");
    }

    public void publicarProyectoGradoCreado(ProyectoGradoResponse proyectoGradoResponse) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PROYECTO_GRADO_CREADO_QUEUE,
                proyectoGradoResponse
        );
        System.out.println("📤 ProyectoGradoResponse publicado: " + proyectoGradoResponse.id());
    }

    public void publicarNotificacionFormatoACreado(FormatoAnotification evento) {
        rabbitTemplate.convertAndSend(
                "formatoa.notificaciones.queue", // Cola específica para notificaciones
                evento
        );
        System.out.println("📧 Notificación FormatoA publicada: " + evento.formatoAId());
    }

    public void publicarNotificacionAnteproyectoCreado(AnteproyectoNotification evento) {
        rabbitTemplate.convertAndSend(
                "anteproyecto.notificaciones.queue",
                evento
        );
        System.out.println("📧 Notificación Anteproyecto publicada: " + evento.anteproyectoId());
    }

    public void publicarNotificacionVersionCreada(VersionNotification evento) {
        rabbitTemplate.convertAndSend(
                "formatoaversion.notificaciones.queue",
                evento
        );
        System.out.println("📧 Notificación Versión publicada: " + evento.versionId());
    }
}