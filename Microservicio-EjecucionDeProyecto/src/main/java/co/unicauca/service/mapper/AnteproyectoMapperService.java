package co.unicauca.service.mapper;

import co.unicauca.entity.AnteproyectoEntity;
import co.unicauca.infra.dto.AnteproyectoRequest;
import co.unicauca.infra.dto.AnteproyectoResponse;
import org.springframework.stereotype.Service;

@Service
public class AnteproyectoMapperService {

    /**
     * Convierte un AnteproyectoRequest en AnteproyectoEntity (para persistir en DB)
     */
    public AnteproyectoEntity mapFromRequest(AnteproyectoRequest request) {
        AnteproyectoEntity entity = new AnteproyectoEntity();

        if (request.id() != null) {
            entity.setId(request.id());
        }

        entity.setTitulo(request.titulo());
        entity.setEstado(request.estado() != null ? request.estado() : "PENDIENTE");
        entity.setFecha(request.fecha());
        entity.setObservaciones(request.observaciones());

        if (request.idProyectoGrado() != null) {
            // Si manejas relación, aquí solo asignamos el ID; la relación completa la puedes resolver después
            entity.getProyectoGrado().setId(request.idProyectoGrado());
        }

        return entity;
    }

    /**
     * Convierte un AnteproyectoEntity en AnteproyectoResponse (para exponer info)
     */
    public AnteproyectoResponse mapToResponse(AnteproyectoEntity entity) {
        return new AnteproyectoResponse(
                entity.getId(),
                entity.getTitulo(),
                entity.getFecha(),
                entity.getEstado(),
                entity.getObservaciones(),
                entity.getProyectoGrado() != null ? entity.getProyectoGrado().getId() : null
        );
    }
}
