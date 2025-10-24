package co.unicauca.service.mapper;

import co.unicauca.entity.FormatoAEntity;
import co.unicauca.entity.FormatoAVersionEntity;
import co.unicauca.infra.dto.FormatoARequest;
import co.unicauca.infra.dto.FormatoAResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FormatoAMapperService {

    /**
     * Convierte un FormatoARequest en FormatoAEntity
     */
    public FormatoAEntity mapFromRequest(FormatoARequest request) {
        FormatoAEntity entity = new FormatoAEntity();

        if (request.id() != null) {
            entity.setId(request.id());
        }

        entity.setTitulo(request.title());
        entity.setModalidad(request.mode());
        entity.setEstado(request.estado() != null ? request.estado() : "PENDIENTE");
        entity.setCounter(request.counter());

        // Si manejas proyecto, solo asignar ID
        if (request.projectManagerEmail() != null) {
            // Aquí podrías enlazar la entidad ProyectoGradoEntity si quieres
            // entity.setProyectoGrado(...);
        }

        return entity;
    }

    /**
     * Convierte un FormatoAEntity en FormatoAResponse
     */
    public FormatoAResponse mapToResponse(FormatoAEntity entity) {
        List<Long> versionesIds = entity.getVersiones()
                .stream()
                .map(FormatoAVersionEntity::getId)
                .collect(Collectors.toList());

        return new FormatoAResponse(
                entity.getId(),
                entity.getTitulo(),
                entity.getModalidad(),
                entity.getEstado(),
                null, // observaciones, si lo agregas en la entidad lo pones aquí
                entity.getCounter(),
                entity.getProyectoGrado() != null ? entity.getProyectoGrado().getId() : null,
                versionesIds
        );
    }
}
