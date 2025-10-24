package co.unicauca.messaging;

import co.unicauca.dto.FormatoARequest;
import co.unicauca.entity.FormatoAEntity;
import co.unicauca.mapper.FormatoAMapper;
import co.unicauca.repository.FormatoARepository;
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

    // Ajusta el nombre de la cola según tu configuración
    @RabbitListener(queues = "q.formatoa.created")
    @Transactional
    public void onFormatoAReceived(FormatoARequest dto) {
        try {
            log.info("Recibido FormatoARequest id={} title={}", dto.getId(), dto.getTitle());
            FormatoAEntity entity = FormatoAMapper.fromRequest(dto);
            formatoARepository.save(entity);
            log.info("FormatoA persistido id={}", entity.getId());
        } catch (Exception ex) {
            log.error("Error guardando FormatoA id={} -> {}", dto != null ? dto.getId() : null, ex.getMessage(), ex);
            // Política: rethrow si quieres que el broker reintente, o manejar aquí según conveniencia
            throw ex;
        }
    }
}
