package co.unicauca.application.services.facade;

import co.unicauca.application.ports.input.AnteproyectoFacadeInPort;
import co.unicauca.application.ports.output.MessagingPort;
import co.unicauca.application.services.AnteproyectoService;
import co.unicauca.domain.entities.Anteproyecto;
import co.unicauca.infrastructure.dto.request.*;
import co.unicauca.infrastructure.dto.response.*;
import co.unicauca.infrastructure.dto.notification.*;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnteproyectoFacade  implements AnteproyectoFacadeInPort {

    private final AnteproyectoService anteproyectoService;
    private final MessagingPort publisher;


    public AnteproyectoFacade(AnteproyectoService anteproyectoService, MessagingPort publisher) {
        this.anteproyectoService = anteproyectoService;
        this.publisher = publisher;
    }

    /**
     * Guarda un nuevo anteproyecto .
     */
    public AnteproyectoResponse crearAnteproyecto(AnteproyectoRequest request) {
        Anteproyecto anteproyecto = anteproyectoService.guardarAnteproyecto(request);
        AnteproyectoResponse response = new AnteproyectoResponse(
                anteproyecto.getId(),
                anteproyecto.getTitulo(),
                anteproyecto.getFechaCreacion(),
                anteproyecto.getEstado(),
                anteproyecto.getIdProyectoGrado()
        );



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
                        a.getFechaCreacion(),
                        a.getEstado(),
                        a.getIdProyectoGrado()
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
                anteproyecto.getFechaCreacion(),
                anteproyecto.getEstado(),
                anteproyecto.getIdProyectoGrado()
        );

        return response;
    }
    public AnteproyectoResponseNotification asignarEvaluadores(Long idAnteproyecto, String email1, String email2) {
        Anteproyecto anteproyecto = anteproyectoService.asignarEvaluadores(idAnteproyecto, email1, email2);

        AnteproyectoResponseNotification response = new AnteproyectoResponseNotification(
                anteproyecto.getId(),
                anteproyecto.getTitulo(),
                anteproyecto.getEmailEvaluador1(),
                anteproyecto.getEmailEvaluador2()
        );
        publisher.publishAnteproyectoAsignacion(response);
        return response;
    }

}
