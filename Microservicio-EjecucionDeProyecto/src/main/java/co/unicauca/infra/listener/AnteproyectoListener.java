package co.unicauca.infra.listener;

import co.unicauca.infra.config.RabbitMQConfig;
import co.unicauca.infra.dto.AnteproyectoResponse;
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
    public void receiveAnteproyecto(AnteproyectoResponse anteproyectoResponse) {
        logger.info("📥 [ANTEPROYECTO] Mensaje recibido: {}", anteproyectoResponse.titulo());

        try {
            // ✅ Procesar el AnteproyectoResponse recibido
            anteproyectoService.procesarAnteproyectoRecibido(anteproyectoResponse);
            logger.info("✅ [ANTEPROYECTO] Anteproyecto procesado exitosamente: {}", anteproyectoResponse.titulo());
        } catch (Exception e) {
            logger.error("❌ [ANTEPROYECTO] Error procesando anteproyecto: {}", e.getMessage(), e);
        }
    }
}