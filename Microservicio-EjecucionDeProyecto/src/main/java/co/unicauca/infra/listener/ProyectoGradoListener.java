package co.unicauca.infra.listener;

import co.unicauca.infra.config.RabbitMQConfig;
import co.unicauca.infra.dto.ProyectoGradoResponse;
import co.unicauca.service.ProyectoGradoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProyectoGradoListener {


    private ProyectoGradoService proyectoGradoService;

    /**
     * Método que maneja la creación de ProyectoGrado.
     * @param response Respuesta de ProyectoGradoResponse
     */
    @RabbitListener(queues = RabbitMQConfig.FORMATOA_QUEUE)  // Cola donde llega el mensaje
    public void handleProyectoGradoResponse(ProyectoGradoResponse response) {
        // Delegar la lógica al service para guardar el ProyectoGrado
        proyectoGradoService.saveInterno(response);
    }
}
