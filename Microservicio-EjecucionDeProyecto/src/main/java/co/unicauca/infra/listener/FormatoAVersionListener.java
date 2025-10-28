package co.unicauca.infra.listener;

import co.unicauca.infra.config.RabbitMQConfig;
import co.unicauca.infra.dto.FormatoAVersionRequest;
import co.unicauca.service.FormatoAVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FormatoAVersionListener {

    private final FormatoAVersionService versionService;

    /**
     * ✅ LISTENER CORREGIDO - AHORA RECIBE FormatoAVersionRequest
     */
    @RabbitListener(queues = RabbitMQConfig.FORMATOAVERSION_HISTORICO_QUEUE)
    public void recibirVersionCreada(FormatoAVersionRequest versionRequest) {
        System.out.println("📥 [RABBITMQ] Versión Request recibida: " + versionRequest.titulo() +
                " - v" + versionRequest.numVersion() +
                " para FormatoA: " + versionRequest.idFormatoA());

        try {
            versionService.procesarVersionRecibida(versionRequest);
            System.out.println("✅ [RABBITMQ] Versión Request procesada exitosamente: v" + versionRequest.numVersion());
        } catch (Exception e) {
            System.out.println("❌ [RABBITMQ] Error procesando versión Request: " + e.getMessage());
            e.printStackTrace();
            // Puedes implementar dead letter queue aquí si es necesario
        }
    }
}