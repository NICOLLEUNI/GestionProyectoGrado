package co.unicauca.service.facade;

import co.unicauca.entity.*;
import co.unicauca.infra.dto.*;
import co.unicauca.infra.dto.notificacion.FormatoAReponseNotificacion;
import co.unicauca.infra.messaging.RabbitMQPublisher;
import co.unicauca.service.FormatoAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
                formato.getObservations(),
                formato.getCounter()
        );

        // ✅ Enviar el evento con el método ya existente del Publisher
        publisher.publishFormatoA(response);

        return response;
    }

    /**
     * Lista todos los FormatoA existentes.
     */
    public List<FormatoA> listarFormatosA() {
        return formatoAService.findAll();
    }
    /**
     * Actualiza el estado de un FormatoA y notifica por RabbitMQ.
     */
    /**
     * Actualiza el estado de un FormatoA y notifica por RabbitMQ.
     */
    public FormatoAResponse actualizarEstado(Long id, EnumEstado nuevoEstado, String observaciones) {
        Optional<FormatoA> formatoAOpt = formatoAService.actualizarEstado(id, nuevoEstado, observaciones);

        if (formatoAOpt.isEmpty()) {
            throw new RuntimeException("❌ No se encontró el FormatoA con id: " + id);
        }

        FormatoA formatoA = formatoAOpt.get();

        // 🔹 Mapear a los dos DTOs
        FormatoAResponse formatoAResponse = formatoAService.mapToFormatoAResponse(formatoA);
        FormatoAReponseNotificacion formatoAResponseNotificacion = formatoAService.mapToFormatoAResponseNotificacion(formatoA);

        // 📤 Publicar a Submission
        publisher.publishFormatoAEvaluado(formatoAResponse);

        // 📤 Publicar a Notificación
        publisher.publishFormatoAEvaluadoNotificacion(formatoAResponseNotificacion);

        // ✅ Devolver respuesta al cliente
        return formatoAResponse;
    }

    public FormatoA obtenerFormatoAPorId(Long id) {
        Optional<FormatoA> formatoAOpt = Optional.ofNullable(formatoAService.findById(id));

        if (formatoAOpt.isEmpty()) {
                    throw new RuntimeException("❌ No se encontró el FormatoA con id: " + id);
        }

        return formatoAOpt.get();
    }

    public List<FormatoA> listarFormatosPorPrograma(String programa) {
        List<FormatoA> formatos = formatoAService.listarFormatosPorPrograma(programa);

        if (formatos.isEmpty()) {
            throw new RuntimeException("⚠️ No se encontraron formatos para el programa: " + programa);
        }

        return formatos;
    }

}