package co.unicauca.application.ports.input;

import co.unicauca.domain.entities.EnumEstado;
import co.unicauca.domain.entities.FormatoA;
import co.unicauca.infrastructure.dto.request.FormatoARequest;
import co.unicauca.infrastructure.dto.response.FormatoAResponse;

import java.util.List;

public interface FormatoAFacadeInPort {

    FormatoAResponse crearFormatoA(FormatoARequest request);

    List<FormatoA> listarFormatosA();

    FormatoAResponse actualizarEstado(Long id, EnumEstado nuevoEstado, String observaciones);

    FormatoA obtenerFormatoAPorId(Long id);

    List<FormatoA> listarFormatosPorPrograma(String programa);
}
