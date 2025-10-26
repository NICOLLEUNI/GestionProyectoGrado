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

    @RabbitListener(queues = RabbitMQConfig.COLA_ANTEPROYECTO)
    public void receiveAnteproyecto(AnteproyectoRequest anteproyectoRequest) {
        logger.info("📥 [ANTEPROYECTO] Mensaje recibido: {}", anteproyectoRequest);

        try {
            // ✅ CAMBIADO: Usar crearOActualizar en lugar de crear
            anteproyectoService.crearOActualizarAnteproyecto(anteproyectoRequest);
            logger.info("✅ [ANTEPROYECTO] Anteproyecto procesado exitosamente (creado o actualizado)");
        } catch (Exception e) {
            logger.error("❌ [ANTEPROYECTO] Error procesando anteproyecto: {}", e.getMessage(), e);
        }
    }
}