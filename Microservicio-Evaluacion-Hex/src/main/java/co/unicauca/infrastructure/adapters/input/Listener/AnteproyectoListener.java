package co.unicauca.infrastructure.adapters.input.Listener;

import co.unicauca.application.ports.input.AnteproyectoFacadeInPort;
import co.unicauca.domain.entities.Anteproyecto;
import co.unicauca.infrastructure.config.RabbitMQConfig;
import co.unicauca.infrastructure.dto.request.AnteproyectoRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class    AnteproyectoListener {

    private final AnteproyectoFacadeInPort anteproyectoService;

    public AnteproyectoListener( AnteproyectoFacadeInPort anteproyectoService) {
        this.anteproyectoService = anteproyectoService;
    }

    /**
     * Escucha los mensajes de la cola "anteproyecto.queue" definidos en RabbitMQConfig.
     * Cuando se recibe un mensaje con los datos de un AnteproyectoRequest, se guarda en la base de datos.
     */
    @RabbitListener(queues = RabbitMQConfig.ANTEPROYECTO_EVALUACION_QUEUE)
    public void recibirAnteproyecto(AnteproyectoRequest request) {
        System.out.println("📩 Mensaje recibido en anteproyecto.queue: " + request);
        Anteproyecto anteproyecto = anteproyectoService.crearAnteproyecto(request);
        System.out.println("✅ Anteproyecto guardado con ID: " + anteproyecto.getId());
    }
}
