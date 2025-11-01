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

    @Transactional
    public FormatoAVersion crearVersion(FormatoAVersionRequest request) {
        System.out.println(" VERSION DE FORMATOA RECIBIDA ");

        // VERIFICACIÓN DEL ID - Si viene en el request, usarlo directamente
        FormatoAVersion version;
        if (request.id() != null) {
            // Buscar si ya existe una versión con ese ID
            Optional<FormatoAVersion> versionExistente = versionRepository.findById(request.id());
            if (versionExistente.isPresent()) {
                // ACTUALIZAR versión existente
                version = versionExistente.get();
                actualizarVersionDesdeRequest(version, request);
                System.out.println(" ACTUALIZANDO versión existente - ID: " + version.getId());
            } else {
                // CREAR nueva versión con el ID proporcionado
                version = convertirRequestAEntity(request);
                // 🔹 El ID ya viene asignado desde el DTO
                System.out.println(" CREANDO nueva versión con ID proporcionado: " + request.id());
            }
        } else {
            throw new IllegalArgumentException("El ID es requerido para crear/actualizar una versión");
        }

        System.out.println("   - id: " + request.id());
        System.out.println("   - numVersion: " + request.numVersion());
        System.out.println("   - counter: " + request.counter());
        System.out.println("   - estado: " + request.estado());
        System.out.println("   - fecha: " + request.fecha());
        System.out.println("   - titulo: " + request.titulo());
        System.out.println("   - modalidad: " + request.modalidad());
        System.out.println("   - idFormatoA: " + request.idFormatoA());

        FormatoAVersion guardada = versionRepository.save(version);

        // GUARDAR REQUEST ORIGINAL EN MEMENTO
        Map<String, Object> requestData = convertirRequestAMap(request);
        RequestMemento memento = historyManager.saveRequestState("FORMATO_A", guardada.getId(), request.estado(), requestData);

        System.out.println(" VERSIÓN CREADA con Memento - ID: " + guardada.getId() +
                " | Versión Memento: " + memento.getVersion());

        return guardada;
    }

    /**
     * PROCESAR VERSIÓN RECIBIDA (REQUEST) CON MEMENTO - CREA NUEVA VERSIÓN EN LUGAR DE ACTUALIZAR
     */
    @Transactional
    public void procesarVersionRecibida(FormatoAVersionRequest request) {
        try {
            FormatoAVersion ultimaVersion = null;

            // BUSCAR VERSIONES EXISTENTES POR FormatoA ID
            if (request.idFormatoA() != null) {
                List<FormatoAVersion> versiones = versionRepository.findByIdFormatoAOrderByNumeroVersionDesc(request.idFormatoA());
                if (!versiones.isEmpty()) {
                    ultimaVersion = versiones.get(0);
                    System.out.println(" Encontrada última versión por FormatoA ID: " + request.idFormatoA() +
                            " - Versión actual: v" + ultimaVersion.getNumeroVersion() +
                            " - Counter actual: " + ultimaVersion.getCounter());
                }
            }

            if (ultimaVersion != null) {
                System.out.println("🔄 CREANDO NUEVA VERSIÓN basada en versión existente - FormatoA ID: " + ultimaVersion.getIdFormatoA() +
                        " | Versión anterior: v" + ultimaVersion.getNumeroVersion() +
                        " | Counter anterior: " + ultimaVersion.getCounter());

                // CREAR NUEVA VERSIÓN INCREMENTANDO EL NÚMERO DE VERSIÓN
                FormatoAVersion nuevaVersion = crearNuevaVersionDesdeAnterior(ultimaVersion, request);
                FormatoAVersion guardada = versionRepository.save(nuevaVersion);

                // GUARDAR NUEVO REQUEST EN MEMENTO
                Map<String, Object> requestData = convertirEntityAMap(guardada);
                RequestMemento mementoNuevo = historyManager.saveRequestState("FORMATO_A",
                        guardada.getId(), guardada.getState().name(), requestData);

                System.out.println(" NUEVA VERSIÓN CREADA - ID: " + guardada.getId() +
                        " | Nueva versión: v" + guardada.getNumeroVersion() +
                        " | Versión Memento: " + mementoNuevo.getVersion() +
                        " | Counter nuevo: " + guardada.getCounter());

            } else {
                System.out.println(" NUEVA VERSIÓN INICIAL - FormatoA: " + request.idFormatoA() +
                        " | Counter inicial: " + request.counter());

                // CREAR VERSIÓN INICIAL (v1)
                FormatoAVersion nuevaVersion = convertirRequestAEntity(request);

                // 🔹 Asegurar que sea versión 1 si no viene especificada
                if (nuevaVersion.getNumeroVersion() == 0) {
                    nuevaVersion.setNumeroVersion(1);
                }

                // 🔹 Asegurar que tenga un ID válido
                if (nuevaVersion.getId() == null) {
                    nuevaVersion.setId(generarNuevoId());
                }

                FormatoAVersion guardada = versionRepository.save(nuevaVersion);

                // GUARDAR EN MEMENTO
                Map<String, Object> requestData = convertirEntityAMap(guardada);
                RequestMemento memento = historyManager.saveRequestState("FORMATO_A",
                        guardada.getId(), guardada.getState().name(), requestData);

                System.out.println(" VERSIÓN INICIAL CREADA - ID: " + guardada.getId() +
                        " | Versión: v" + guardada.getNumeroVersion() +
                        " | Versión Memento: " + memento.getVersion() +
                        " | Counter final: " + guardada.getCounter());
            }

        } catch (Exception e) {
            System.out.println(" ERROR procesando versión: " + e.getMessage());
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
     * ✅ ACTUALIZAR VERSIÓN ESPECÍFICA CREANDO NUEVA VERSIÓN
     */
    @Transactional
    public FormatoAVersionResponse actualizarVersion(Long id, FormatoAVersionRequest request) {
        System.out.println(" ACTUALIZANDO VERSIÓN específica: " + id + " - CREANDO NUEVA VERSIÓN");

        FormatoAVersion versionExistente = versionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Versión no encontrada con ID: " + id));

        // CREAR NUEVA VERSIÓN INCREMENTANDO EL NÚMERO
        FormatoAVersion nuevaVersion = crearNuevaVersionDesdeAnterior(versionExistente, request);
        FormatoAVersion guardada = versionRepository.save(nuevaVersion);

        // GUARDAR NUEVO ESTADO EN MEMENTO
        Map<String, Object> requestData = convertirEntityAMap(guardada);
        RequestMemento memento = historyManager.saveRequestState("FORMATO_A",
                guardada.getId(), guardada.getState().name(), requestData);

        FormatoAVersionResponse response = convertirAResponse(guardada);

        System.out.println(" NUEVA VERSIÓN CREADA - ID anterior: " + id +
                " | Nueva ID: " + guardada.getId() +
                " | Nueva versión: v" + guardada.getNumeroVersion() +
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
     * ✅ RESTAURAR A VERSIÓN ANTERIOR CREANDO NUEVA VERSIÓN
     */
    @Transactional
    public FormatoAVersion restaurarAVersion(Long formatoAId, int version) {
        System.out.println("⏪ RESTAURANDO a versión " + version + " para FormatoA: " + formatoAId + " - CREANDO NUEVA VERSIÓN");

        RequestMemento memento = historyManager.restoreToRequestVersion("FORMATO_A", formatoAId, version);

        // Obtener la versión actual para determinar el siguiente número de versión
        List<FormatoAVersion> versionesActuales = versionRepository.findByIdFormatoAOrderByNumeroVersionDesc(formatoAId);
        Integer siguienteVersion = 1;

        if (!versionesActuales.isEmpty()) {
            siguienteVersion = versionesActuales.get(0).getNumeroVersion() + 1;
        }

        // Crear nueva versión basada en el memento
        Map<String, Object> requestData = memento.getRequestData();

        FormatoAVersion versionRestaurada = convertirMapAEntity(requestData);
        versionRestaurada.setNumeroVersion(siguienteVersion); // Incrementar versión
        // NO establecer ID - dejar que JPA lo genere automáticamente

        FormatoAVersion guardada = versionRepository.save(versionRestaurada);

        // Guardar en historial como nueva versión
        RequestMemento nuevoMemento = historyManager.saveRequestState("FORMATO_A",
                guardada.getId(), guardada.getState().name(), requestData);

        System.out.println("✅ NUEVA VERSIÓN RESTAURADA - Nueva ID: " + guardada.getId() +
                " | Nueva versión: v" + guardada.getNumeroVersion() +
                " | Nueva versión Memento: " + nuevoMemento.getVersion());

        return guardada;
    }

    // ========== MÉTODOS PRIVADOS DE CONVERSIÓN ==========

    /**
     * ✅ CONVERTIR REQUEST A ENTITY
     */
    private FormatoAVersion convertirRequestAEntity(FormatoAVersionRequest request) {
        FormatoAVersion entity = new FormatoAVersion();
        // NO establecer el ID - dejar que JPA lo genere
        entity.setId(request.id());
        entity.setNumeroVersion(request.numVersion());
        entity.setFecha(request.fecha());
        entity.setTitle(request.titulo());
        entity.setMode(EnumModalidad.valueOf(request.modalidad()));
        entity.setState(EnumEstado.valueOf(request.estado()));
        entity.setObservations(request.observaciones());
        entity.setCounter(request.counter());
        entity.setIdFormatoA(request.idFormatoA());

        return entity;
    }

    /**
     * ✅ CREAR NUEVA VERSIÓN DESDE VERSIÓN ANTERIOR
     */
    private FormatoAVersion crearNuevaVersionDesdeAnterior(FormatoAVersion versionAnterior, FormatoAVersionRequest request) {
        FormatoAVersion nuevaVersion = new FormatoAVersion();

        Long nuevoId = generarNuevoId();
        nuevaVersion.setId(nuevoId);

        // Incrementar número de versión
        Integer nuevaVersionNum = versionAnterior.getNumeroVersion() + 1;

        // Copiar datos de la versión anterior y aplicar cambios del request
        // NO establecer ID - dejar que JPA lo genere
        nuevaVersion.setNumeroVersion(nuevaVersionNum);
        nuevaVersion.setFecha(request.fecha() != null ? request.fecha() : versionAnterior.getFecha());
        nuevaVersion.setTitle(request.titulo() != null ? request.titulo() : versionAnterior.getTitle());
        nuevaVersion.setMode(request.modalidad() != null ? EnumModalidad.valueOf(request.modalidad()) : versionAnterior.getMode());
        nuevaVersion.setState(request.estado() != null ? EnumEstado.valueOf(request.estado()) : versionAnterior.getState());
        nuevaVersion.setObservations(request.observaciones() != null ? request.observaciones() : versionAnterior.getObservations());
        nuevaVersion.setCounter(request.counter() != null ? request.counter() : versionAnterior.getCounter());
        nuevaVersion.setIdFormatoA(versionAnterior.getIdFormatoA()); // Mantener mismo FormatoA ID

        System.out.println("🔄 CREANDO NUEVA VERSIÓN - De v" + versionAnterior.getNumeroVersion() + " a v" + nuevaVersionNum);
        System.out.println("   Counter: " + versionAnterior.getCounter() + " → " + nuevaVersion.getCounter());
        System.out.println("   Estado: " + versionAnterior.getState() + " → " + nuevaVersion.getState());

        return nuevaVersion;
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
     * ✅ CONVERTIR ENTITY A MAP (para Memento)
     */
    private Map<String, Object> convertirEntityAMap(FormatoAVersion entity) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", entity.getId());
        map.put("numVersion", entity.getNumeroVersion());
        map.put("fecha", entity.getFecha());
        map.put("titulo", entity.getTitle());
        map.put("modalidad", entity.getMode().name());
        map.put("estado", entity.getState().name());
        map.put("observaciones", entity.getObservations());
        map.put("counter", entity.getCounter());
        map.put("idFormatoA", entity.getIdFormatoA());

        System.out.println("🗂️ Entity convertido a Map - ID: " + entity.getId());
        return map;
    }

    /**
     * ✅ CONVERTIR MAP A ENTITY (para restauración)
     */
    private FormatoAVersion convertirMapAEntity(Map<String, Object> map) {
        FormatoAVersion entity = new FormatoAVersion();
        // NO establecer ID - dejar que JPA lo genere
        entity.setNumeroVersion((Integer) map.get("numVersion"));
        entity.setFecha((LocalDate) map.get("fecha"));
        entity.setTitle((String) map.get("titulo"));
        entity.setMode(EnumModalidad.valueOf((String) map.get("modalidad")));
        entity.setState(EnumEstado.valueOf((String) map.get("estado")));
        entity.setObservations((String) map.get("observaciones"));
        entity.setCounter((Integer) map.get("counter"));
        entity.setIdFormatoA((Long) map.get("idFormatoA"));

        return entity;
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
        List<FormatoAVersion> versiones = versionRepository.findByIdFormatoAOrderByNumeroVersionDesc(idFormatoA);
        System.out.println("✅ Versiones encontradas: " + versiones.size());
        return versiones;
    }

    @Transactional(readOnly = true)
    public Optional<FormatoAVersion> buscarUltimaVersionPorFormatoA(Long idFormatoA) {
        System.out.println("🔍 BUSCANDO última versión por FormatoA: " + idFormatoA);
        List<FormatoAVersion> versiones = versionRepository.findByIdFormatoAOrderByNumeroVersionDesc(idFormatoA);
        Optional<FormatoAVersion> ultimaVersion = versiones.stream().findFirst();

        if (ultimaVersion.isPresent()) {
            System.out.println("✅ Última versión encontrada: v" + ultimaVersion.get().getNumeroVersion());
        } else {
            System.out.println("ℹ️ No hay versiones para FormatoA: " + idFormatoA);
        }

        return ultimaVersion;
    }

    /**
     * ✅ OBTENER HISTORIAL DE VERSIONES POR FormatoA
     */
    @Transactional(readOnly = true)
    public List<FormatoAVersion> obtenerHistorialCompletoPorFormatoA(Long idFormatoA) {
        System.out.println("📊 CONSULTANDO HISTORIAL COMPLETO para FormatoA: " + idFormatoA);
        List<FormatoAVersion> historial = versionRepository.findByIdFormatoAOrderByNumeroVersionAsc(idFormatoA);
        System.out.println("📈 Historial completo encontrado: " + historial.size() + " versiones");
        return historial;
    }

    // Método auxiliar para actualizar una versión existente desde el request
    private void actualizarVersionDesdeRequest(FormatoAVersion version, FormatoAVersionRequest request) {
        // No actualizar el ID ya que es el mismo
        version.setNumeroVersion(request.numVersion());
        version.setCounter(request.counter());
        version.setState(EnumEstado.valueOf(request.estado()));
        version.setFecha(request.fecha());
        version.setTitle(request.titulo());
        version.setMode(EnumModalidad.valueOf(request.modalidad()));
        version.setIdFormatoA(request.idFormatoA());
        // Actualizar otros campos según sea necesario


    }

    private Long generarNuevoId() {
        try {
            Long maxId = versionRepository.findMaxId();
            Long nuevoId = (maxId != null) ? maxId + 1 : 1L;
            System.out.println("🆔 GENERADO nuevo ID: " + nuevoId);
            return nuevoId;
        } catch (Exception e) {
            // Si hay error al obtener el máximo ID, usar timestamp
            Long timestampId = System.currentTimeMillis();
            System.out.println("🆔 GENERADO ID por timestamp: " + timestampId);
            return timestampId;
        }
    }
}