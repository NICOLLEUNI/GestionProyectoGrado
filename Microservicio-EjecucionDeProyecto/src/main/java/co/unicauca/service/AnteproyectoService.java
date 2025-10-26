package co.unicauca.service;

import co.unicauca.entity.Anteproyecto;
import co.unicauca.entity.EnumEstadoAnteproyecto;
import co.unicauca.entity.ProyectoGrado;
import co.unicauca.infra.dto.AnteproyectoRequest;
import co.unicauca.infra.dto.AnteproyectoResponse;
import co.unicauca.infra.messaging.RabbitMQPublisher;
import co.unicauca.repository.AnteproyectoRepository;
import co.unicauca.repository.ProyectoGradoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnteproyectoService {

    private final AnteproyectoRepository anteproyectoRepository;
    private final ProyectoGradoRepository proyectoGradoRepository;
    private final RabbitMQPublisher rabbitMQPublisher;
    private static final Logger logger = LoggerFactory.getLogger(AnteproyectoService.class);

    /**
     * ✅ CREAR ANTEPROYECTO DESDE API (PUBLICA EVENTO)
     */
    @Transactional
    public AnteproyectoResponse crearAnteproyecto(AnteproyectoRequest request) {
        logger.info("📄 Creando anteproyecto desde API: {}", request.titulo());

        // Validar que el proyecto exista
        ProyectoGrado proyecto = proyectoGradoRepository.findById(request.idProyectoGrado())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + request.idProyectoGrado()));

        Anteproyecto anteproyecto = new Anteproyecto();
        anteproyecto.setTitulo(request.titulo());
        anteproyecto.setFechaCreacion(request.fecha());
        anteproyecto.setEstado(EnumEstadoAnteproyecto.valueOf(request.estado()));
        anteproyecto.setObservaciones(request.observaciones());
        anteproyecto.setProyectoGrado(proyecto);

        Anteproyecto guardado = anteproyectoRepository.save(anteproyecto);
        AnteproyectoResponse response = convertirAResponse(guardado);

        // ✅ PUBLICAR EVENTO a RabbitMQ
        rabbitMQPublisher.publishAnteproyectoCreado(response);
        logger.info("✅ Anteproyecto creado y evento publicado: {}", response.id());

        return response;
    }

    /**
     * ✅ CREAR ANTEPROYECTO INTERNO (SIN PUBLICAR EVENTO - PARA LISTENER)
     */
    @Transactional
    public AnteproyectoResponse crearAnteproyectoInterno(AnteproyectoRequest request) {
        logger.info("🔄 Creando anteproyecto interno (desde listener): {}", request.titulo());

        ProyectoGrado proyecto = proyectoGradoRepository.findById(request.idProyectoGrado())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + request.idProyectoGrado()));

        Anteproyecto anteproyecto = new Anteproyecto();
        anteproyecto.setTitulo(request.titulo());
        anteproyecto.setFechaCreacion(request.fecha());
        anteproyecto.setEstado(EnumEstadoAnteproyecto.valueOf(request.estado()));
        anteproyecto.setObservaciones(request.observaciones());
        anteproyecto.setProyectoGrado(proyecto);

        Anteproyecto guardado = anteproyectoRepository.save(anteproyecto);
        logger.info("✅ Anteproyecto interno creado (sin evento): {}", guardado.getId());

        return convertirAResponse(guardado);
    }

    /**
     * ✅ CREAR O ACTUALIZAR ANTEPROYECTO (PARA RABBITMQ - SIN BUCLE)
     */
    @Transactional
    public AnteproyectoResponse crearOActualizarAnteproyecto(AnteproyectoRequest request) {
        logger.info("🔍 Buscando anteproyecto existente para proyecto: {}", request.idProyectoGrado());

        // Buscar si ya existe un anteproyecto para este proyecto
        List<Anteproyecto> existentes = anteproyectoRepository.findAllByProyectoGradoId(request.idProyectoGrado());

        if (!existentes.isEmpty()) {
            // ✅ ACTUALIZAR el existente
            Anteproyecto anteproyecto = existentes.get(0);
            logger.info("🔄 Anteproyecto existente encontrado, actualizando: {}", anteproyecto.getId());

            anteproyecto.setTitulo(request.titulo());
            anteproyecto.setFechaCreacion(request.fecha());
            anteproyecto.setEstado(EnumEstadoAnteproyecto.valueOf(request.estado()));
            anteproyecto.setObservaciones(request.observaciones());

            Anteproyecto actualizado = anteproyectoRepository.save(anteproyecto);
            logger.info("✅ Anteproyecto actualizado: {}", actualizado.getId());
            return convertirAResponse(actualizado);
        } else {
            // ✅ CREAR nuevo usando método INTERNO (sin publicar evento)
            logger.info("🆕 No existe anteproyecto, creando nuevo");
            return crearAnteproyectoInterno(request);
        }
    }

    /**
     * ✅ PROCESAR ANTEPROYECTO RECIBIDO DE RABBITMQ (LISTENER)
     */
    @Transactional
    public void procesarAnteproyectoRecibido(AnteproyectoResponse anteproyectoRecibido) {
        logger.info("📥 [LISTENER] Procesando anteproyecto recibido: {}", anteproyectoRecibido.titulo());

        try {
            // Convertir Response a Request
            AnteproyectoRequest request = convertirResponseARequest(anteproyectoRecibido);

            // ✅ USAR CREAR O ACTUALIZAR (que usa método interno)
            crearOActualizarAnteproyecto(request);

            logger.info("✅ [LISTENER] Anteproyecto procesado exitosamente (sin bucle): {}", anteproyectoRecibido.titulo());

        } catch (Exception e) {
            logger.error("❌ [LISTENER] Error procesando anteproyecto: {}", e.getMessage(), e);
            throw new RuntimeException("Error procesando anteproyecto recibido", e);
        }
    }

    @Transactional(readOnly = true)
    public AnteproyectoResponse buscarPorId(Long id) {
        Anteproyecto anteproyecto = anteproyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Anteproyecto no encontrado"));
        return convertirAResponse(anteproyecto);
    }

    @Transactional
    public AnteproyectoResponse actualizarAnteproyecto(Long id, AnteproyectoRequest request) {
        logger.info("✏️ Actualizando anteproyecto: {}", id);

        Anteproyecto anteproyecto = anteproyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Anteproyecto no encontrado"));

        anteproyecto.setTitulo(request.titulo());
        anteproyecto.setFechaCreacion(request.fecha());
        anteproyecto.setEstado(EnumEstadoAnteproyecto.valueOf(request.estado()));
        anteproyecto.setObservaciones(request.observaciones());

        Anteproyecto actualizado = anteproyectoRepository.save(anteproyecto);
        AnteproyectoResponse response = convertirAResponse(actualizado);

        // ✅ PUBLICAR EVENTO DE ACTUALIZACIÓN
        rabbitMQPublisher.publishAnteproyectoCreado(response);
        logger.info("✅ Anteproyecto actualizado y evento publicado: {}", id);

        return response;
    }

    /**
     * ✅ CONVERTIR REQUEST A ENTITY
     */
    private Anteproyecto convertirAEntity(AnteproyectoRequest request) {
        ProyectoGrado proyecto = proyectoGradoRepository.findById(request.idProyectoGrado())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + request.idProyectoGrado()));

        Anteproyecto entity = new Anteproyecto();
        entity.setTitulo(request.titulo());
        entity.setFechaCreacion(request.fecha());
        entity.setEstado(EnumEstadoAnteproyecto.valueOf(request.estado()));
        entity.setObservaciones(request.observaciones());
        entity.setProyectoGrado(proyecto);

        return entity;
    }

    /**
     * ✅ CONVERTIR RESPONSE RECIBIDO A REQUEST INTERNO
     */
    private AnteproyectoRequest convertirResponseARequest(AnteproyectoResponse response) {
        return new AnteproyectoRequest(
                response.id(),
                response.titulo(),
                response.fecha(),
                response.estado(),
                response.observaciones(),
                response.idProyectoGrado()
        );
    }

    private AnteproyectoResponse convertirAResponse(Anteproyecto anteproyecto) {
        return new AnteproyectoResponse(
                anteproyecto.getId(),
                anteproyecto.getTitulo(),
                anteproyecto.getFechaCreacion(),
                anteproyecto.getEstado().name(),
                anteproyecto.getObservaciones(),
                anteproyecto.getProyectoGrado() != null ? anteproyecto.getProyectoGrado().getId() : null
        );
    }

    public List<AnteproyectoResponse> obtenerTodos() {
        List<Anteproyecto> anteproyectos = anteproyectoRepository.findAll();
        return anteproyectos.stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    public List<AnteproyectoResponse> buscarPorProyecto(Long proyectoId) {
        List<Anteproyecto> anteproyectos = anteproyectoRepository.findAllByProyectoGradoId(proyectoId);
        return anteproyectos.stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }
}