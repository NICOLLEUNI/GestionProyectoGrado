package co.unicauca.infra.listener;

import co.unicauca.entity.FormatoAVersionEntity;
import co.unicauca.infra.dto.FormatoAVersionRequest;
import co.unicauca.service.FormatoAVersionService;
import co.unicauca.service.mapper.FormatoAVersionMapperService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FormatoAVersionListener {

    private final FormatoAVersionService service;
    private final FormatoAVersionMapperService mapper;

    public FormatoAVersionListener(FormatoAVersionService service, FormatoAVersionMapperService mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @RabbitListener(queues = "q.formatoaversion.created")
    public void recibirVersion(FormatoAVersionRequest request) {

        // 🔹 Mapear DTO a entidad
        FormatoAVersionEntity entity = mapper.mapFromRequest(request);

        // 🔹 Persistir (nuevo o actualización)
        service.saveOrUpdate(entity);

        System.out.println("FormatoAVersion procesada y persistida: " + entity.getTitulo());
    }
}
