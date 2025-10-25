package co.unicauca.infra.listener;

import co.unicauca.entity.FormatoA;
import co.unicauca.infra.config.RabbitMQConfig;
import co.unicauca.infra.dto.FormatoARequest;
import co.unicauca.service.FormatoAService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FormatoAListener {

    private final FormatoAService formatoAService;

    public FormatoAListener(FormatoAService formatoAService) {
        this.formatoAService = formatoAService;
    }

    /**
     * Escucha los mensajes del exchange/cola definidos en RabbitMQConfig para FormatoA.
     * Cuando se recibe un mensaje con los datos de FormatoARequest, se guarda en la base de datos.
     */
    @RabbitListener(queues = RabbitMQConfig.FORMATOA_CREADO_QUEUE)
    public void recibirFormatoA(FormatoARequest request) {
        System.out.println("📩 Mensaje recibido en formatoa.queue: " + request);
        FormatoA formatoA = formatoAService.guardarFormatoA(request);
        System.out.println("✅ Formato A guardado con ID: " + formatoA.getId());
    }
}

