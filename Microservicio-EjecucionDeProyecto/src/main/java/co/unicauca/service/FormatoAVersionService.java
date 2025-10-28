package co.unicauca.service;

import co.unicauca.entity.FormatoAVersion;
import co.unicauca.entity.EnumEstado;
import co.unicauca.entity.EnumModalidad;
import co.unicauca.infra.dto.FormatoAVersionRequest;
import co.unicauca.infra.dto.FormatoAVersionResponse;
import co.unicauca.infra.memento.RequestHistoryManager;
import co.unicauca.infra.memento.RequestMemento;
import co.unicauca.repository.FormatoAVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FormatoAVersionService {

    private final FormatoAVersionRepository versionRepository;
    private final RequestHistoryManager historyManager;

    /**
     * ✅ CREAR VERSIÓN DESDE REQUEST CON MEMENTO
     */
    @Transactional
    public FormatoAVersion crearVersion(FormatoAVersionRequest request) {
        System.out.println("📑 RECIBIENDO VERSIÓN desde Request: " + request.titulo() + " - v" + request.numVersion() + "Counter "+ request.counter());

        FormatoAVersion version = convertirRequestAEntity(request);
        System.out.println("🔍 DEBUG COMPLETO DEL REQUEST:");
        System.out.println("   - ID: " + version.getId());
        System.out.println("   - numVersion: " + version.getNumeroVersion());
        System.out.println("   - counter: " + version.getCounter());
        System.out.println("   - estado: " + version.getState());
        System.out.println("   - fecha: " + version.getFecha());
        System.out.println("   - titulo: " + version.getTitle());
        System.out.println("   - modalidad: " + version.getMode());
        System.out.println("   - idFormatoA: " + version.getIdFormatoA());

        FormatoAVersion guardada = versionRepository.save(version);

        // ✅ GUARDAR REQUEST ORIGINAL EN MEMENTO
        Map<String, Object> requestData = convertirRequestAMap(request);
        RequestMemento memento = historyManager.saveRequestState("FORMATO_A", guardada.getId(), request.estado(), requestData);

        System.out.println("✅ VERSIÓN CREADA con Memento - ID: " + guardada.getId() +
                " | Versión Memento: " + memento.getVersion());

        return guardada;
    }

    /**
     * ✅ PROCESAR VERSIÓN RECIBIDA (REQUEST) CON MEMENTO
     */
    @Transactional
    public void procesarVersionRecibida(FormatoAVersionRequest request) {
        try {
            FormatoAVersion versionAActualizar = null;
            String estrategia = "";

            // 🔍 ESTRATEGIA 2: Buscar por FormatoA si no se encontró por ID
            if (versionAActualizar == null && request.idFormatoA() != null) {
                List<FormatoAVersion> versiones = versionRepository.findByIdFormatoA(request.idFormatoA());
                if (!versiones.isEmpty()) {
                    versionAActualizar = versiones.get(0);
                    estrategia = "FormatoA";
                    System.out.println("🔍 Encontrada por FormatoA ID: " + request.idFormatoA() +
                            " - Counter actual: " + versionAActualizar.getCounter());
                }
            }

            if (versionAActualizar != null) {
                System.out.println("🔄 ACTUALIZAR versión existente - ID: " + versionAActualizar.getId() +
                        " | Counter antes: " + versionAActualizar.getCounter());

                // ✅ GUARDAR ESTADO ACTUAL ANTES DE ACTUALIZAR
                Map<String, Object> estadoAnterior = crearSnapshotEntity(versionAActualizar);
                RequestMemento mementoAnterior = historyManager.saveRequestState("FORMATO_A",
                        versionAActualizar.getId(), versionAActualizar.getState().name(), estadoAnterior);

                System.out.println("💾 Estado anterior guardado - Versión: " + mementoAnterior.getVersion() +
                        " | Counter: " + versionAActualizar.getCounter());

                // ACTUALIZAR ENTIDAD DESDE REQUEST
                actualizarEntityDesdeRequest(versionAActualizar, request);
                FormatoAVersion actualizada = versionRepository.save(versionAActualizar);

                // ✅ GUARDAR NUEVO REQUEST EN MEMENTO
                Map<String, Object> requestData = convertirRequestAMap(request);
                RequestMemento mementoNuevo = historyManager.saveRequestState("FORMATO_A",
                        actualizada.getId(), request.estado(), requestData);

                System.out.println("✅ VERSIÓN ACTUALIZADA - ID: " + actualizada.getId() +
                        " | Versión Memento: " + mementoNuevo.getVersion() +
                        " | Counter después: " + actualizada.getCounter());

            } else {
                System.out.println("🆕 NUEVA VERSIÓN - FormatoA: " + request.idFormatoA() +
                        " | Counter inicial: " + request.counter());

                // ✅ CREAR NUEVA VERSIÓN DESDE REQUEST
                FormatoAVersion nuevaVersion = convertirRequestAEntity(request);
                FormatoAVersion guardada = versionRepository.save(nuevaVersion);

                // ✅ GUARDAR REQUEST EN MEMENTO
                Map<String, Object> requestData = convertirRequestAMap(request);
                RequestMemento memento = historyManager.saveRequestState("FORMATO_A",
                        guardada.getId(), request.estado(), requestData);

                System.out.println("✅ VERSIÓN CREADA - ID: " + guardada.getId() +
                        " | Versión Memento: " + memento.getVersion() +
                        " | Counter final: " + guardada.getCounter());
            }

        } catch (Exception e) {
            System.out.println("❌ ERROR procesando versión: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error procesando versión recibida", e);
        }
    }

    /**
     * ✅ BUSCAR VERSIÓN POR ID
     */
    @Transactional(readOnly = true)
    public FormatoAVersionResponse buscarPorId(Long id) {
        System.out.println("🔍 BUSCANDO versión por ID: " + id);

        FormatoAVersion version = versionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Versión no encontrada con ID: " + id));

        System.out.println("✅ VERSIÓN ENCONTRADA: " + version.getId() + " - v" + version.getNumeroVersion());
        return convertirAResponse(version);
    }

    /**
     * ✅ ACTUALIZAR VERSIÓN ESPECÍFICA
     */
    @Transactional
    public FormatoAVersionResponse actualizarVersion(Long id, FormatoAVersionRequest request) {
        System.out.println("✏️ ACTUALIZANDO VERSIÓN específica: " + id);

        FormatoAVersion version = versionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Versión no encontrada con ID: " + id));

        // 💾 GUARDAR ESTADO ACTUAL EN MEMENTO
        Map<String, Object> estadoAnterior = crearSnapshotEntity(version);
        historyManager.saveRequestState("FORMATO_A", version.getId(),
                version.getState().name(), estadoAnterior);

        // ✏️ ACTUALIZAR CAMPOS
        version.setTitle(request.titulo());
        version.setNumeroVersion(request.numVersion());
        version.setFecha(request.fecha());
        version.setMode(EnumModalidad.valueOf(request.modalidad()));
        version.setState(EnumEstado.valueOf(request.estado()));
        version.setObservations(request.observaciones());
        version.setCounter(request.counter());
        version.setIdFormatoA(request.idFormatoA());

        FormatoAVersion actualizada = versionRepository.save(version);

        // 💾 GUARDAR NUEVO ESTADO EN MEMENTO
        Map<String, Object> requestData = convertirRequestAMap(request);
        RequestMemento memento = historyManager.saveRequestState("FORMATO_A",
                actualizada.getId(), actualizada.getState().name(), requestData);

        FormatoAVersionResponse response = convertirAResponse(actualizada);

        System.out.println("✅ VERSIÓN ACTUALIZADA - ID: " + id +
                " | Versión Memento: " + memento.getVersion());

        return response;
    }


    // ========== MÉTODOS DE CONSULTA HISTORIAL MEMENTO ==========

    public List<RequestMemento> obtenerHistorialVersiones(Long formatoAId) {
        System.out.println("📊 CONSULTANDO HISTORIAL para FormatoA: " + formatoAId);
        List<RequestMemento> historial = historyManager.getRequestHistory("FORMATO_A", formatoAId);
        System.out.println("📈 Historial encontrado: " + historial.size() + " versiones");
        return historial;
    }

    public RequestMemento obtenerEstadoVersion(Long formatoAId, int version) {
        System.out.println("🔍 BUSCANDO versión " + version + " para FormatoA: " + formatoAId);
        RequestMemento memento = historyManager.restoreToRequestVersion("FORMATO_A", formatoAId, version);
        System.out.println("✅ Versión " + version + " encontrada - Estado: " + memento.getEstado());
        return memento;
    }

    public RequestMemento obtenerUltimoEstado(Long formatoAId) {
        System.out.println("📈 CONSULTANDO último estado para FormatoA: " + formatoAId);
        RequestMemento memento = historyManager.getLastRequest("FORMATO_A", formatoAId);
        if (memento != null) {
            System.out.println("✅ Último estado - Versión: " + memento.getVersion() + " | Estado: " + memento.getEstado());
        } else {
            System.out.println("ℹ️ No hay historial para FormatoA: " + formatoAId);
        }
        return memento;
    }

    /**
     * ✅ RESTAURAR A VERSIÓN ANTERIOR
     */
    @Transactional
    public FormatoAVersion restaurarAVersion(Long formatoAId, int version) {
        System.out.println("⏪ RESTAURANDO a versión " + version + " para FormatoA: " + formatoAId);

        RequestMemento memento = historyManager.restoreToRequestVersion("FORMATO_A", formatoAId, version);

        // Crear nueva versión basada en el memento
        Map<String, Object> requestData = memento.getRequestData();
        FormatoAVersionRequest request = convertirMapARequest(requestData);

        FormatoAVersion versionRestaurada = convertirRequestAEntity(request);
        versionRestaurada.setId(null); // Para que sea nueva entidad

        FormatoAVersion guardada = versionRepository.save(versionRestaurada);

        // Guardar en historial como nueva versión
        RequestMemento nuevoMemento = historyManager.saveRequestState("FORMATO_A",
                guardada.getId(), guardada.getState().name(), requestData);

        System.out.println("✅ VERSIÓN RESTAURADA - Nueva ID: " + guardada.getId() +
                " | Nueva versión Memento: " + nuevoMemento.getVersion());

        return guardada;
    }

    // ========== MÉTODOS PRIVADOS DE CONVERSIÓN ==========

    /**
     * ✅ CONVERTIR REQUEST A ENTITY
     */
    private FormatoAVersion convertirRequestAEntity(FormatoAVersionRequest request) {
        FormatoAVersion entity = new FormatoAVersion();
        entity.setNumeroVersion(request.numVersion());
        entity.setFecha(request.fecha());
        entity.setTitle(request.titulo());
        entity.setMode(EnumModalidad.valueOf(request.modalidad()));
        entity.setState(EnumEstado.valueOf(request.estado()));
        entity.setObservations(request.observaciones());
        entity.setCounter(request.counter());
        entity.setIdFormatoA(request.idFormatoA());

        if (request.id() != null) {
            entity.setId(request.id());
        }

        return entity;
    }

    /**
     * ✅ ACTUALIZAR ENTIDAD DESDE REQUEST
     */
    private void actualizarEntityDesdeRequest(FormatoAVersion entity, FormatoAVersionRequest request) {
        System.out.println("🔄 ACTUALIZANDO entidad desde Request");
        System.out.println("   Estado: " + entity.getState() + " → " + request.estado());
        System.out.println("   Counter: " + entity.getCounter() + " → " + request.counter());
        System.out.println("   Observaciones: " + entity.getObservations() + " → " + request.observaciones());

        entity.setState(EnumEstado.valueOf(request.estado()));
        entity.setObservations(request.observaciones());
        entity.setCounter(request.counter());
        entity.setTitle(request.titulo());
        entity.setMode(EnumModalidad.valueOf(request.modalidad()));
        entity.setNumeroVersion(request.numVersion());
    }

    /**
     * ✅ CONVERTIR REQUEST A MAP (para Memento)
     */
    private Map<String, Object> convertirRequestAMap(FormatoAVersionRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", request.id());
        map.put("numVersion", request.numVersion());
        map.put("fecha", request.fecha());
        map.put("titulo", request.titulo());
        map.put("modalidad", request.modalidad());
        map.put("estado", request.estado());
        map.put("observaciones", request.observaciones());
        map.put("counter", request.counter());
        map.put("idFormatoA", request.idFormatoA());

        System.out.println("🗂️ Request convertido a Map - Campos: " + map.size());
        return map;
    }

    /**
     * ✅ CREAR SNAPSHOT DE ENTIDAD (para guardar estado anterior)
     */
    private Map<String, Object> crearSnapshotEntity(FormatoAVersion entity) {
        Map<String, Object> snapshot = new HashMap<>();
        snapshot.put("id", entity.getId());
        snapshot.put("numeroVersion", entity.getNumeroVersion());
        snapshot.put("fecha", entity.getFecha());
        snapshot.put("titulo", entity.getTitle());
        snapshot.put("modalidad", entity.getMode().name());
        snapshot.put("estado", entity.getState().name());
        snapshot.put("observaciones", entity.getObservations());
        snapshot.put("counter", entity.getCounter());
        snapshot.put("idFormatoA", entity.getIdFormatoA());

        System.out.println("📸 Snapshot creado de entidad - ID: " + entity.getId());
        return snapshot;
    }

    /**
     * ✅ CONVERTIR MAP A REQUEST (para restauración)
     */
    private FormatoAVersionRequest convertirMapARequest(Map<String, Object> map) {
        return new FormatoAVersionRequest(
                (Long) map.get("id"),
                (Integer) map.get("numVersion"),
                (LocalDate) map.get("fecha"),
                (String) map.get("titulo"),
                (String) map.get("modalidad"),
                (String) map.get("estado"),
                (String) map.get("observaciones"),
                (Integer) map.get("counter"),
                (Long) map.get("idFormatoA")
        );
    }

    /**
     * ✅ CONVERTIR ENTITY A RESPONSE
     */
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

    // ========== MÉTODOS ADICIONALES DE CONSULTA ==========

    @Transactional(readOnly = true)
    public List<FormatoAVersion> buscarPorFormatoA(Long idFormatoA) {
        System.out.println("🔍 BUSCANDO versiones por FormatoA: " + idFormatoA);
        List<FormatoAVersion> versiones = versionRepository.findByIdFormatoA(idFormatoA);
        System.out.println("✅ Versiones encontradas: " + versiones.size());
        return versiones;
    }

    @Transactional(readOnly = true)
    public Optional<FormatoAVersion> buscarUltimaVersionPorFormatoA(Long idFormatoA) {
        System.out.println("🔍 BUSCANDO última versión por FormatoA: " + idFormatoA);
        List<FormatoAVersion> versiones = versionRepository.findByIdFormatoA(idFormatoA);
        Optional<FormatoAVersion> ultimaVersion = versiones.stream().findFirst();

        if (ultimaVersion.isPresent()) {
            System.out.println("✅ Última versión encontrada: v" + ultimaVersion.get().getNumeroVersion());
        } else {
            System.out.println("ℹ️ No hay versiones para FormatoA: " + idFormatoA);
        }

        return ultimaVersion;
    }

    /**
     * ✅ GENERAR NUEVO ID MANUALMENTE
     */
    private Long generarNuevoId() {
        try {
            // Estrategia 1: Buscar el máximo ID existente y sumar 1
            Long maxId = versionRepository.findMaxId();
            Long nuevoId = (maxId != null) ? maxId + 1 : 1L;
            System.out.println("🔧 ID generado: " + nuevoId + " (maxId encontrado: " + maxId + ")");
            return nuevoId;
        } catch (Exception e) {
            // Estrategia 2: Usar timestamp si hay error
            Long timestampId = System.currentTimeMillis();
            System.out.println("⚠️ Usando ID por timestamp: " + timestampId);
            return timestampId;
        }
    }
}