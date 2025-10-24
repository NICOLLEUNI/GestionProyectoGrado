package co.unicauca.infra.listener;

import co.unicauca.entity.AnteproyectoEntity;
import co.unicauca.infra.dto.AnteproyectoRequest;
import co.unicauca.service.AnteproyectoService;
import co.unicauca.service.mapper.AnteproyectoMapperService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AnteproyectoListener {

    private final AnteproyectoService anteproyectoService;
    private final AnteproyectoMapperService mapper;

    public AnteproyectoListener(AnteproyectoService anteproyectoService,
                                AnteproyectoMapperService mapper) {
        this.anteproyectoService = anteproyectoService;
        this.mapper = mapper;
    }

    /**
     * Listener para la cola "q.anteproyecto.created".
     * Recibe un AnteproyectoRequest, lo mapea a entidad y persiste.
     */
    @RabbitListener(queues = "q.anteproyecto.created")
    public void recibirAnteproyecto(AnteproyectoRequest request) {

        // 🔹 Mapear DTO a entidad
        AnteproyectoEntity entity = mapper.mapFromRequest(request);

        // 🔹 Persistir: guarda nuevo o actualiza existente
        anteproyectoService.saveOrUpdate(entity);

        // 🔹 Log opcional
        System.out.println("Anteproyecto procesado y persistido: " + entity.getTitulo());
    }
}
