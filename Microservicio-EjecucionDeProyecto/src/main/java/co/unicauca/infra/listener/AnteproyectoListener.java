package co.unicauca.infra.listener;

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

    @RabbitListener(queues = RabbitMQConfig.ANTEPROYECTO_QUEUE)  // Cola que envía el microservicio de evaluación
    public void handleAnteproyectoResponse(AnteproyectoResponse response) {
        // Llamar al servicio para guardar el Anteproyecto
        anteproyectoService.saveInterno(response);
    }
}
