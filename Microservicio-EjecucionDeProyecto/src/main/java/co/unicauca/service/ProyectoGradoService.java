package co.unicauca.service;

import co.unicauca.entity.*;
import co.unicauca.infra.dto.*;
import co.unicauca.infra.messaging.RabbitMQPublisher;
import co.unicauca.repository.ProyectoGradoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProyectoGradoService {

    private final ProyectoGradoRepository proyectoRepository;
    private final RabbitMQPublisher rabbitMQPublisher;
    private static final Logger logger = LoggerFactory.getLogger(ProyectoGradoService.class);

    /**
     * ✅ CREAR PROYECTO DESDE API (PUBLICA EVENTO)
     */
    @Transactional
    public ProyectoGradoResponse crearProyecto(ProyectoGradoRequest request) {
        logger.info("🎓 Creando proyecto desde API: {}", request.nombre());

        ProyectoGrado proyecto = new ProyectoGrado();
        proyecto.setNombre(request.nombre());
        proyecto.setFechaCreacion(request.fecha().atStartOfDay());
        proyecto.setEstudiantesEmail(request.estudiantesEmail());
        proyecto.setEstado("ENTREGADO");
        proyecto.setIdFormatoA(request.idFormatoA());

        ProyectoGrado guardado = proyectoRepository.save(proyecto);
        ProyectoGradoResponse response = convertirAResponse(guardado);

        // ✅ PUBLICAR EVENTO a RabbitMQ (solo cuando se crea desde API)
        rabbitMQPublisher.publishProyectoGradoCreado(response);
        logger.info("✅ Proyecto creado y evento publicado: {}", response.id());

        return response;
    }

    /**
     * ✅ CREAR PROYECTO INTERNO (SIN PUBLICAR EVENTO - PARA LISTENER)
     */
    @Transactional
    public ProyectoGradoResponse crearProyectoInterno(ProyectoGradoRequest request) {
        logger.info("🔄 Creando proyecto interno (desde listener): {}", request.nombre());

        ProyectoGrado proyecto = new ProyectoGrado();
        proyecto.setNombre(request.nombre());
        proyecto.setFechaCreacion(request.fecha().atStartOfDay());
        proyecto.setEstudiantesEmail(request.estudiantesEmail());
        proyecto.setEstado("ENTREGADO");
        proyecto.setIdFormatoA(request.idFormatoA());

        ProyectoGrado guardado = proyectoRepository.save(proyecto);
        logger.info("✅ Proyecto interno creado (sin evento): {}", guardado.getId());

        return convertirAResponse(guardado);
    }

    /**
     * ✅ PROCESAR PROYECTO RECIBIDO DE RABBITMQ (LISTENER)
     */
    @Transactional
    public void procesarProyectoRecibido(ProyectoGradoResponse proyectoRecibido) {
        logger.info("📥 [LISTENER] Procesando proyecto recibido: {}", proyectoRecibido.nombre());

        try {
            // Convertir Response a Request
            ProyectoGradoRequest request = convertirResponseARequest(proyectoRecibido);

            // ✅ USAR MÉTODO INTERNO que NO publica evento
            crearProyectoInterno(request);

            logger.info("✅ [LISTENER] Proyecto procesado exitosamente (sin bucle): {}", proyectoRecibido.nombre());

        } catch (Exception e) {
            logger.error("❌ [LISTENER] Error procesando proyecto: {}", e.getMessage(), e);
            throw new RuntimeException("Error procesando proyecto recibido", e);
        }
    }

    @Transactional(readOnly = true)
    public ProyectoGradoResponse buscarPorId(Long id) {
        ProyectoGrado proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + id));
        return convertirAResponse(proyecto);
    }

    @Transactional(readOnly = true)
    public List<ProyectoGradoResponse> listarTodos() {
        return proyectoRepository.findAll().stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @Transactional
    public ProyectoGradoResponse actualizarProyecto(Long id, ProyectoGradoRequest request) {
        logger.info("✏️ Actualizando proyecto: {}", id);

        ProyectoGrado proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + id));

        proyecto.setNombre(request.nombre());
        proyecto.setEstudiantesEmail(request.estudiantesEmail());
        proyecto.setIdFormatoA(request.idFormatoA());

        ProyectoGrado actualizado = proyectoRepository.save(proyecto);
        ProyectoGradoResponse response = convertirAResponse(actualizado);

        // ✅ PUBLICAR EVENTO DE ACTUALIZACIÓN
        rabbitMQPublisher.publishProyectoGradoCreado(response);
        logger.info("✅ Proyecto actualizado y evento publicado: {}", id);

        return response;
    }

    @Transactional
    public void sincronizarFormatoA(Long proyectoId, Long idFormatoAExterno) {
        logger.info("🔄 Sincronizando FormatoA para proyecto: {}", proyectoId);

        ProyectoGrado proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + proyectoId));
        proyecto.setIdFormatoA(idFormatoAExterno);
        proyectoRepository.save(proyecto);

        // ✅ PUBLICAR EVENTO DE SINCRONIZACIÓN
        ProyectoGradoResponse response = convertirAResponse(proyecto);
        rabbitMQPublisher.publishProyectoGradoCreado(response);

        logger.info("✅ FormatoA sincronizado: Proyecto {} → FormatoA {}", proyectoId, idFormatoAExterno);
    }

    @Transactional(readOnly = true)
    public Optional<ProyectoGrado> buscarPorFormatoAExternoId(Long idFormatoA) {
        return proyectoRepository.findByIdFormatoA(idFormatoA);
    }

    @Transactional(readOnly = true)
    public List<FormatoAVersionResponse> buscarVersionesPorProyecto(Long proyectoId) {
        ProyectoGrado proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + proyectoId));

        if (proyecto.getIdFormatoA() == null) {
            throw new RuntimeException("El proyecto no tiene FormatoA asociado");
        }

        logger.info("🔍 Buscando versiones para proyecto {} via FormatoA: {}", proyectoId, proyecto.getIdFormatoA());
        return List.of(); // Implementar según tu lógica
    }

    public ProyectoGrado buscarEntidadPorId(Long id) {
        return proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + id));
    }

    public List<ProyectoGradoResponse> obtenerTodosConRelaciones() {
        List<ProyectoGrado> proyectos = proyectoRepository.findAllWithEstudiantes();
        return proyectos.stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    /**
     * ✅ CONVERTIR RESPONSE RECIBIDO A REQUEST INTERNO
     */
    private ProyectoGradoRequest convertirResponseARequest(ProyectoGradoResponse response) {
        return new ProyectoGradoRequest(
                response.id(),
                response.nombre(),
                response.fecha(),
                response.estudiantesEmail(),
                response.idFormatoA()
        );
    }

    private ProyectoGradoResponse convertirAResponse(ProyectoGrado proyecto) {
        return new ProyectoGradoResponse(
                proyecto.getId(),
                proyecto.getNombre(),
                proyecto.getFechaCreacion().toLocalDate(),
                proyecto.getEstudiantesEmail(),
                proyecto.getIdFormatoA()
        );
    }
}