package co.unicauca.infra.listener;

import co.unicauca.entity.Anteproyecto;
import co.unicauca.infra.config.RabbitMQConfig;
import co.unicauca.infra.dto.AnteproyectoRequest;
import co.unicauca.service.AnteproyectoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AnteproyectoListener {


    private final AnteproyectoService anteproyectoService;

    public AnteproyectoListener(AnteproyectoService anteproyectoService) {
        this.anteproyectoService = anteproyectoService;
    }

    /**
     * Escucha los mensajes de la cola "anteproyecto.queue" definidos en RabbitMQConfig.
     * Cuando se recibe un mensaje con los datos de un AnteproyectoRequest, se guarda en la base de datos.
     */
    @RabbitListener(queues = RabbitMQConfig.ANTEPROYECTO_CREADO_QUEUE)
    public void recibirAnteproyecto(AnteproyectoRequest request) {
        System.out.println("📩 Mensaje recibido en anteproyecto.queue: " + request);
        Anteproyecto anteproyecto = anteproyectoService.guardarAnteproyecto(request);
        System.out.println("✅ Anteproyecto guardado con ID: " + anteproyecto.getId());
    }
}

