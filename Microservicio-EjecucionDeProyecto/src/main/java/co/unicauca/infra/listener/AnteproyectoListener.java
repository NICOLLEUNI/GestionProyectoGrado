package co.unicauca.infra.listener;

import co.unicauca.infra.config.RabbitMQConfig;
import co.unicauca.infra.dto.AnteproyectoRequest;
import co.unicauca.service.AnteproyectoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AnteproyectoListener {

    private final AnteproyectoService anteproyectoService;

    public AnteproyectoListener(AnteproyectoService anteproyectoService) {
        this.anteproyectoService = anteproyectoService;
    }

    /**
     * ✅ LISTENER SIMPLIFICADO - Solo recibe y delega al Service
     */
    @RabbitListener(queues = RabbitMQConfig.ANTEPROYECTO_EVALUACION_QUEUE)
    public void receiveAnteproyecto(AnteproyectoRequest anteproyectoRequest) {
        System.out.println("📥 [LISTENER] Anteproyecto Request recibido: " + anteproyectoRequest.titulo() +
                " | ID: " + anteproyectoRequest.id() +
                " | Proyecto: " + anteproyectoRequest.idProyectoGrado());

        try {
            // ✅ DELEGAR TODA LA LÓGICA AL SERVICE
            anteproyectoService.procesarAnteproyectoRequest(anteproyectoRequest);

            System.out.println("✅ [LISTENER] Anteproyecto Request delegado al Service: " + anteproyectoRequest.titulo());

        } catch (Exception e) {
            System.out.println("❌ [LISTENER] Error delegando Request al Service: " + e.getMessage());
            e.printStackTrace();
        }
    }
}