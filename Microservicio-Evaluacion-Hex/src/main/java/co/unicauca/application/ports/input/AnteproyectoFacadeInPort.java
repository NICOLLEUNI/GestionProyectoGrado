package co.unicauca.application.ports.input;

import co.unicauca.domain.entities.Anteproyecto;
import co.unicauca.infrastructure.dto.request.*;
import co.unicauca.infrastructure.dto.response.*;
import co.unicauca.infrastructure.dto.notification.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface AnteproyectoFacadeInPort {
    Anteproyecto crearAnteproyecto(AnteproyectoRequest request);

    List<AnteproyectoResponse> listarAnteproyectos();

    AnteproyectoResponse buscarPorId(Long id);

    AnteproyectoResponseNotification asignarEvaluadores(Long id, String email1, String email2);
}
