package co.unicauca.service.facade;

import co.unicauca.entity.*;
import co.unicauca.infra.dto.*;
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
     * Crea un nuevo FormatoA, lo guarda en la base de datos y publica el evento en RabbitMQ.
     */
    public FormatoAResponse crearFormatoA(FormatoARequest request) {
        // Crear el FormatoA utilizando el servicio
        FormatoAEntity formatoAEntity = formatoAService.saveFormatoA(request);

        // Convertir el FormatoAEntity a FormatoAResponse
        FormatoAResponse response = new FormatoAResponse(
                formatoAEntity.getId(),
                formatoAEntity.getTitle(),
                formatoAEntity.getMode(),
                formatoAEntity.getProjectManager().getEmail(),
                formatoAEntity.getProjectCoManager().getEmail(),
                formatoAEntity.getGeneralObjetive(),
                formatoAEntity.getSpecificObjetives(),
                formatoAEntity.getArchivoPDF(),
                formatoAEntity.getCartaLaboral(),
                formatoAEntity.getEstudianteEmails(),
                formatoAEntity.getCounter(),
                formatoAEntity.getState().name()

        );

        // Publicar el evento en RabbitMQ
        publisher.publishFormatoA(response);

        return response;
    }


    /**
     * Listar todos los FormatosA existentes.
     */
    public List<FormatoAResponse> listarFormatosA() {
        // Obtener todos los FormatosA desde el servicio
        List<FormatoAEntity> formatosA = formatoAService.findAll();

        // Convertir la lista de FormatoAEntity a FormatoAResponse
        return formatosA.stream()
                .map(formatoAEntity -> new FormatoAResponse(
                        formatoAEntity.getId(),
                        formatoAEntity.getTitle(),
                        formatoAEntity.getMode(),
                        formatoAEntity.getProjectManager().getEmail(),
                        formatoAEntity.getProjectCoManager().getEmail(),
                        formatoAEntity.getGeneralObjetive(),
                        formatoAEntity.getSpecificObjetives(),
                        formatoAEntity.getArchivoPDF(),
                        formatoAEntity.getCartaLaboral(),
                        formatoAEntity.getEstudianteEmails(),
                        formatoAEntity.getCounter(),
                        formatoAEntity.getState().name()
                ))
                .collect(Collectors.toList());
    }
}
