package co.unicauca.infra.listener;

import co.unicauca.entity.ProyectoGradoEntity;
import co.unicauca.infra.config.RabbitMQConfig;
import co.unicauca.infra.dto.ProyectoGradoResponse;
import co.unicauca.service.PersonaService;
import co.unicauca.service.ProyectoGradoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProyectoGradoListener {


    private ProyectoGradoService proyectoGradoService;

    public ProyectoGradoListener(ProyectoGradoService proyectoGradoService) {
        this.proyectoGradoService = proyectoGradoService;
    }

    /**
     * Método que maneja la creación de ProyectoGrado.
     * @param request Respuesta de ProyectoGradoResponse
     */
    @RabbitListener(queues = RabbitMQConfig.FORMATOA_QUEUE)  // Cola donde llega el mensaje
    public void handleProyectoGradoResponse(ProyectoGradoResponse request) {

        System.out.println("📩 Mensaje recibido en por definir: " + request);
        ProyectoGradoEntity proyecto = proyectoGradoService.saveInterno(request);
        System.out.println("✅ Formato A guardado con ID: " + proyecto.getId());
        proyectoGradoService.saveInterno(request);
    }
}
