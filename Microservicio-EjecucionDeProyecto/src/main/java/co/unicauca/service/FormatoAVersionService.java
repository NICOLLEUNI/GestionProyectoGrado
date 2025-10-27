package co.unicauca.service;

import co.unicauca.entity.FormatoAVersion;
import co.unicauca.entity.EnumEstado;
import co.unicauca.entity.EnumModalidad;
import co.unicauca.infra.dto.FormatoAVersionRequest;
import co.unicauca.infra.dto.FormatoAVersionResponse;
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
    private static final Logger logger = LoggerFactory.getLogger(FormatoAVersionService.class);

    /**
     * ✅ CREAR VERSIÓN DESDE API
     */
    @Transactional
    public FormatoAVersionResponse crearVersion(FormatoAVersionRequest request) {
        logger.info("📑 Creando versión desde API: {} - v{}", request.titulo(), request.numVersion());

        FormatoAVersion version = new FormatoAVersion();
        version.setNumeroVersion(request.numVersion());
        version.setFecha(request.fecha());
        version.setTitle(request.titulo());
        version.setMode(EnumModalidad.valueOf(request.modalidad()));
        version.setState(EnumEstado.valueOf(request.estado()));
        version.setObservations(request.observaciones());
        version.setCounter(request.counter());
        version.setIdFormatoA(request.idFormatoA());

        FormatoAVersion guardada = versionRepository.save(version);
        FormatoAVersionResponse response = convertirAResponse(guardada);

        logger.info("✅ Versión creada: {} - FormatoA: {}", response.id(), response.idFormatoA());

        return response;
    }

    /**
     * ✅ CREAR VERSIÓN INTERNA
     */
    @Transactional
    public FormatoAVersionResponse crearVersionInterna(FormatoAVersionRequest request) {
        logger.info("🔄 Creando versión interna: {} - v{}", request.titulo(), request.numVersion());

        FormatoAVersion version = new FormatoAVersion();
        version.setNumeroVersion(request.numVersion());
        version.setFecha(request.fecha());
        version.setTitle(request.titulo());
        version.setMode(EnumModalidad.valueOf(request.modalidad()));
        version.setState(EnumEstado.valueOf(request.estado()));
        version.setObservations(request.observaciones());
        version.setCounter(request.counter());
        version.setIdFormatoA(request.idFormatoA());

        FormatoAVersion guardada = versionRepository.save(version);
        logger.info("✅ Versión interna creada: {} - FormatoA: {}", guardada.getId(), guardada.getIdFormatoA());

        return convertirAResponse(guardada);
    }

    /**
     * ✅ PROCESAR VERSIÓN RECIBIDA
     * - Busca por idFormatoA y numVersion específicos
     * - Si existe: actualiza solo los campos del response
     * - Si no existe: crea nueva versión
     */
    @Transactional
    public void procesarVersionRecibida(FormatoAVersionResponse versionRecibida) {
        logger.info("📥 Procesando versión recibida: {} - v{} para FormatoA: {}",
                versionRecibida.titulo(), versionRecibida.numVersion(), versionRecibida.idFormatoA());

        try {
            // ✅ BUSCAR SI YA EXISTE ESTA VERSIÓN ESPECÍFICA
            Optional<FormatoAVersion> versionExistente = versionRepository
                    .findByIdFormatoAAndNumeroVersion(versionRecibida.idFormatoA(), versionRecibida.numVersion());

            if (versionExistente.isPresent()) {
                // ✅ ACTUALIZAR VERSIÓN EXISTENTE
                FormatoAVersion version = versionExistente.get();
                logger.info("🔄 Versión existente encontrada, actualizando: ID {}", version.getId());

                // 📝 ACTUALIZAR CAMPOS CORRECTAMENTE
                version.setState(EnumEstado.valueOf(versionRecibida.estado()));
                version.setObservations(versionRecibida.observaciones());

                versionRepository.save(version);
                logger.info("✅ Versión actualizada exitosamente: v{}", versionRecibida.numVersion());

            } else {
                // ✅ CREAR NUEVA VERSIÓN
                logger.info("🆕 Creando nueva versión: v{} para FormatoA: {}",
                        versionRecibida.numVersion(), versionRecibida.idFormatoA());

                FormatoAVersion nuevaVersion = new FormatoAVersion();
                nuevaVersion.setTitle(versionRecibida.titulo());  // ← CORREGIDO
                nuevaVersion.setNumeroVersion(versionRecibida.numVersion());  // ← CORREGIDO
                nuevaVersion.setIdFormatoA(versionRecibida.idFormatoA());
                nuevaVersion.setMode(EnumModalidad.valueOf(versionRecibida.modalidad()));  // ← CORREGIDO
                nuevaVersion.setState(EnumEstado.valueOf(versionRecibida.estado()));  // ← CORREGIDO
                nuevaVersion.setObservations(versionRecibida.observaciones());  // ← CORREGIDO
                nuevaVersion.setCounter(versionRecibida.counter());
                nuevaVersion.setFecha(versionRecibida.fecha());

                versionRepository.save(nuevaVersion);
                logger.info("✅ Nueva versión creada exitosamente: v{}", versionRecibida.numVersion());
            }

        } catch (Exception e) {
            logger.error("❌ Error procesando versión: {}", e.getMessage(), e);
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

        version.setTitle(request.titulo());
        version.setMode(EnumModalidad.valueOf(request.modalidad()));
        version.setState(EnumEstado.valueOf(request.estado()));
        version.setObservations(request.observaciones());
        version.setCounter(request.counter());

        if (request.idFormatoA() != null) {
            version.setIdFormatoA(request.idFormatoA());
        }

        FormatoAVersion actualizada = versionRepository.save(version);
        FormatoAVersionResponse response = convertirAResponse(actualizada);

        logger.info("✅ Versión actualizada: {}", id);

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
                response.titulo(),
                response.modalidad(),
                response.estado(),
                response.observaciones(),
                response.counter(),
                response.idFormatoA()
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