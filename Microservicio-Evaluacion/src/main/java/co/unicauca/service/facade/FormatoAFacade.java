package co.unicauca.service.facade;

import co.unicauca.entity.EnumEstado;
import co.unicauca.entity.FormatoA;
import co.unicauca.infra.dto.FormatoARequest;
import co.unicauca.infra.dto.FormatoAResponse;
import co.unicauca.infra.messaging.RabbitMQPublisher;
import co.unicauca.service.FormatoAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FormatoAFacade {

    private final FormatoAService formatoAService;
    private final RabbitMQPublisher publisher;

    @Autowired
    public FormatoAFacade(FormatoAService formatoAService, RabbitMQPublisher publisher) {
        this.formatoAService = formatoAService;
        this.publisher = publisher;
    }

    /**
     * Guarda un nuevo FormatoA y publica un evento a RabbitMQ.
     */
    public FormatoAResponse crearFormatoA(FormatoARequest request) {
        FormatoA formato = formatoAService.guardarFormatoA(request);

        FormatoAResponse response = new FormatoAResponse(
                formato.getId().intValue(),
                formato.getTitle(),
                formato.getState().name(),
                formato.getObservations()
        );

        // ✅ Enviar el evento con el método ya existente del Publisher
        publisher.publishFormatoA(response);

        return response;
    }

    /**
     * Lista todos los FormatoA existentes.
     */
    public List<FormatoAResponse> listarFormatosA() {
        return formatoAService.findAll().stream()
                .map(f -> new FormatoAResponse(
                        f.id(),
                        f.title(),
                        f.state(),
                        f.observations()
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
        // 🔹 El service devuelve un Optional<FormatoAResponse>
        Optional<FormatoAResponse> responseOpt = formatoAService.actualizarEstado(id, nuevoEstado, observaciones);

        if (responseOpt.isEmpty()) {
            throw new RuntimeException("❌ No se encontró el FormatoA con id: " + id);
        }

        FormatoAResponse response = responseOpt.get();

        // ✅ Publicar el evento actualizado
        publisher.publishFormatoA(response);

        return response;
    }

}