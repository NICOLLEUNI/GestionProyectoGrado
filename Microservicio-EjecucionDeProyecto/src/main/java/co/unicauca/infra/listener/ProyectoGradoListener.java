package co.unicauca.infra.listener;

import co.unicauca.infra.config.RabbitMQConfig;
import co.unicauca.infra.dto.ProyectoGradoRequest;
import co.unicauca.service.ProyectoGradoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

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

    @RabbitListener(queues = RabbitMQConfig.PROYECTO_ELIMINADO_QUEUE)
    public void recibirProyectoEliminado(Map<String, Object> mensaje) {
        Long formatoAId = Long.valueOf(mensaje.get("formatoAId").toString());
        String razon = (String) mensaje.get("razon");

        System.out.println("️ [EJECUCION] Eliminando PROYECTO - FormatoA ID: " + formatoAId + ", Razón: " + razon);

        try {
            proyectoGradoService.eliminarProyectoPorFormatoA(formatoAId);
            System.out.println("✅ [EJECUCION] Proyecto eliminado exitosamente");
        } catch (Exception e) {
            System.err.println("❌ [EJECUCION] Error eliminando proyecto: " + e.getMessage());
            e.printStackTrace();
        }
    }
}