package co.unicauca.service.facade;

import co.unicauca.infra.dto.AnteproyectoRequest;
import co.unicauca.infra.dto.AnteproyectoResponse;
import co.unicauca.infra.messaging.RabbitMQPublisher;
import co.unicauca.service.AnteproyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnteproyectoFacade {

    private final AnteproyectoService anteproyectoService;
    private final RabbitMQPublisher rabbitMQPublisher;

    @Autowired
    public AnteproyectoFacade(AnteproyectoService anteproyectoService,
                              RabbitMQPublisher rabbitMQPublisher) {
        this.anteproyectoService = anteproyectoService;
        this.rabbitMQPublisher = rabbitMQPublisher;
    }

    /**
     * Crea un nuevo Anteproyecto y lo guarda en la base de datos.
     * Luego publica un evento a RabbitMQ.
     */
    @Transactional
    public AnteproyectoResponse crearAnteproyecto(AnteproyectoRequest request) {
        // Usar el servicio para crear el anteproyecto
        AnteproyectoResponse response = anteproyectoService.crearAnteproyecto(request);

        // Publicar evento
        rabbitMQPublisher.publishAnteproyectoCreado(response);

        return response;
    }


    /**
     * Obtener un anteproyecto por su ID.
     */
    public AnteproyectoResponse obtenerPorId(Long id) {
        return anteproyectoService.buscarPorId(id);
    }

    /**
     * Actualizar un anteproyecto existente.
     */
    @Transactional
    public AnteproyectoResponse actualizarAnteproyecto(Long id, AnteproyectoRequest request) {
        AnteproyectoResponse response = anteproyectoService.actualizarAnteproyecto(id, request);

        // Publicar evento de actualización si es necesario
        rabbitMQPublisher.publishAnteproyectoCreado(response); // O crear método específico para actualización

        return response;
    }

}