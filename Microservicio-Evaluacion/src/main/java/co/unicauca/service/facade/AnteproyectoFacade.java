package co.unicauca.service.facade;

import co.unicauca.entity.Anteproyecto;
import co.unicauca.infra.dto.AnteproyectoRequest;
import co.unicauca.infra.dto.AnteproyectoResponse;
import co.unicauca.infra.messaging.RabbitMQPublisher;
import co.unicauca.service.AnteproyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnteproyectoFacade {

    private final AnteproyectoService anteproyectoService;
    private final RabbitMQPublisher publisher;

    @Autowired
    public AnteproyectoFacade(AnteproyectoService anteproyectoService, RabbitMQPublisher publisher) {
        this.anteproyectoService = anteproyectoService;
        this.publisher = publisher;
    }

    /**
     * Guarda un nuevo anteproyecto y publica el evento por RabbitMQ.
     */
    public AnteproyectoResponse crearAnteproyecto(AnteproyectoRequest request) {
        Anteproyecto anteproyecto = anteproyectoService.guardarAnteproyecto(request);

        AnteproyectoResponse response = new AnteproyectoResponse(
                anteproyecto.getId(),
                anteproyecto.getTitulo(),
                anteproyecto.getEstado(),
                anteproyecto.getObservaciones(),
                anteproyecto.getIdProyectoGrado(),
                anteproyecto.getFechaCreacion()
        );

        // ✅ Publicar evento a RabbitMQ
        publisher.publishAnteproyecto(response);

        return response;
    }

    /**
     * Lista todos los anteproyectos existentes.
     */
    public List<AnteproyectoResponse> listarAnteproyectos() {
        return anteproyectoService.listarAnteproyectos().stream()
                .map(a -> new AnteproyectoResponse(
                        a.getId(),
                        a.getTitulo(),
                        a.getEstado(),
                        a.getObservaciones(),
                        a.getIdProyectoGrado(),
                        a.getFechaCreacion()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Busca un anteproyecto por su ID.
     */
    public AnteproyectoResponse buscarPorId(Long id) {
        Anteproyecto anteproyecto = anteproyectoService.buscarPorId(id);

        AnteproyectoResponse response = new AnteproyectoResponse(
                anteproyecto.getId(),
                anteproyecto.getTitulo(),
                anteproyecto.getEstado(),
                anteproyecto.getObservaciones(),
                anteproyecto.getIdProyectoGrado(),
                anteproyecto.getFechaCreacion()
        );

        return response;
    }
}