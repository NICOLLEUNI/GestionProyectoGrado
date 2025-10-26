package co.unicauca.infra.listener;

import co.unicauca.infra.config.RabbitMQConfig;
import co.unicauca.infra.dto.AnteproyectoRequest;
import co.unicauca.service.AnteproyectoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AnteproyectoListener {

    private static final Logger logger = LoggerFactory.getLogger(AnteproyectoListener.class);
    private final AnteproyectoService anteproyectoService;

    public AnteproyectoListener(AnteproyectoService anteproyectoService) {
        this.anteproyectoService = anteproyectoService;
    }

    @RabbitListener(queues = RabbitMQConfig.ANTEPROYECTO_EVALUACION_QUEUE)
    public void receiveAnteproyecto(AnteproyectoRequest anteproyectoRequest) {
        logger.info("📥 [ANTEPROYECTO] Mensaje recibido: {}", anteproyectoRequest);

        try {
            // Crear o actualizar el anteproyecto
            anteproyectoService.crearAnteproyecto(anteproyectoRequest);
            logger.info("✅ [ANTEPROYECTO] Anteproyecto procesado exitosamente: {}", anteproyectoRequest.id());
        } catch (Exception e) {
            logger.error("❌ [ANTEPROYECTO] Error procesando anteproyecto: {}", e.getMessage(), e);
        }
    }

    // Opcional: También puedes escuchar en la cola de notificaciones si es necesario
    @RabbitListener(queues = RabbitMQConfig.ANTEPROYECTO_NOTIFICACIONES_QUEUE)
    public void receiveAnteproyectoNotificaciones(AnteproyectoRequest anteproyectoRequest) {
        logger.info("📥 [ANTEPROYECTO-NOTIF] Mensaje recibido para notificaciones: {}", anteproyectoRequest);
        // Lógica específica para notificaciones si es necesario
    }
}