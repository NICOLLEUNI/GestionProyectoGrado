package co.unicauca.service.mapper;

import co.unicauca.entity.FormatoAVersionEntity;
import co.unicauca.entity.FormatoAEntity;
import co.unicauca.infra.dto.FormatoAVersionRequest;
import co.unicauca.infra.dto.FormatoAVersionResponse;
import org.springframework.stereotype.Service;

@Service
public class FormatoAVersionMapperService {

    /**
     * Convierte un FormatoAVersionRequest en FormatoAVersionEntity
     */
    public FormatoAVersionEntity mapFromRequest(FormatoAVersionRequest request) {
        FormatoAVersionEntity entity = new FormatoAVersionEntity();

        if (request.id() != null) {
            try {
                entity.setId(Long.valueOf(request.id()));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("ID de FormatoAVersionRequest no es un número válido");
            }
        }

        entity.setNumVersion(request.numVersion());
        entity.setFecha(request.fecha());
        entity.setTitulo(request.titulo());
        entity.setModalidad(request.modalidad());
        entity.setEstado(request.estado());
        entity.setObservaciones(request.observaciones());
        entity.setCounter(request.counter());

        // Relación con FormatoA solo asignando ID si existe
        if (request.id() != null) {
            FormatoAEntity formatoA = new FormatoAEntity();
            try {
                formatoA.setId(request.id());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("ID de FormatoA no es un número válido");
            }
            entity.setFormatoA(formatoA);
        }

        return entity;
    }

    /**
     * Convierte un FormatoAVersionEntity en FormatoAVersionResponse
     */
    public FormatoAVersionResponse mapToResponse(FormatoAVersionEntity entity) {
        return new FormatoAVersionResponse(
                entity.getId() != null ? String.valueOf(entity.getId()) : null,
                entity.getNumVersion(),
                entity.getFecha(),
                entity.getTitulo(),
                entity.getModalidad(),
                entity.getEstado(),
                entity.getObservaciones(),
                entity.getCounter(),
                entity.getFormatoA() != null ? String.valueOf(entity.getFormatoA().getId()) : null
        );
    }
}
