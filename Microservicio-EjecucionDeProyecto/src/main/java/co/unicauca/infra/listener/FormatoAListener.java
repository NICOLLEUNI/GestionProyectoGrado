package co.unicauca.infra.listener;

import co.unicauca.entity.FormatoAEntity;
import co.unicauca.infra.dto.FormatoARequest;
import co.unicauca.service.FormatoAService;
import co.unicauca.service.mapper.FormatoAMapperService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FormatoAListener {

    private final FormatoAService formatoAService;
    private final FormatoAMapperService mapper;

    public FormatoAListener(FormatoAService formatoAService, FormatoAMapperService mapper) {
        this.formatoAService = formatoAService;
        this.mapper = mapper;
    }

    @RabbitListener(queues = "q.formatoa.created")
    public void recibirFormatoA(FormatoARequest request) {
        // 🔹 Mapear DTO a entidad
        FormatoAEntity entity = mapper.mapFromRequest(request);

        // 🔹 Guardar o actualizar en BD
        formatoAService.saveOrUpdate(entity);
    }
}
