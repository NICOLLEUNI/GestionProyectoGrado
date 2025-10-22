package co.unicauca.infra.messaging;

import co.unicauca.entity.SeguimientoProyecto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publicarSeguimientoCreado(SeguimientoProyecto seguimiento) {
        rabbitTemplate.convertAndSend(
                "ejecucion.events",
                "ejecucion.seguimiento.creado",
                seguimiento
        );
    }

    public void publicarEstadoActualizado(SeguimientoProyecto seguimiento) {
        rabbitTemplate.convertAndSend(
                "ejecucion.events",
                "ejecucion.proyecto.estadoActualizado",
                seguimiento
        );
    }

}


