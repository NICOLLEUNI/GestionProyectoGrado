package co.unicauca.application.services.facade;

import co.unicauca.application.ports.input.FormatoAFacadeInPort;
import co.unicauca.application.ports.output.FormatoARepoOutPort;
import co.unicauca.application.ports.output.MessagingPort;
import co.unicauca.application.services.FormatoAService;
import co.unicauca.domain.entities.*;
import co.unicauca.infrastructure.dto.request.FormatoARequest;
import co.unicauca.infrastructure.dto.response.FormatoAResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FormatoAFacade implements FormatoAFacadeInPort {

    private final FormatoAService formatoAService;
    private final MessagingPort publisher;


    public FormatoAFacade(FormatoAService formatoAService, MessagingPort messagingPort) {
        this.formatoAService = formatoAService;
        this.publisher = messagingPort;
    }

    /**
     * Guarda un nuevo FormatoA y publica un evento a RabbitMQ.
     */
    public FormatoA crearFormatoA(FormatoARequest request) {
        FormatoA formato = formatoAService.guardarFormatoA(request);

        FormatoAResponse response = new FormatoAResponse(
                formato.getId().intValue(),
                formato.getTitle(),
                formato.getState().toString(),
                formato.getObservations(),
                formato.getCounter()
        );

        publisher.publishFormatoA(response);

        return formato;
    }


    /**
     * Lista todos los FormatoA existentes.
     */
    public List<FormatoAResponse> listarFormatosA() {
        return formatoAService.listarTodos().stream()
                .map(f -> new FormatoAResponse(
                        f.getId().intValue(),
                        f.getTitle(),
                        f.getState().toString(),
                        f.getObservations(),
                        f.getCounter()
                ))
                .collect(Collectors.toList());
    }
    /**
     * Actualiza el estado de un FormatoA y notifica por RabbitMQ.
     */
    /**
     * Actualiza el estado de un FormatoA y notifica por RabbitMQ.
     */
    public FormatoAResponse actualizarEstado(Long id, EnumEstado nuevoEstado, String observaciones) {
        Optional<FormatoA> opt = formatoAService.actualizarEstado(id, nuevoEstado, observaciones);
        if (opt.isEmpty()) {
            throw new RuntimeException("No existe FormatoA con id: " + id);
        }

        FormatoA formato = opt.get();

        FormatoAResponse response = new FormatoAResponse(
                formato.getId().intValue(),
                formato.getTitle(),
                formato.getState().toString(),
                formato.getObservations(),
                formato.getCounter()
        );

        publisher.publishFormatoAEvaluado(response);

        return response;
    }

    /**
     * Buscar FormatoA por id.
     */
    public FormatoAResponse obtenerFormatoAPorId(Long id) {
        FormatoA formato = formatoAService.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe FormatoA con id: " + id));

        FormatoAResponse response = new FormatoAResponse(
                formato.getId().intValue(),
                formato.getTitle(),
                formato.getState().toString(),
                formato.getObservations(),
                formato.getCounter()
        );

        return response;
    }

    /**
     * Listar formatos por programa académico.
     */
    public List<FormatoAResponse> listarFormatosPorPrograma(String programa) {
        List<FormatoA> lista = formatoAService.listarPorPrograma(programa);

        if (lista.isEmpty()) {
            throw new RuntimeException("No hay formatos para el programa: " + programa);
        }

        return lista.stream()
                .map(f -> new FormatoAResponse(
                        f.getId().intValue(),
                        f.getTitle(),
                        f.getState().toString(),
                        f.getObservations(),
                        f.getCounter()
                ))
                .toList();
    }

}
