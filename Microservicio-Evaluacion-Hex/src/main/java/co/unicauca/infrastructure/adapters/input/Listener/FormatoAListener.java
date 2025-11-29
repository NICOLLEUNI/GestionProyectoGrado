package co.unicauca.infrastructure.adapters.input.Listener;

import co.unicauca.application.ports.input.FormatoAFacadeInPort;
import co.unicauca.domain.entities.FormatoA;
import co.unicauca.infrastructure.config.RabbitMQConfig;
import co.unicauca.infrastructure.dto.request.FormatoARequest;
import co.unicauca.infrastructure.dto.response.FormatoAResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FormatoAListener {

    private final FormatoAFacadeInPort formatoAService;

    public FormatoAListener(FormatoAFacadeInPort formatoAService) {
        this.formatoAService = formatoAService;
    }

    @RabbitListener(queues = RabbitMQConfig.FORMATOA_EVALUACION_QUEUE)
    public void recibirFormatoA(FormatoARequest request) {

        System.out.println("Mensaje recibido en formatoa.queue: " + request);

        FormatoA formatoA = formatoAService.crearFormatoA(request);

        System.out.println("Formato A guardado con ID: " + formatoA.getId());
    }
}
