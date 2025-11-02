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
    public void recibirVersion(DtoFormatoVersion evento) {
        try {
            // Convertimos al DTO que usa tu service
            DtoFormatoVersion versionRequest = new DtoFormatoVersion(
                    evento.versionId(),
                    evento.formatoAId(),
                    evento.numeroVersion(),
                    evento.estado(),
                    evento.estudiantesEmails(),
                    evento.directorEmail()
            );

            formatoAVersionService.procesarNotificacionVersion(versionRequest);

        } catch (Exception e) {
            System.err.println("❌ Error procesando evento FormatoA: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
