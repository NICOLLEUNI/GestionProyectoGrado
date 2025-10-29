package co.unicauca.infra.listener;

import co.unicauca.entity.FormatoA;
import co.unicauca.infra.dto.FormatoACreado;
import co.unicauca.service.FormatoAService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListenerFormatoA {
    @Autowired
    private FormatoAService notificationService;

    /**
     * Escucha los mensajes cuando un FormatoA es evaluado.
     */
    @RabbitListener(queues = "formatoa.evaluado.notification.queue")
    public void recibirMensajeEvaluado(co.unicauca.entity.FormatoA formatoA) {
        notificationService.procesarNotificacionEvaluado(formatoA);
    }

    /**
     * Escucha los mensajes cuando un FormatoA es creado.
     */
    @RabbitListener(queues = "formatoa.notificaciones.queue")
    public void recibirMensajeCreado(FormatoACreado formatoCreado) {
        notificationService.procesarNotificacionCreado(formatoCreado);
    }
}
