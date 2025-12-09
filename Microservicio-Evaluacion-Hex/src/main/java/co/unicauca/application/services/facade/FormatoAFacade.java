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

import java.text.Normalizer;
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
    public FormatoAResponse crearFormatoA(FormatoARequest request) {
        FormatoA formato = formatoAService.guardarFormatoA(request);

        if (formato.getId() == null) {
            throw new RuntimeException("Error: ID del FormatoA es nulo después de guardar");
        }
        FormatoAResponse response = new FormatoAResponse(
                formato.getId().intValue(),
                formato.getTitle(),
                formato.getState().name(),
                formato.getObservations(),
                formato.getCounter()
        );

        return response;
    }


    /**
     * Lista todos los FormatoA existentes.
     */
    public List<FormatoA>listarFormatosA(){return formatoAService.listarTodos();}
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
                formato.getState().name(),
                formato.getObservations(),
                formato.getCounter()
        );

        publisher.publishFormatoAEvaluado(response);

        publisher.publishFormatoAEvaluadoNotificacion(response);

        return response;
    }

    /**
     * Buscar FormatoA por id.
     */
    public FormatoA obtenerFormatoAPorId(Long id) {
        Optional<FormatoA> formatoAOpt = Optional.ofNullable(formatoAService.findById(id));

        if (formatoAOpt.isEmpty()) {
            throw new RuntimeException("❌ No se encontró el FormatoA con id: " + id);
        }

        return formatoAOpt.get();
    }

    /**
     * Listar formatos por programa académico.
     */
    public List<FormatoA> listarFormatosPorPrograma(String programa) {
        List<FormatoA> formatos = formatoAService.listarPorPrograma(programa);

        if (formatos.isEmpty()) {
            throw new RuntimeException("No hay formatos para el programa: " + programa);
        }

        return formatos;
    }

}
