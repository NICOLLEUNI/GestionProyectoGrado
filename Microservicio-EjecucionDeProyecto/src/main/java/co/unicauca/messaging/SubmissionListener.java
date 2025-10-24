package co.unicauca.messaging;

import co.unicauca.dto.SubmissionRequest;
import co.unicauca.entity.*;
import co.unicauca.mapper.*;
import co.unicauca.service.EjecucionProyectoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Listener encargado de recibir los eventos emitidos por Submission.
 * Este componente actúa como adaptador de entrada y delega la lógica al servicio EjecucionProyectoService.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SubmissionListener {

    @RabbitListener(queues = "submission.proyecto.creado")
    public void receiveSubmission(SubmissionRequest request) {
        log.info("🚀 Recibido Submission completo: {}", request);
    }
}
