package co.unicauca.infra.listener;

import co.unicauca.infra.config.RabbitMQConfig;
import co.unicauca.infra.dto.DtoFormatoVersion;
import co.unicauca.infra.dto.PersonaRequest;
import co.unicauca.service.FormatoAVersionService;
import co.unicauca.service.PersonaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FormatoAVersionListener {
    private final ObjectMapper objectMapper;
    private final FormatoAVersionService formatoAVersionService;

    public FormatoAVersionListener(ObjectMapper objectMapper, FormatoAVersionService formatoAVersionService, FormatoAVersionService formatoAVersionService1) {
        this.objectMapper = objectMapper;
        this.formatoAVersionService = formatoAVersionService1;
    }

    /**
     * Escucha la cola de usuarios definida en RabbitMQConfig.
     * El mensaje llega en formato JSON, se convierte en PersonaRequest y se guarda.
     */
    @RabbitListener(queues = RabbitMQConfig.FORMATOAVERSION_NOTIFICACIONES_QUEUE)
    public void recibirVersion(String message) {
        try {
            // 🟢 Convertir el JSON a objeto DTO
            DtoFormatoVersion versionRequest = objectMapper.readValue(message, DtoFormatoVersion.class);

            // 🟢 Llamar al service con la instancia, no con la clase
            formatoAVersionService.procesarNotificacionVersion(versionRequest);

        } catch (Exception e) {
            System.err.println("❌ Error procesando mensaje de versión FormatoA: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
