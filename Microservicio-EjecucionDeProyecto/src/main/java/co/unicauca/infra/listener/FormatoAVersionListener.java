package co.unicauca.infra.listener;

import co.unicauca.infra.config.RabbitMQConfig;
import co.unicauca.infra.dto.FormatoAVersionResponse;
import co.unicauca.service.facade.FormatoAVersionFacade;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class FormatoAVersionListener {

    private static final Logger logger = LoggerFactory.getLogger(FormatoAVersionListener.class);
    private final FormatoAVersionFacade formatoAVersionFacade;

    public FormatoAVersionListener(FormatoAVersionFacade formatoAVersionFacade) {
        this.formatoAVersionFacade = formatoAVersionFacade;
    }

    @RabbitListener(queues = RabbitMQConfig.FORMATOAVERSION_HISTORICO_QUEUE)
    public void receiveFormatoAVersion(FormatoAVersionResponse versionResponse) {
        logger.info("📥 [LISTENER] Mensaje recibido de FormatoAVersion: {}", versionResponse);

        try {
            // Delegar toda la lógica a la Facade
            formatoAVersionFacade.procesarVersionRecibida(versionResponse);

            logger.info("✅ [LISTENER] Versión {} procesada exitosamente", versionResponse.numVersion());
        } catch (Exception e) {
            logger.error("❌ [LISTENER] Error procesando versión: {}", e.getMessage(), e);
        }
    }
}