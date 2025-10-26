package co.unicauca.service.facade;

import co.unicauca.infra.dto.FormatoAVersionRequest;
import co.unicauca.infra.dto.FormatoAVersionResponse;
import co.unicauca.infra.messaging.RabbitMQPublisher;
import co.unicauca.service.FormatoAVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FormatoAVersionFacade {

    private final FormatoAVersionService formatoAVersionService;
    private final RabbitMQPublisher rabbitMQPublisher;

    @Autowired
    public FormatoAVersionFacade(FormatoAVersionService formatoAVersionService,
                                 RabbitMQPublisher rabbitMQPublisher) {
        this.formatoAVersionService = formatoAVersionService;
        this.rabbitMQPublisher = rabbitMQPublisher;
    }

    /**
     * LÓGICA DEL LISTENER - Recibe FormatoAVersionResponse del otro microservicio
     * y lo guarda en nuestra base de datos
     */
    @Transactional
    public void procesarVersionRecibida(FormatoAVersionResponse versionRecibida) {
        System.out.println("📥 [FACADE] Procesando versión recibida: " + versionRecibida);

        try {
            // Convertir FormatoAVersionResponse a FormatoAVersionRequest
            FormatoAVersionRequest request = convertirResponseARequest(versionRecibida);

            // Guardar la versión en nuestra base de datos
            formatoAVersionService.crearVersion(request);

            System.out.println("✅ [FACADE] Versión " + versionRecibida.numVersion() + " procesada exitosamente");

        } catch (Exception e) {
            System.err.println("❌ [FACADE] Error procesando versión: " + e.getMessage());
            throw new RuntimeException("Error procesando versión recibida", e);
        }
    }

    /**
     * Crear nueva versión DESDE NUESTRO microservicio y publicar evento
     */
    @Transactional
    public FormatoAVersionResponse crearVersion(FormatoAVersionRequest request) {
        FormatoAVersionResponse response = formatoAVersionService.crearVersion(request);

        // Publicar evento
        rabbitMQPublisher.publishFormatoACreado(response);

        return response;
    }

    /**
     * Listar todas las versiones de formato.
     */
    public List<FormatoAVersionResponse> listarTodas() {
        return formatoAVersionService.listarTodas();
    }

    /**
     * Obtener versión por ID.
     */
    public FormatoAVersionResponse obtenerPorId(Long id) {
        return formatoAVersionService.buscarPorId(id);
    }



    /**
     * Actualizar versión existente.
     */
    @Transactional
    public FormatoAVersionResponse actualizarVersion(Long id, FormatoAVersionRequest request) {
        FormatoAVersionResponse response = formatoAVersionService.actualizarVersion(id, request);

        // Publicar evento de actualización
        rabbitMQPublisher.publishFormatoACreado(response);

        return response;
    }

    /**
     * Convertir FormatoAVersionResponse (recibido) a FormatoAVersionRequest (para nuestro servicio)
     */
    private FormatoAVersionRequest convertirResponseARequest(FormatoAVersionResponse response) {
        return new FormatoAVersionRequest(
                response.id(),
                response.numVersion(),
                response.fecha(),
                response.title(),       // Usamos los nombres de NUESTRO DTO
                response.mode(),        // "mode" en nuestro Request
                response.state(),       // "state" en nuestro Request
                response.observations(), // "observations" en nuestro Request
                response.counter(),
                response.IdFormatoA()  // "idFormatoA" en nuestro Request
        );
    }
}