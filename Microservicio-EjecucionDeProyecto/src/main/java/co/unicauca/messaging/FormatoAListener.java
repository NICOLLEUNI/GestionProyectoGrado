package co.unicauca.messaging;

import co.unicauca.dto.FormatoARequest;
import co.unicauca.entity.FormatoAEntity;
import co.unicauca.entity.ProyectoGradoEntity;
import co.unicauca.infra.config.RabbitMQConfig;
import co.unicauca.mapper.FormatoAMapper;
import co.unicauca.repository.FormatoARepository;
import co.unicauca.repository.ProyectoGradoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class FormatoAListener {

    private final FormatoARepository formatoARepository;
    private final ProyectoGradoRepository proyectoRepository;


    @RabbitListener(queues = "q.formatoa.created")
    @Transactional
    public void onFormatoAReceived(FormatoARequest dto) {
        try {
            log.info("📥 Recibido FormatoARequest id={} title={}", dto.getId(), dto.getTitle());

            // Obtener el proyecto asociado
            ProyectoGradoEntity proyecto = proyectoRepository.findById((long) dto.getId())
                    .orElseThrow(() -> new RuntimeException("Proyecto no encontrado id=" + dto.getId()));

            // Mapear DTO a entidad y asociar proyecto
            FormatoAEntity entity = FormatoAMapper.fromRequest(dto, proyecto);

            // Guardar en la base de datos
            formatoARepository.save(entity);

            log.info("✅ FormatoA persistido correctamente id={}", entity.getId());

        } catch (Exception ex) {
            log.error("❌ Error guardando FormatoA id={} -> {}", dto != null ? dto.getId() : null, ex.getMessage(), ex);
            // Si quieres que RabbitMQ reintente, relanza la excepción; si no, puedes manejarla aquí
            throw ex;
        }
    }
}
