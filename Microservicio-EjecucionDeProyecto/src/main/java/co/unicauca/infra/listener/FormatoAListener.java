package co.unicauca.infra.listener;

import co.unicauca.entity.FormatoAVersionEntity;
import co.unicauca.infra.config.RabbitMQConfig;
import co.unicauca.infra.dto.FormatoAUpdateRequest;
import co.unicauca.infra.dto.FormatoAUpdateResponse;
import co.unicauca.service.FormatoAService;
import co.unicauca.service.FormatoAVersionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component  // Cambiado a @Service para indicar que es un servicio que maneja la lógica
public class FormatoAListener {


    private FormatoAVersionService formatoAVersionService;
    public FormatoAListener(FormatoAVersionService formatoAVersionService) {
        this.formatoAVersionService = formatoAVersionService;
    }

    /**
     * Método que maneja la actualización de FormatoA.
     * @param request Respuesta de FormatoAUpdateResponse
     */
    @RabbitListener(queues = RabbitMQConfig.FORMATOA_QUEUE) // Cola donde llega el mensaje
    public void handleFormatoAUpdateResponse(FormatoAUpdateRequest request) {
        System.out.println("📩 Mensaje recibido en formatoa.evaluado.queue: " + request);
        FormatoAVersionEntity formatoA = formatoAVersionService.saveInterno(request);
        System.out.println("✅ Formato A guardado con ID: " + formatoA.getId());
        formatoAVersionService.saveInterno(request);
    }
}
