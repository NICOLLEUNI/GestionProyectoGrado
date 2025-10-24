package co.unicauca.service.mapper;

import co.unicauca.entity.FormatoAEntity;
import co.unicauca.infra.dto.FormatoAUpdateRequest;
import co.unicauca.infra.dto.FormatoAUpdateResponse;
import org.springframework.stereotype.Service;

@Service
public class FormatoAUpdateMapperService {

    /**
     * Convierte un FormatoAUpdateRequest en FormatoAEntity
     */
    public FormatoAEntity mapFromRequest(FormatoAUpdateRequest request) {
        FormatoAEntity entity = new FormatoAEntity();

        if (request.id() != null) {
            entity.setId(request.id());
        }

        entity.setEstado(request.estado());
        entity.setCounter(request.counter());
        entity.setTitulo(null); // si no se actualiza el título
        entity.setModalidad(null); // si no se actualiza la modalidad
        entity.setVersiones(null); // se mantienen las versiones existentes
        return entity;
    }

    /**
     * Convierte un FormatoAEntity en FormatoAUpdateResponse
     */
    public FormatoAUpdateResponse mapToResponse(FormatoAEntity entity) {
        return new FormatoAUpdateResponse(
                entity.getId(),
                entity.getEstado(),
                entity.getObservaciones(),
                entity.getCounter()
        );
    }
}
