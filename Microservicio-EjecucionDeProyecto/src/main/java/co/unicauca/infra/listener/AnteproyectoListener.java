package co.unicauca.infra.listener;

import co.unicauca.entity.AnteproyectoEntity;
import co.unicauca.infra.config.RabbitMQConfig;
import co.unicauca.infra.dto.AnteproyectoResponse;
import co.unicauca.service.AnteproyectoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class AnteproyectoListener {


    private AnteproyectoService anteproyectoService;

    public AnteproyectoListener(AnteproyectoService anteproyectoService) {
        this.anteproyectoService = anteproyectoService;
    }

    @RabbitListener(queues = RabbitMQConfig.ANTEPROYECTO_QUEUE)  // Cola que envía el microservicio de evaluación
    public void handleAnteproyectoResponse(AnteproyectoResponse request) {
        // Llamar al servicio para guardar el Anteproyecto
        System.out.println("📩 Mensaje recibido en anteproyecto.queue: " + request);
        AnteproyectoEntity anteproyecto = anteproyectoService.saveInterno(request);
        System.out.println("✅ Anteproyecto guardado con ID: " + anteproyecto.getId());
    }
}
