package co.unicauca.infra.messaging;

import co.unicauca.dto.ProyectoGradoDTO;
import co.unicauca.entity.EstadoProyecto;
import co.unicauca.entity.SeguimientoProyecto;
import co.unicauca.repository.SeguimientoProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class EjecucionProyectoListener {


    private final SeguimientoProyectoRepository repository;
    private final EventPublisher eventPublisher;

    @RabbitListener(queues = "submission.proyecto.creado")
    public void onProyectoCreado(ProyectoGradoDTO proyecto) {

        System.out.println("=== MENSAJE RECIBIDO ===");
        System.out.println("ID: " + proyecto.getId());
        System.out.println("Título: " + proyecto.getTitulo());


        System.out.println("Mensaje recibido en onProyectoCreado: " + proyecto);
        SeguimientoProyecto seguimiento = SeguimientoProyecto.builder()
                .idProyecto(proyecto.getId())
                .titulo(proyecto.getTitulo())
                .modalidad(proyecto.getModalidad())
                .estado(EstadoProyecto.PENDIENTE)
                .idEstudiante(proyecto.getIdEstudiante())
                .idDirector(proyecto.getIdDirector())
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        repository.save(seguimiento);
        eventPublisher.publicarSeguimientoCreado(seguimiento);
    }

    @RabbitListener(queues = "evaluacion.formatoa.aprobado")
    public void onFormatoAprobado(Long idProyecto) {

        System.out.println("Mensaje recibido en onFormatoAprobado: " + idProyecto);
        repository.findByIdProyecto(idProyecto).ifPresent(seguimiento -> {
            seguimiento.setEstado(EstadoProyecto.EN_EJECUCION);
            seguimiento.setFechaActualizacion(LocalDateTime.now());
            repository.save(seguimiento);
            eventPublisher.publicarEstadoActualizado(seguimiento);
        });
    }

    @RabbitListener(queues = "evaluacion.formatoa.rechazado")
    public void onFormatoRechazado(Long idProyecto) {

        System.out.println("Mensaje recibido en onFormatoAprobado: " + idProyecto);
        repository.findByIdProyecto(idProyecto).ifPresent(seguimiento -> {
            seguimiento.setEstado(EstadoProyecto.RECHAZADO);
            seguimiento.setFechaActualizacion(LocalDateTime.now());
            repository.save(seguimiento);
            eventPublisher.publicarEstadoActualizado(seguimiento);
        });
    }
}
