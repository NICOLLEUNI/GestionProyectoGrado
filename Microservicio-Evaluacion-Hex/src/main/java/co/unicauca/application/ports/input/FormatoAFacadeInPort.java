package co.unicauca.application.ports.input;

import co.unicauca.domain.entities.EnumEstado;
import co.unicauca.domain.entities.FormatoA;
import co.unicauca.infrastructure.dto.request.FormatoARequest;
import co.unicauca.infrastructure.dto.response.FormatoAResponse;

import java.util.List;

public interface FormatoAFacadeInPort {

    FormatoA crearFormatoA(FormatoARequest request);

    List<FormatoAResponse> listarFormatosA();

    FormatoAResponse actualizarEstado(Long id, EnumEstado nuevoEstado, String observaciones);

    FormatoAResponse obtenerFormatoAPorId(Long id);

    List<FormatoAResponse> listarFormatosPorPrograma(String programa);
}
