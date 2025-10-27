package co.unicauca.infra.listener;

import co.unicauca.infra.config.RabbitMQConfig;
import co.unicauca.infra.dto.AnteproyectoResponse;
import co.unicauca.infra.dto.FormatoAVersionResponse;
import co.unicauca.service.FormatoAVersionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FormatoAVersionListener {

    private final FormatoAVersionService versionService;
    private static final Logger logger = LoggerFactory.getLogger(FormatoAVersionListener.class);

    /**
     * ✅ LISTENER PARA VERSIONES CREADAS DESDE OTROS MICROSERVICIOS
     */
    @RabbitListener(queues = RabbitMQConfig.FORMATOAVERSION_HISTORICO_QUEUE)
    public void recibirVersionCreada(FormatoAVersionResponse versionResponse) {
        logger.info("📥 [RABBITMQ] Versión recibida: {} - v{} para FormatoA: {}",
                versionResponse.titulo(), versionResponse.numVersion(), versionResponse.idFormatoA());

        try {
            versionService.procesarVersionRecibida(versionResponse);
            logger.info("✅ [RABBITMQ] Versión procesada exitosamente: v{}", versionResponse.numVersion());
        } catch (Exception e) {
            logger.error("❌ [RABBITMQ] Error procesando versión: {}", e.getMessage(), e);
            // Puedes implementar dead letter queue aquí si es necesario
        }

    }

}