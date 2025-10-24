package co.unicauca.infra.listener;

import co.unicauca.infra.dto.ProyectoGradoRequest;
import co.unicauca.service.PersonaService;
import co.unicauca.service.ProyectoGradoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ProyectoGradoListener {

    private final ProyectoGradoService proyectoGradoService;
    private final PersonaService personaService;

    public ProyectoGradoListener(ProyectoGradoService proyectoGradoService,
                                 PersonaService personaService) {
        this.proyectoGradoService = proyectoGradoService;
        this.personaService = personaService;
    }

    @RabbitListener(queues = "q.proyecto.ejecucion")
    @Transactional
    public void recibirProyecto(ProyectoGradoRequest request) {
        // Convertir IDs de String a Long
        Long formatoAId = Long.valueOf(request.IdFormatoA());
        Long anteproyectoId = Long.valueOf(request.IdAnteproyecto());

        // Mapear emails de estudiantes a IDs de PersonaEntity
        List<Long> personasIds = personaService.buscarIdsPorEmails(request.estudiantesEmail());

        // Reconstruir y persistir el proyecto
        proyectoGradoService.reconstruirProyecto(
                formatoAId,
                null, // versiones aún no se usan
                anteproyectoId,
                personasIds
        );

        System.out.println("Proyecto de grado procesado correctamente: " + request.nombre());
    }
}
