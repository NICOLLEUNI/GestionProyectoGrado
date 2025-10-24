package co.unicauca.messaging;

import co.unicauca.dto.FormatoAVersionRequest;
import co.unicauca.entity.FormatoAEntity;
import co.unicauca.entity.FormatoAVersionEntity;
import co.unicauca.mapper.FormatoAVersionMapper;
import co.unicauca.repository.FormatoARepository;
import co.unicauca.repository.FormatoAVersionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class FormatoAVersionListener {
    private final FormatoARepository formatoARepository;
    private final FormatoAVersionRepository formatoAVersionRepository;

    @RabbitListener(queues = "q.formatoa.version.created")
    @Transactional
    public void onFormatoAVersionReceived(FormatoAVersionRequest dto) {
        try {
            log.info("Recibida FormatoAVersionRequest id={} numVersion={}", dto.getId(), dto.getNumVersion());

            // Atención: DTO trae formatoAId como int en tu ejemplo. FormatoAEntity usa id String.
            // Intentamos buscar por String(valor) y si no existe, por el entero convertido.
            String formatoAKey = String.valueOf(dto.getFormatoAId());
            FormatoAEntity formatoA = formatoARepository.findById(formatoAKey)
                    .orElse(null);

            if (formatoA == null) {
                // Si tu FormatoAEntity tuviera id numérico, adapta esto:
                log.warn("FormatoA con id {} no encontrado. La versión se guardará sin relación por ahora.", formatoAKey);
            }

            FormatoAVersionEntity version = FormatoAVersionMapper.fromRequest(dto, formatoA);
            formatoAVersionRepository.save(version);
            log.info("FormatoA Version guardada idVersion={} (formatoAId={})", version.getId(), formatoAKey);
        } catch (Exception ex) {
            log.error("Error guardando FormatoA Version id={} -> {}", dto != null ? dto.getId() : null, ex.getMessage(), ex);
            throw ex;
        }
    }
}
