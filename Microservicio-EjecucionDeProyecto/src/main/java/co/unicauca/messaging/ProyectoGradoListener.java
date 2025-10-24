package co.unicauca.messaging;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import co.unicauca.dto.ProyectoGradoRequest;
import co.unicauca.entity.ProyectoGradoEntity;
import co.unicauca.mapper.ProyectoMapper;
import co.unicauca.repository.ProyectoGradoRepository;


@Component
@RequiredArgsConstructor
public class ProyectoGradoListener {

    private final ProyectoGradoRepository repository;

    @RabbitListener(queues = "q.proyecto.ejecucion")
    public void onProyectoReceived(ProyectoGradoRequest dto) {
        ProyectoGradoEntity entity = ProyectoMapper.fromRequest(dto);
        repository.save(entity);
    }
}

