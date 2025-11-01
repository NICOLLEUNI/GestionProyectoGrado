package co.unicauca.infra.listener;

import co.unicauca.infra.config.RabbitMQConfig;
import co.unicauca.infra.dto.ProyectoGradoRequest;
import co.unicauca.service.ProyectoGradoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ProyectoGradoListener {

    private final ProyectoGradoService proyectoGradoService;

    public ProyectoGradoListener(ProyectoGradoService proyectoGradoService) {
        this.proyectoGradoService = proyectoGradoService;
    }

    /**
     * ✅ LISTENER SIMPLIFICADO - Solo recibe y delega al Service
     */
    @RabbitListener(queues = RabbitMQConfig.PROYECTO_GRADO_CREADO_QUEUE)
    public void receiveProyectoGrado(ProyectoGradoRequest proyectoRequest) {
        System.out.println("📥 [LISTENER] Proyecto Request recibido: " + proyectoRequest.nombre() +
                " | ID: " + proyectoRequest.id());

        try {
            // ✅ DELEGAR TODA LA LÓGICA AL SERVICE
            proyectoGradoService.procesarProyectoRequest(proyectoRequest);

            System.out.println("✅ [LISTENER] Proyecto Request delegado al Service: " + proyectoRequest.nombre());

        } catch (Exception e) {
            System.out.println("❌ [LISTENER] Error delegando Request al Service: " + e.getMessage());
            e.printStackTrace();
        }
    }
}