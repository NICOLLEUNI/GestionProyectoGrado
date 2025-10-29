package co.unicauca.infra.listener;

import co.unicauca.entity.FormatoA;
import co.unicauca.service.FormatoAService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ListenerFormatoA {
    @Autowired
    private FormatoAService notificationService;

    @RabbitListener(queues = "formatoa.evaluado.notification.queue")
    public void recibirMensaje(FormatoA formatoA) {
        notificationService.procesarNotificacionEvaluado(formatoA);
    }
}
