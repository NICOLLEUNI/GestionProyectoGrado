package co.unicauca.infra.listener;

import co.unicauca.infra.dto.AnteproyectoCreado;
import co.unicauca.service.AnteproyectoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnteproyectoListener {

    @Autowired
    private AnteproyectoService anteproyectoService;

    /**
     * Escucha los mensajes enviados a la cola "anteproyecto.notificaciones.queue"
     * y los procesa al recibir un nuevo anteproyecto creado.
     */
    @RabbitListener(queues = "anteproyecto.notificaciones.queue")
    public void recibirMensaje(AnteproyectoCreado evento) {
        anteproyectoService.procesarNotificacionCreado(evento);
    }
}
