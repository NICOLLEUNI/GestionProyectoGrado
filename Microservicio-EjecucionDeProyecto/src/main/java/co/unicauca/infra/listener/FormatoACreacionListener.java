package co.unicauca.infra.listener;

import co.unicauca.entity.FormatoAEntity;
import co.unicauca.infra.config.RabbitMQConfig;
import co.unicauca.infra.dto.FormatoARequest;
import co.unicauca.infra.dto.FormatoAResponse;
import co.unicauca.service.FormatoAService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormatoACreacionListener {


    private FormatoAService formatoAService;
    public FormatoACreacionListener(FormatoAService formatoAService) {
        this.formatoAService = formatoAService;
    }


    /**
     * Listener para manejar la creación de un nuevo FormatoA.
     * Recibe el mensaje de creación de FormatoA y lo procesa a través del servicio.
     */
    @RabbitListener(queues = RabbitMQConfig.FORMATOA_QUEUE) // Cola donde llega el mensaje de creación de FormatoA
    public void handleFormatoACreacionResponse(FormatoARequest request) {
        System.out.println("📩 Mensaje recibido en formatoa.queue: " + request);
        FormatoAEntity formatoA = formatoAService.saveFormatoA(request);
        System.out.println("✅ Formato A guardado con ID: " + formatoA.getId());
        formatoAService.saveFormatoA(request);
    }
}
