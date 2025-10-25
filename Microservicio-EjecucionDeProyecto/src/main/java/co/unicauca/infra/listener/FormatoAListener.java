package co.unicauca.infra.listener;

import co.unicauca.infra.config.RabbitMQConfig;
import co.unicauca.infra.dto.FormatoAUpdateResponse;
import co.unicauca.service.FormatoAVersionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component  // Cambiado a @Service para indicar que es un servicio que maneja la lógica
public class FormatoAListener {


    private FormatoAVersionService formatoAVersionService;

    /**
     * Método que maneja la actualización de FormatoA.
     * @param response Respuesta de FormatoAUpdateResponse
     */
    @RabbitListener(queues = RabbitMQConfig.FORMATOA_EVALUADO_QUEUE) // Cola donde llega el mensaje
    public void handleFormatoAUpdateResponse(FormatoAUpdateResponse response) {
        // Delegar la lógica de negocio al service
        formatoAVersionService.saveInterno(response);
    }
}
