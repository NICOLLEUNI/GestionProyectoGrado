package co.unicauca.infra.listener;

import co.unicauca.infra.config.RabbitMQConfig;
import co.unicauca.infra.dto.FormatoAVersionRequest;
import co.unicauca.service.FormatoAVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FormatoAVersionListener {

    private final FormatoAVersionService versionService;

    /**
     * ✅ LISTENER CORREGIDO - AHORA RECIBE FormatoAVersionRequest
     */
    @RabbitListener(queues = RabbitMQConfig.FORMATOAVERSION_HISTORICO_QUEUE)
    public void recibirVersionCreada(FormatoAVersionRequest versionRequest) {
        System.out.println("📥 [RABBITMQ] Versión Request recibida: " + versionRequest.titulo() +
                " - v" + versionRequest.numVersion() +
                " para FormatoA: " + versionRequest.idFormatoA());

        try {
            versionService.procesarVersionRecibida(versionRequest);
            System.out.println("✅ [RABBITMQ] Versión Request procesada exitosamente: v" + versionRequest.numVersion());
        } catch (Exception e) {
            System.out.println("❌ [RABBITMQ] Error procesando versión Request: " + e.getMessage());
            e.printStackTrace();
            // Puedes implementar dead letter queue aquí si es necesario
        }
    }


    /**
     * ✅ NUEVO LISTENER PARA ELIMINACIÓN DE FORMATOA
     */
    @RabbitListener(queues = RabbitMQConfig.FORMATOA_ELIMINADO_QUEUE)
    public void recibirFormatoAEliminado(Map<String, Object> mensaje) {
        Long formatoAId = Long.valueOf(mensaje.get("formatoAId").toString());
        String razon = (String) mensaje.get("razon");
        String origen = (String) mensaje.get("origen");

        System.out.println("🗑️ [RABBITMQ] Evento de eliminación recibido - " +
                "FormatoA ID: " + formatoAId + ", Razón: " + razon + ", Origen: " + origen);

        try {
            versionService.eliminarVersionesPorFormatoA(formatoAId);
            System.out.println("✅ [RABBITMQ] Versiones eliminadas para FormatoA ID: " + formatoAId);

        } catch (Exception e) {
            System.err.println("❌ [RABBITMQ] Error eliminando versiones para FormatoA ID: " + formatoAId);
            e.printStackTrace();
            throw new RuntimeException("Error procesando eliminación", e);
        }
    }
}

