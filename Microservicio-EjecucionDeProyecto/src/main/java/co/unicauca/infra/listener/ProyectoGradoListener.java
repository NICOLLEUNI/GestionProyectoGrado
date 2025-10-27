package co.unicauca.infra.listener;

import co.unicauca.infra.config.RabbitMQConfig;
import co.unicauca.infra.dto.ProyectoGradoResponse;
import co.unicauca.service.ProyectoGradoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ProyectoGradoListener {

    private static final Logger logger = LoggerFactory.getLogger(ProyectoGradoListener.class);
    private final ProyectoGradoService proyectoGradoService;

    public ProyectoGradoListener(ProyectoGradoService proyectoGradoService) {
        this.proyectoGradoService = proyectoGradoService;
    }

    @RabbitListener(queues = RabbitMQConfig.ANTEPROYECTO_EVALUACION_QUEUE)
    public void receiveProyectoGrado(ProyectoGradoResponse proyectoResponse) {
        logger.info("📥 [PROYECTO_GRADO] Mensaje recibido: {}", proyectoResponse.nombre());

        try {
            // ✅ Usar el Service directamente
            proyectoGradoService.procesarProyectoRecibido(proyectoResponse);
            logger.info("✅ [PROYECTO_GRADO] Proyecto procesado exitosamente: {}", proyectoResponse.nombre());
        } catch (Exception e) {
            logger.error("❌ [PROYECTO_GRADO] Error procesando proyecto: {}", e.getMessage(), e);
        }
    }
}