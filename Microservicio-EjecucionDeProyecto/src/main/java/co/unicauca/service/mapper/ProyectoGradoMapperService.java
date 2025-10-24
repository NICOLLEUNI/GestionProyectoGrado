package co.unicauca.service.mapper;

import co.unicauca.entity.ProyectoGradoEntity;
import co.unicauca.infra.dto.ProyectoGradoRequest;
import co.unicauca.infra.dto.ProyectoGradoResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProyectoGradoMapperService {

    /**
     * Convierte un ProyectoGradoRequest en ProyectoGradoEntity
     */
    public ProyectoGradoEntity mapFromRequest(ProyectoGradoRequest request) {
        ProyectoGradoEntity entity = new ProyectoGradoEntity();

        if (request.id() != null) {
            entity.setId(request.id());
        }

        entity.setNombre(request.nombre());
        entity.setFecha(request.fecha());

        // Se pueden asignar estudiantes, aquí solo emails si no quieres relacionar las entidades completas
        if (request.estudiantesEmail() != null) {
            entity.setEstudiantesEmails(new ArrayList<>(request.estudiantesEmail()));
        }

        // Relación con FormatoA y Anteproyecto, solo asignando ID
        if (request.idFormatoA() != null) {
            entity.getFormatoA().setId(request.idFormatoA());
        }
        if (request.idAnteproyecto() != null) {
            entity.getAnteproyecto().setId(request.idAnteproyecto());
        }

        return entity;
    }

    /**
     * Convierte un ProyectoGradoEntity en ProyectoGradoResponse
     */
    public ProyectoGradoResponse mapToResponse(ProyectoGradoEntity entity) {
        List<String> estudiantesEmail = entity.getEstudiantesEmails() != null
                ? new ArrayList<>(entity.getEstudiantesEmails())
                : new ArrayList<>();

        return new ProyectoGradoResponse(
                entity.getId(),
                entity.getNombre(),
                entity.getFecha(),
                estudiantesEmail,
                entity.getFormatoA() != null ? entity.getFormatoA().getId() : null,
                entity.getAnteproyecto() != null ? entity.getAnteproyecto().getId() : null
        );
    }
}
