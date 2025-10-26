package co.unicauca.service;

import co.unicauca.entity.FormatoAVersion;
import co.unicauca.entity.EnumEstado;
import co.unicauca.entity.EnumModalidad;
import co.unicauca.infra.dto.FormatoAVersionRequest;
import co.unicauca.infra.dto.FormatoAVersionResponse;
import co.unicauca.infra.messaging.RabbitMQPublisher;
import co.unicauca.repository.FormatoAVersionRepository;
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
public class FormatoAVersionService {

    private final FormatoAVersionRepository versionRepository;
    private final RabbitMQPublisher rabbitMQPublisher;
    private static final Logger logger = LoggerFactory.getLogger(FormatoAVersionService.class);

    /**
     * ✅ CREAR VERSIÓN DESDE API (PUBLICA EVENTO)
     */
    @Transactional
    public FormatoAVersionResponse crearVersion(FormatoAVersionRequest request) {
        logger.info("📑 Creando versión desde API: {} - v{}", request.title(), request.numVersion());

        FormatoAVersion version = new FormatoAVersion();
        version.setNumeroVersion(request.numVersion());
        version.setFecha(request.fecha());
        version.setTitle(request.title());
        version.setMode(EnumModalidad.valueOf(request.mode()));
        version.setState(EnumEstado.valueOf(request.state()));
        version.setObservations(request.observations());
        version.setCounter(request.counter());
        version.setIdFormatoA(request.IdFormatoA());

        FormatoAVersion guardada = versionRepository.save(version);
        FormatoAVersionResponse response = convertirAResponse(guardada);

        // ✅ PUBLICAR EVENTO a RabbitMQ
        rabbitMQPublisher.publishFormatoACreado(response);
        logger.info("✅ Versión creada y evento publicado: {} - FormatoA: {}", response.id(), response.IdFormatoA());

        return response;
    }

    /**
     * ✅ CREAR VERSIÓN INTERNA (SIN PUBLICAR EVENTO - PARA LISTENER)
     */
    @Transactional
    public FormatoAVersionResponse crearVersionInterna(FormatoAVersionRequest request) {
        logger.info("🔄 Creando versión interna (desde listener): {} - v{}", request.title(), request.numVersion());

        FormatoAVersion version = new FormatoAVersion();
        version.setNumeroVersion(request.numVersion());
        version.setFecha(request.fecha());
        version.setTitle(request.title());
        version.setMode(EnumModalidad.valueOf(request.mode()));
        version.setState(EnumEstado.valueOf(request.state()));
        version.setObservations(request.observations());
        version.setCounter(request.counter());
        version.setIdFormatoA(request.IdFormatoA());

        FormatoAVersion guardada = versionRepository.save(version);
        logger.info("✅ Versión interna creada (sin evento): {} - FormatoA: {}", guardada.getId(), guardada.getIdFormatoA());

        return convertirAResponse(guardada);
    }

    /**
     * ✅ PROCESAR VERSIÓN RECIBIDA DE RABBITMQ (LISTENER)
     * - Busca por idFormatoA y numVersion específicos
     * - Si existe: actualiza solo los campos del response
     * - Si no existe: crea nueva versión
     */
    @Transactional
    public void procesarVersionRecibida(FormatoAVersionResponse versionRecibida) {
        logger.info("📥 [LISTENER] Procesando versión recibida: {} - v{} para FormatoA: {}",
                versionRecibida.title(), versionRecibida.numVersion(), versionRecibida.IdFormatoA());

        try {
            // ✅ BUSCAR SI YA EXISTE ESTA VERSIÓN ESPECÍFICA
            Optional<FormatoAVersion> versionExistente = versionRepository
                    .findByIdFormatoAAndNumeroVersion(versionRecibida.IdFormatoA(), versionRecibida.numVersion());

            if (versionExistente.isPresent()) {
                // ✅ ACTUALIZAR VERSIÓN EXISTENTE
                FormatoAVersion version = versionExistente.get();
                logger.info("🔄 Versión existente encontrada, actualizando: ID {}", version.getId());

                // 📝 CAMPOS QUE SE ACTUALIZAN (vienen en FormatoAResponse):
                logger.info("   📤 Estado anterior: {} → Nuevo: {}", version.getState(), versionRecibida.state());
                logger.info("   📤 Observaciones anteriores: {} → Nuevas: {}",
                        version.getObservations(), versionRecibida.observations());

                version.setState(EnumEstado.valueOf(versionRecibida.state()));
                version.setObservations(versionRecibida.observations());

                // 🔒 CAMPOS QUE SE MANTIENEN (NO se actualizan):
                logger.info("   🔒 Título se mantiene: {}", version.getTitle());
                logger.info("   🔒 Modalidad se mantiene: {}", version.getMode());
                logger.info("   🔒 Fecha se mantiene: {}", version.getFecha());
                logger.info("   🔒 Counter se mantiene: {}", version.getCounter());
                logger.info("   🔒 idFormatoA se mantiene: {}", version.getIdFormatoA());
                logger.info("   🔒 numVersion se mantiene: {}", version.getNumeroVersion());

                // Los campos que NO se tocan (preservan valores originales):
                // - version.setTitle() → NO SE ACTUALIZA
                // - version.setMode() → NO SE ACTUALIZA
                // - version.setFecha() → NO SE ACTUALIZA
                // - version.setCounter() → NO SE ACTUALIZA
                // - version.setIdFormatoA() → NO SE ACTUALIZA
                // - version.setNumeroVersion() → NO SE ACTUALIZA

                versionRepository.save(version);
                logger.info("✅ Versión actualizada exitosamente: v{}", versionRecibida.numVersion());

            } else {
                // ✅ CREAR NUEVA VERSIÓN
                logger.info("🆕 Creando nueva versión: v{} para FormatoA: {}",
                        versionRecibida.numVersion(), versionRecibida.IdFormatoA());

                FormatoAVersionRequest request = convertirResponseARequest(versionRecibida);
                crearVersionInterna(request);

                logger.info("✅ Nueva versión creada exitosamente: v{}", versionRecibida.numVersion());
            }

        } catch (Exception e) {
            logger.error("❌ [LISTENER] Error procesando versión: {}", e.getMessage(), e);
            throw new RuntimeException("Error procesando versión recibida", e);
        }
    }

    @Transactional(readOnly = true)
    public FormatoAVersionResponse buscarPorId(Long id) {
        FormatoAVersion version = versionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Versión no encontrada"));
        return convertirAResponse(version);
    }

    @Transactional(readOnly = true)
    public List<FormatoAVersionResponse> listarTodas() {
        return versionRepository.findAll().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FormatoAVersionResponse> buscarPorFormatoA(Long formatoAId) {
        logger.info("🔍 Buscando versiones por FormatoA: {}", formatoAId);
        return versionRepository.findByIdFormatoA(formatoAId).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public FormatoAVersionResponse actualizarVersion(Long id, FormatoAVersionRequest request) {
        logger.info("✏️ Actualizando versión: {}", id);

        FormatoAVersion version = versionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Versión no encontrada"));

        version.setTitle(request.title());
        version.setMode(EnumModalidad.valueOf(request.mode()));
        version.setState(EnumEstado.valueOf(request.state()));
        version.setObservations(request.observations());
        version.setCounter(request.counter());

        if (request.IdFormatoA() != null) {
            version.setIdFormatoA(request.IdFormatoA());
        }

        FormatoAVersion actualizada = versionRepository.save(version);
        FormatoAVersionResponse response = convertirAResponse(actualizada);

        // ✅ PUBLICAR EVENTO DE ACTUALIZACIÓN
        rabbitMQPublisher.publishFormatoACreado(response);
        logger.info("✅ Versión actualizada y evento publicado: {}", id);

        return response;
    }

    /**
     * ✅ CONVERTIR RESPONSE RECIBIDO A REQUEST INTERNO
     */
    private FormatoAVersionRequest convertirResponseARequest(FormatoAVersionResponse response) {
        return new FormatoAVersionRequest(
                response.id(),
                response.numVersion(),
                response.fecha(),
                response.title(),
                response.mode(),
                response.state(),
                response.observations(),
                response.counter(),
                response.IdFormatoA()
        );
    }

    private FormatoAVersionResponse convertirAResponse(FormatoAVersion version) {
        return new FormatoAVersionResponse(
                version.getId(),
                version.getNumeroVersion(),
                version.getFecha(),
                version.getTitle(),
                version.getMode().name(),
                version.getState().name(),
                version.getObservations(),
                version.getCounter(),
                version.getIdFormatoA()
        );
    }
}