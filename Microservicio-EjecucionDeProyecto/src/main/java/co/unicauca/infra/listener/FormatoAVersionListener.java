package co.unicauca.infra.listener;

import co.unicauca.infra.config.RabbitMQConfig;
import co.unicauca.infra.dto.FormatoAVersionResponse;
import co.unicauca.service.FormatoAVersionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class FormatoAVersionListener {

    private static final Logger logger = LoggerFactory.getLogger(FormatoAVersionListener.class);
    private final FormatoAVersionService formatoAVersionService;

    public FormatoAVersionListener(FormatoAVersionService formatoAVersionService) {
        this.formatoAVersionService = formatoAVersionService;
    }

    @RabbitListener(queues = RabbitMQConfig.COLA_FORMATO_A)
    public void receiveFormatoA(FormatoAVersionResponse formatoResponse) {
        logger.info("📥 [FORMATO_A] Mensaje recibido: {} - v{}",
                formatoResponse.title(), formatoResponse.numVersion());

        try {
            // ✅ Usar el Service directamente
            formatoAVersionService.procesarVersionRecibida(formatoResponse);
            logger.info("✅ [FORMATO_A] Versión procesada exitosamente: v{}", formatoResponse.numVersion());
        } catch (Exception e) {
            logger.error("❌ [FORMATO_A] Error procesando versión: {}", e.getMessage(), e);
        }
    }
}