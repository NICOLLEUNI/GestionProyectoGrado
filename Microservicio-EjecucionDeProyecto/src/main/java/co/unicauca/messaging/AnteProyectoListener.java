package co.unicauca.messaging;

import co.unicauca.dto.AnteproyectoRequest;
import co.unicauca.entity.AnteproyectoEntity;
import co.unicauca.entity.ProyectoGradoEntity;
import co.unicauca.mapper.AnteproyectoMapper;
import co.unicauca.repository.AnteproyectoRepository;
import co.unicauca.repository.ProyectoGradoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j

public class AnteProyectoListener {

    private final AnteproyectoRepository anteproyectoRepository;
    private final ProyectoGradoRepository proyectoGradoRepository;

    @RabbitListener(queues = "q.anteproyecto.created")
    @Transactional
    public void onAnteproyectoReceived(AnteproyectoRequest dto) {
        try {
            log.info("Recibido AnteproyectoRequest id={} proyectoId={}", dto.getId(), dto.getIdProyectoGrado());

            ProyectoGradoEntity proyecto = proyectoGradoRepository.findById(dto.getIdProyectoGrado())
                    .orElse(null);

            if (proyecto == null) {
                log.warn("ProyectoGrado id={} no existe en DB. Se procede a crear la entrada mínima o guardar sin asociación.", dto.getIdProyectoGrado());
                // Opcional: crear un registro placeholder o lanzar excepción según política.
            }

            AnteproyectoEntity entity = AnteproyectoMapper.fromRequest(dto, proyecto);
            anteproyectoRepository.save(entity);

            log.info("Anteproyecto guardado id={} para proyecto={}", entity.getId(), dto.getIdProyectoGrado());
        } catch (Exception ex) {
            log.error("Error guardando Anteproyecto id={} -> {}", dto != null ? dto.getId() : null, ex.getMessage(), ex);
            throw ex;
        }
    }
}
