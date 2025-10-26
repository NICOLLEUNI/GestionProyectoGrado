package co.unicauca.service.facade;

import co.unicauca.infra.dto.*;
import co.unicauca.infra.messaging.RabbitMQPublisher;
import co.unicauca.service.ProyectoGradoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProyectoGradoFacade {

    private final ProyectoGradoService proyectoGradoService;
    private final RabbitMQPublisher rabbitMQPublisher;

    @Autowired
    public ProyectoGradoFacade(ProyectoGradoService proyectoGradoService,
                               RabbitMQPublisher rabbitMQPublisher) {
        this.proyectoGradoService = proyectoGradoService;
        this.rabbitMQPublisher = rabbitMQPublisher;
    }

    /**
     * Crea un nuevo ProyectoGrado y publica evento.
     */
    @Transactional
    public ProyectoGradoResponse crearProyecto(ProyectoGradoRequest request) {
        ProyectoGradoResponse response = proyectoGradoService.crearProyecto(request);

        // Publicar evento
        rabbitMQPublisher.publishProyectoGradoCreado(response);

        return response;
    }

    /**
     * Listar todos los proyectos de grado.
     */
    public List<ProyectoGradoResponse> listarTodos() {
        return proyectoGradoService.listarTodos();
    }

    /**
     * Obtener proyecto por ID.
     */
    public ProyectoGradoResponse obtenerPorId(Long id) {
        return proyectoGradoService.buscarPorId(id);
    }

    /**
     * Actualizar proyecto existente.
     */
    @Transactional
    public ProyectoGradoResponse actualizarProyecto(Long id, ProyectoGradoRequest request) {
        ProyectoGradoResponse response = proyectoGradoService.actualizarProyecto(id, request);

        // Publicar evento de actualización
        rabbitMQPublisher.publishProyectoGradoCreado(response);

        return response;
    }

    /**
     * Agregar versión de formato a un proyecto.
     */
    @Transactional
    public ProyectoGradoResponse agregarVersionFormato(Long proyectoId, FormatoAVersionRequest request) {
        // ✅ CORREGIDO: Agregar el parámetro proyectoId que faltaba
        ProyectoGradoResponse response = proyectoGradoService.agregarVersionFormato(proyectoId, request);

        // Publicar evento de nueva versión
        rabbitMQPublisher.publishProyectoGradoCreado(response);

        return response;
    }

    /**
     * Sincronizar referencia externa de FormatoA.
     */
    @Transactional
    public void sincronizarFormatoA(Long proyectoId, Long idFormatoA) {
        proyectoGradoService.sincronizarFormatoA(proyectoId, idFormatoA);

        // Publicar evento de sincronización
        ProyectoGradoResponse proyecto = proyectoGradoService.buscarPorId(proyectoId);
        rabbitMQPublisher.publishProyectoGradoCreado(proyecto);
    }

    /**
     * Buscar versiones relacionadas a un proyecto (via formatoAExternoId)
     */
    public List<FormatoAVersionResponse> buscarVersionesPorProyecto(Long proyectoId) {
        return proyectoGradoService.buscarVersionesPorProyecto(proyectoId);
    }

    /**
     * LÓGICA DEL LISTENER - Procesar proyecto recibido del otro microservicio
     */
    @Transactional
    public void procesarProyectoRecibido(ProyectoGradoResponse proyectoRecibido) {
        System.out.println("📥 [FACADE] Procesando proyecto recibido: " + proyectoRecibido.nombre());

        try {
            // Convertir ProyectoGradoResponse a ProyectoGradoRequest
            ProyectoGradoRequest request = convertirResponseARequest(proyectoRecibido);

            // Crear el proyecto en nuestra base de datos
            proyectoGradoService.crearProyecto(request);

            System.out.println("✅ [FACADE] Proyecto " + proyectoRecibido.nombre() + " procesado exitosamente");

        } catch (Exception e) {
            System.err.println("❌ [FACADE] Error procesando proyecto: " + e.getMessage());
            throw new RuntimeException("Error procesando proyecto recibido", e);
        }
    }

    /**
     * Convertir ProyectoGradoResponse (recibido) a ProyectoGradoRequest (para nuestro servicio)
     */
    private ProyectoGradoRequest convertirResponseARequest(ProyectoGradoResponse response) {
        return new ProyectoGradoRequest(
                response.id(),
                response.nombre(),
                response.fecha(),
                response.estudiantesEmail(),
                response.idFormatoA(),
                response.idAnteproyecto()
        );
    }
}