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
        logger.info("📥 ===========================================");
        logger.info("📥 PROCESANDO VERSIÓN RECIBIDA - INICIO");
        logger.info("📥 ===========================================");

        // 📋 MOSTRAR TODOS LOS CAMPOS RECIBIDOS
        logger.info("📋 DETALLE DE CAMPOS RECIBIDOS:");
        logger.info("   🆔 ID: {}", versionRecibida.id());
        logger.info("   🔢 Número Versión: {}", versionRecibida.numVersion());
        logger.info("   📅 Fecha: {}", versionRecibida.fecha());
        logger.info("   📝 Título: {}", versionRecibida.titulo());
        logger.info("   🎯 Modalidad: {}", versionRecibida.modalidad());
        logger.info("   📊 Estado: {}", versionRecibida.estado());
        logger.info("   💭 Observaciones: {}", versionRecibida.observaciones());
        logger.info("   🔢 Counter: {}", versionRecibida.counter());
        logger.info("   📄 ID Formato A: {}", versionRecibida.idFormatoA());

        logger.info("📥 ===========================================");

        try {
            // ✅ BUSCAR SI YA EXISTE ESTA VERSIÓN ESPECÍFICA
            logger.info("🔍 EJECUTANDO BÚSQUEDA EN BD:");
            logger.info("   Buscando: idFormatoA = {}, numeroVersion = {}",
                    versionRecibida.idFormatoA(), versionRecibida.numVersion());

            // Verificar tipos de datos
            logger.info("🔍 TIPOS DE DATOS - idFormatoA: {}, numeroVersion: {}",
                    versionRecibida.idFormatoA().getClass().getSimpleName(),
                    versionRecibida.numVersion());

            Optional<FormatoAVersion> versionExistente = versionRepository
                    .findByIdFormatoAAndNumeroVersion(versionRecibida.idFormatoA(), versionRecibida.numVersion());

            logger.info("🔍 RESULTADO BÚSQUEDA: {}", versionExistente.isPresent() ? "✅ ENCONTRADA" : "❌ NO ENCONTRADA");

            if (versionExistente.isPresent()) {
                // ✅ ACTUALIZAR VERSIÓN EXISTENTE
                FormatoAVersion version = versionExistente.get();
                logger.info("🔄 VERSIÓN EXISTENTE ENCONTRADA:");
                logger.info("   ID en BD: {}", version.getId());
                logger.info("   Número Versión en BD: {}", version.getNumeroVersion());
                logger.info("   ID FormatoA en BD: {}", version.getIdFormatoA());
                logger.info("   Estado actual en BD: {}", version.getState());
                logger.info("   Observaciones actuales en BD: {}", version.getObservations());

                logger.info("📝 ACTUALIZANDO CAMPOS:");
                logger.info("   Estado: {} → {}", version.getState(), versionRecibida.estado());
                logger.info("   Observaciones: {} → {}", version.getObservations(), versionRecibida.observaciones());

                // ACTUALIZAR CAMPOS
                version.setState(EnumEstado.valueOf(versionRecibida.estado()));
                version.setObservations(versionRecibida.observaciones());

                FormatoAVersion actualizada = versionRepository.save(version);
                logger.info("✅ VERSIÓN ACTUALIZADA EXITOSAMENTE:");
                logger.info("   ID: {}", actualizada.getId());
                logger.info("   Nuevo Estado: {}", actualizada.getState());
                logger.info("   Nuevas Observaciones: {}", actualizada.getObservations());

            } else {
                // ✅ CREAR NUEVA VERSIÓN
                logger.info("🆕 CREANDO NUEVA VERSIÓN (no se encontró existente)");

                // Verificar si hay otras versiones del mismo FormatoA
                List<FormatoAVersion> versionesExistentes = versionRepository.findByIdFormatoA(versionRecibida.idFormatoA());
                logger.info("🔍 VERSIONES EXISTENTES PARA FormatoA {}: {}",
                        versionRecibida.idFormatoA(), versionesExistentes.size());

                if (!versionesExistentes.isEmpty()) {
                    logger.info("🔍 DETALLE DE VERSIONES EXISTENTES:");
                    for (FormatoAVersion v : versionesExistentes) {
                        logger.info("   - ID: {}, Versión: {}, FormatoA: {}",
                                v.getId(), v.getNumeroVersion(), v.getIdFormatoA());
                    }
                }

                FormatoAVersion nuevaVersion = new FormatoAVersion();
                nuevaVersion.setTitle(versionRecibida.titulo());
                nuevaVersion.setNumeroVersion(versionRecibida.numVersion());
                nuevaVersion.setIdFormatoA(versionRecibida.idFormatoA());
                nuevaVersion.setMode(EnumModalidad.valueOf(versionRecibida.modalidad()));
                nuevaVersion.setState(EnumEstado.valueOf(versionRecibida.estado()));
                nuevaVersion.setObservations(versionRecibida.observaciones());
                nuevaVersion.setCounter(versionRecibida.counter());
                nuevaVersion.setFecha(versionRecibida.fecha());

                FormatoAVersion guardada = versionRepository.save(nuevaVersion);
                logger.info("✅ NUEVA VERSIÓN CREADA EXITOSAMENTE:");
                logger.info("   ID: {}", guardada.getId());
                logger.info("   Número Versión: {}", guardada.getNumeroVersion());
                logger.info("   ID FormatoA: {}", guardada.getIdFormatoA());
            }

            logger.info("📥 ===========================================");
            logger.info("📥 PROCESAMIENTO COMPLETADO EXITOSAMENTE");
            logger.info("📥 ===========================================");

        } catch (Exception e) {
            logger.error("❌ ===========================================");
            logger.error("❌ ERROR PROCESANDO VERSIÓN:");
            logger.error("❌ Mensaje: {}", e.getMessage());
            logger.error("❌ Causa: {}", e.getCause());
            logger.error("❌ StackTrace:", e);
            logger.error("❌ ===========================================");
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

    /**
     * 🔧 MÉTODO TEMPORAL PARA DIAGNÓSTICO
     */
    @Transactional(readOnly = true)
    public void diagnosticarVersiones(Long idFormatoA) {
        logger.info("🔧 DIAGNÓSTICO DE VERSIONES PARA FormatoA: {}", idFormatoA);

        List<FormatoAVersion> versiones = versionRepository.findByIdFormatoA(idFormatoA);
        logger.info("🔧 Total versiones encontradas: {}", versiones.size());

        for (FormatoAVersion version : versiones) {
            logger.info("🔧 Versión - ID: {}, NumVersion: {}, FormatoA: {}, Título: {}",
                    version.getId(), version.getNumeroVersion(), version.getIdFormatoA(), version.getTitle());

            // Probar búsqueda específica
            Optional<FormatoAVersion> busqueda = versionRepository
                    .findByIdFormatoAAndNumeroVersion(version.getIdFormatoA(), version.getNumeroVersion());
            logger.info("🔧 Búsqueda específica resultado: {}", busqueda.isPresent());
        }
    }
}