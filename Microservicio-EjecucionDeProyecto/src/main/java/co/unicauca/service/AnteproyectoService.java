package co.unicauca.service;

import co.unicauca.entity.Anteproyecto;
import co.unicauca.entity.EnumEstadoAnteproyecto;
import co.unicauca.entity.ProyectoGrado;
import co.unicauca.entity.FormatoAVersion;
import co.unicauca.infra.dto.AnteproyectoRequest;
import co.unicauca.infra.dto.AnteproyectoResponse;
import co.unicauca.infra.memento.RequestHistoryManager;
import co.unicauca.infra.memento.RequestMemento;
import co.unicauca.repository.AnteproyectoRepository;
import co.unicauca.repository.ProyectoGradoRepository;
import co.unicauca.repository.FormatoAVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnteproyectoService {

    private final AnteproyectoRepository anteproyectoRepository;
    private final ProyectoGradoRepository proyectoGradoRepository;
    private final FormatoAVersionRepository formatoAVersionRepository;
    private final RequestHistoryManager historyManager;

    /**
     * ✅ CORREGIDO: Procesar request SIN conflicto de IDs
     */
    @Transactional
    public void procesarAnteproyectoRequest(AnteproyectoRequest request) {
        System.out.println("📥 [SERVICE] Procesando Anteproyecto Request: " + request.titulo() +
                " | ID recibido: " + request.id() +
                " | ProyectoGrado: " + request.idProyectoGrado());

        try {
            // Validar que el proyecto exista
            ProyectoGrado proyecto = proyectoGradoRepository.findById(request.idProyectoGrado())
                    .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + request.idProyectoGrado()));

            Anteproyecto anteproyectoExistente = null;
            String accion = "";

            // 🔍 ESTRATEGIA 1: Buscar por ID si viene en el request
            if (request.id() != null) {
                anteproyectoExistente = anteproyectoRepository.findById(request.id()).orElse(null);
                if (anteproyectoExistente != null) {
                    accion = "ACTUALIZAR_POR_ID";
                    System.out.println("🔍 Anteproyecto encontrado por ID: " + request.id());
                }
            }

            // 🔍 ESTRATEGIA 2: Buscar por ProyectoGrado si no se encontró por ID
            if (anteproyectoExistente == null) {
                List<Anteproyecto> existentes = anteproyectoRepository.findAllByProyectoGradoId(request.idProyectoGrado());
                if (!existentes.isEmpty()) {
                    anteproyectoExistente = existentes.get(0);
                    accion = "ACTUALIZAR_POR_PROYECTO";
                    System.out.println("🔍 Anteproyecto encontrado por ProyectoGrado: " + request.idProyectoGrado());
                }
            }

            // 🎯 EJECUTAR ACCIÓN
            if (anteproyectoExistente != null) {
                System.out.println("🔄 " + accion + " - ID: " + anteproyectoExistente.getId());
                actualizarAnteproyectoExistente(anteproyectoExistente, request, proyecto);
            } else {
                System.out.println("🆕 CREAR_NUEVO - No existe anteproyecto para el proyecto: " + request.idProyectoGrado());
                crearNuevoAnteproyectoSinId(request, proyecto);
            }

            System.out.println("✅ [SERVICE] Anteproyecto Request procesado exitosamente: " + request.titulo());

        } catch (Exception e) {
            System.out.println("❌ [SERVICE] Error procesando Anteproyecto Request: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error procesando anteproyecto request", e);
        }
    }

    /**
     * ✅ CORREGIDO: Crear nuevo anteproyecto SIN asignar ID manual
     */
    private void crearNuevoAnteproyectoSinId(AnteproyectoRequest request, ProyectoGrado proyecto) {
        System.out.println("🆕 CREANDO NUEVO ANTEPROYECTO CON ID MANUAL:");
        System.out.println("   - ID: " + request.id());
        System.out.println("   - Título: " + request.titulo());
        System.out.println("   - Proyecto ID: " + proyecto.getId());

        // ✅ Validar que el ID venga en el request
        if (request.id() == null) {
            throw new RuntimeException("❌ ID es requerido para creación manual de anteproyecto");
        }

        // ✅ Crear nueva entidad CON asignación manual de ID
        Anteproyecto nuevoAnteproyecto = new Anteproyecto();
        nuevoAnteproyecto.setId(request.id()); // ← ASIGNAR ID MANUALMENTE
        nuevoAnteproyecto.setTitulo(request.titulo());
        nuevoAnteproyecto.setFecha(request.fecha());
        nuevoAnteproyecto.setEstado(EnumEstadoAnteproyecto.valueOf(request.estado()));
        nuevoAnteproyecto.setObservaciones(request.observaciones());
        nuevoAnteproyecto.setProyectoGrado(proyecto);

        Anteproyecto guardado = anteproyectoRepository.save(nuevoAnteproyecto);

        // 💾 GUARDAR EN MEMENTO
        Map<String, Object> requestData = convertirRequestAMap(request);
        RequestMemento memento = historyManager.saveRequestState("ANTEPROYECTO",
                guardado.getId(), guardado.getEstado().name(), requestData);

        System.out.println("✅ ANTEPROYECTO CREADO CON ID MANUAL - ID: " + guardado.getId() +
                " | Versión Memento: " + memento.getVersion() +
                " | Estado: " + guardado.getEstado());
    }
    /**
     * ✅ ACTUALIZAR ANTEPROYECTO EXISTENTE CON MEMENTO
     */
    private void actualizarAnteproyectoExistente(Anteproyecto anteproyectoExistente, AnteproyectoRequest request, ProyectoGrado proyecto) {
        System.out.println("🔄 ACTUALIZANDO ANTEPROYECTO EXISTENTE:");
        System.out.println("   - ID: " + anteproyectoExistente.getId());
        System.out.println("   - Título: " + anteproyectoExistente.getTitulo() + " → " + request.titulo());
        System.out.println("   - Estado: " + anteproyectoExistente.getEstado() + " → " + request.estado());

        // 💾 GUARDAR ESTADO ACTUAL EN MEMENTO
        Map<String, Object> estadoAnterior = crearSnapshotEntity(anteproyectoExistente);
        RequestMemento mementoAnterior = historyManager.saveRequestState("ANTEPROYECTO",
                anteproyectoExistente.getId(), anteproyectoExistente.getEstado().name(), estadoAnterior);

        System.out.println("💾 Estado anterior guardado - Versión: " + mementoAnterior.getVersion());

        // ✏️ ACTUALIZAR CAMPOS
        anteproyectoExistente.setTitulo(request.titulo());
        anteproyectoExistente.setFecha(request.fecha());
        anteproyectoExistente.setEstado(EnumEstadoAnteproyecto.valueOf(request.estado()));
        anteproyectoExistente.setObservaciones(request.observaciones());
        anteproyectoExistente.setProyectoGrado(proyecto);

        Anteproyecto actualizado = anteproyectoRepository.save(anteproyectoExistente);

        // 💾 GUARDAR NUEVO ESTADO EN MEMENTO
        Map<String, Object> requestData = convertirRequestAMap(request);
        RequestMemento mementoNuevo = historyManager.saveRequestState("ANTEPROYECTO",
                actualizado.getId(), actualizado.getEstado().name(), requestData);

        System.out.println("✅ ANTEPROYECTO ACTUALIZADO - ID: " + actualizado.getId() +
                " | Versión Memento: " + mementoNuevo.getVersion() +
                " | Estado: " + actualizado.getEstado() +
                " | Cambios: " + (mementoNuevo.getVersion() - mementoAnterior.getVersion()));
    }

    // ========== MÉTODOS PÚBLICOS PARA API ==========

    /**
     * ✅ CREAR ANTEPROYECTO DESDE API CON MEMENTO
     */
    @Transactional
    public AnteproyectoResponse crearAnteproyecto(AnteproyectoRequest request) {
        System.out.println("📄 CREAR ANTEPROYECTO desde API: " + request.titulo());

        // Validar que el proyecto exista
        ProyectoGrado proyecto = proyectoGradoRepository.findById(request.idProyectoGrado())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + request.idProyectoGrado()));

        Anteproyecto anteproyecto = convertirRequestAEntity(request, proyecto);
        Anteproyecto guardado = anteproyectoRepository.save(anteproyecto);

        // 💾 GUARDAR EN MEMENTO
        Map<String, Object> requestData = convertirRequestAMap(request);
        RequestMemento memento = historyManager.saveRequestState("ANTEPROYECTO",
                guardado.getId(), guardado.getEstado().name(), requestData);

        AnteproyectoResponse response = convertirAResponse(guardado);

        System.out.println("✅ ANTEPROYECTO CREADO desde API - ID: " + response.id() +
                " | Versión Memento: " + memento.getVersion() +
                " | Estado: " + guardado.getEstado());

        return response;
    }

    /**
     * ✅ ACTUALIZAR ANTEPROYECTO CON MEMENTO
     */
    @Transactional
    public AnteproyectoResponse actualizarAnteproyecto(Long id, AnteproyectoRequest request) {
        System.out.println("✏️ ACTUALIZANDO ANTEPROYECTO: " + id);

        Anteproyecto anteproyecto = anteproyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Anteproyecto no encontrado"));

        ProyectoGrado proyecto = proyectoGradoRepository.findById(request.idProyectoGrado())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + request.idProyectoGrado()));

        // 💾 GUARDAR ESTADO ACTUAL EN MEMENTO
        Map<String, Object> estadoAnterior = crearSnapshotEntity(anteproyecto);
        historyManager.saveRequestState("ANTEPROYECTO", anteproyecto.getId(),
                anteproyecto.getEstado().name(), estadoAnterior);

        anteproyecto.setTitulo(request.titulo());
        anteproyecto.setFecha(request.fecha());
        anteproyecto.setEstado(EnumEstadoAnteproyecto.valueOf(request.estado()));
        anteproyecto.setObservaciones(request.observaciones());
        anteproyecto.setProyectoGrado(proyecto);

        Anteproyecto actualizado = anteproyectoRepository.save(anteproyecto);

        // 💾 GUARDAR NUEVO ESTADO EN MEMENTO
        Map<String, Object> requestData = convertirRequestAMap(request);
        RequestMemento memento = historyManager.saveRequestState("ANTEPROYECTO",
                actualizado.getId(), actualizado.getEstado().name(), requestData);

        AnteproyectoResponse response = convertirAResponse(actualizado);

        System.out.println("✅ ANTEPROYECTO ACTUALIZADO: " + id +
                " | Versión Memento: " + memento.getVersion() +
                " | Nuevo estado: " + actualizado.getEstado());

        return response;
    }

    // ========== MÉTODOS DE CONSULTA ==========

    @Transactional(readOnly = true)
    public AnteproyectoResponse buscarPorId(Long id) {
        System.out.println("🔍 BUSCANDO anteproyecto por ID: " + id);

        Anteproyecto anteproyecto = anteproyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Anteproyecto no encontrado"));

        System.out.println("✅ ANTEPROYECTO ENCONTRADO: " + anteproyecto.getId() + " - " + anteproyecto.getTitulo());
        return convertirAResponse(anteproyecto);
    }

    @Transactional(readOnly = true)
    public List<AnteproyectoResponse> listarTodos() {
        System.out.println("📋 LISTANDO todos los anteproyectos");

        List<Anteproyecto> anteproyectos = anteproyectoRepository.findAll();
        List<AnteproyectoResponse> responses = anteproyectos.stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());

        System.out.println("✅ Anteproyectos encontrados: " + responses.size());
        return responses;
    }

    @Transactional(readOnly = true)
    public List<AnteproyectoResponse> buscarPorProyecto(Long proyectoId) {
        System.out.println("🔍 BUSCANDO anteproyectos por proyecto: " + proyectoId);

        List<Anteproyecto> anteproyectos = anteproyectoRepository.findAllByProyectoGradoId(proyectoId);
        List<AnteproyectoResponse> responses = anteproyectos.stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());

        System.out.println("✅ Anteproyectos encontrados para proyecto " + proyectoId + ": " + responses.size());
        return responses;
    }

    // ========== MÉTODOS DE CONSULTA DE RELACIONES COMPLETAS ==========

    /**
     * ✅ CONSULTAR RELACIÓN COMPLETA: Anteproyecto → Proyecto → FormatoA
     */
    @Transactional(readOnly = true)
    public void mostrarRelacionCompleta(Long anteproyectoId) {
        System.out.println("🔗 MOSTRANDO RELACIÓN COMPLETA para Anteproyecto: " + anteproyectoId);

        // 1. Buscar Anteproyecto
        Anteproyecto anteproyecto = anteproyectoRepository.findById(anteproyectoId)
                .orElseThrow(() -> new RuntimeException("Anteproyecto no encontrado"));

        // 2. Obtener Proyecto relacionado
        ProyectoGrado proyecto = anteproyecto.getProyectoGrado();
        if (proyecto == null) {
            System.out.println("❌ No hay proyecto asociado al anteproyecto");
            return;
        }

        // 3. Buscar FormatoA relacionado
        List<FormatoAVersion> formatoAVersions = formatoAVersionRepository.findByIdFormatoA(proyecto.getIdFormatoA());
        FormatoAVersion formatoA = formatoAVersions.isEmpty() ? null : formatoAVersions.get(0);

        // 🎯 MOSTRAR RELACIÓN EN CONSOLA
        System.out.println("📊 ===== RELACIÓN COMPLETA =====");
        System.out.println("🏷️  ANTEPROYECTO:");
        System.out.println("   - ID: " + anteproyecto.getId());
        System.out.println("   - Título: " + anteproyecto.getTitulo());
        System.out.println("   - Estado: " + anteproyecto.getEstado());
        System.out.println("   - Fecha: " + anteproyecto.getFecha());
        System.out.println("   - Observaciones: " + anteproyecto.getObservaciones());

        System.out.println("📋 PROYECTO:");
        System.out.println("   - ID: " + proyecto.getId());
        System.out.println("   - Título: " + proyecto.getNombre());
        System.out.println("   - ID FormatoA: " + proyecto.getIdFormatoA());
        System.out.println("   - Estado: " + proyecto.getEstado());

        System.out.println("📑 FORMatoA:");
        if (formatoA != null) {
            System.out.println("   - ID: " + formatoA.getId());
            System.out.println("   - Título: " + formatoA.getTitle());
            System.out.println("   - Versión: " + formatoA.getNumeroVersion());
            System.out.println("   - Estado: " + formatoA.getState());
            System.out.println("   - Counter: " + formatoA.getCounter());
            System.out.println("   - Modalidad: " + formatoA.getMode());
            System.out.println("   - Fecha: " + formatoA.getFecha());
            System.out.println("   - Observaciones: " + formatoA.getObservations());
        } else {
            System.out.println("   - ❌ No se encontró FormatoA para ID: " + proyecto.getIdFormatoA());
        }
        System.out.println("📊 =============================");
    }

    /**
     * ✅ CONSULTAR RELACIÓN POR PROYECTO
     */
    @Transactional(readOnly = true)
    public void mostrarRelacionPorProyecto(Long proyectoId) {
        System.out.println("🔗 MOSTRANDO RELACIÓN por Proyecto: " + proyectoId);

        // 1. Buscar Proyecto
        ProyectoGrado proyecto = proyectoGradoRepository.findById(proyectoId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        // 2. Buscar Anteproyectos relacionados
        List<Anteproyecto> anteproyectos = anteproyectoRepository.findAllByProyectoGradoId(proyectoId);

        // 3. Buscar FormatoA relacionado
        List<FormatoAVersion> formatoAVersions = formatoAVersionRepository.findByIdFormatoA(proyecto.getIdFormatoA());
        FormatoAVersion formatoA = formatoAVersions.isEmpty() ? null : formatoAVersions.get(0);

        // 🎯 MOSTRAR EN CONSOLA
        System.out.println("📊 ===== RELACIÓN POR PROYECTO =====");
        System.out.println("📋 PROYECTO:");
        System.out.println("   - ID: " + proyecto.getId());
        System.out.println("   - Título: " + proyecto.getEstado());
        System.out.println("   - ID FormatoA: " + proyecto.getIdFormatoA());
        System.out.println("   - Estado: " + proyecto.getEstado());

        System.out.println("🏷️  ANTEPROYECTOS (" + anteproyectos.size() + "):");
        if (anteproyectos.isEmpty()) {
            System.out.println("   - ❌ No hay anteproyectos para este proyecto");
        } else {
            anteproyectos.forEach(anteproyecto -> {
                System.out.println("   - ID: " + anteproyecto.getId() +
                        " | Título: " + anteproyecto.getTitulo() +
                        " | Estado: " + anteproyecto.getEstado());
            });
        }

        System.out.println("📑 FORMatoA:");
        if (formatoA != null) {
            System.out.println("   - ID: " + formatoA.getId());
            System.out.println("   - Título: " + formatoA.getTitle());
            System.out.println("   - Versión: " + formatoA.getNumeroVersion());
            System.out.println("   - Estado: " + formatoA.getState());
        } else {
            System.out.println("   - ❌ No encontrado");
        }
        System.out.println("📊 =================================");
    }

    /**
     * ✅ LISTAR TODAS LAS RELACIONES CON DETALLES COMPLETOS
     */
    @Transactional(readOnly = true)
    public void listarTodasLasRelaciones() {
        System.out.println("📊 LISTANDO TODAS LAS RELACIONES EXISTENTES CON DETALLES");

        List<Anteproyecto> anteproyectos = anteproyectoRepository.findAll();

        System.out.println("🔗 ===== TODAS LAS RELACIONES CON DETALLES =====");
        System.out.println("📈 Total de anteproyectos: " + anteproyectos.size());

        if (anteproyectos.isEmpty()) {
            System.out.println("   - ❌ No hay anteproyectos en el sistema");
        } else {
            anteproyectos.forEach(anteproyecto -> {
                ProyectoGrado proyecto = anteproyecto.getProyectoGrado();

                if (proyecto != null) {
                    // 🔍 BUSCAR FORMatoA VERSION PARA MOSTRAR DETALLES
                    List<FormatoAVersion> formatoAVersions = formatoAVersionRepository.findByIdFormatoA(proyecto.getIdFormatoA());
                    FormatoAVersion formatoA = formatoAVersions.isEmpty() ? null : formatoAVersions.get(0);

                    String formatoAInfo = formatoA != null ?
                            "FormatoA(ID:" + formatoA.getId() +
                                    ", Título:" + formatoA.getTitle() +
                                    ", Versión:" + formatoA.getNumeroVersion() +
                                    ", Estado:" + formatoA.getState() +
                                    ", Counter:" + formatoA.getCounter() + ")" :
                            "❌ FormatoA no encontrado para ID: " + proyecto.getIdFormatoA();

                    System.out.println("   📍 Anteproyecto: " + anteproyecto.getTitulo() +
                            " (ID:" + anteproyecto.getId() + ", Estado:" + anteproyecto.getEstado() + ")");
                    System.out.println("        └─ Proyecto: " + proyecto.getNombre() + " (ID:" + proyecto.getId() + ")");
                    System.out.println("           └─ " + formatoAInfo);
                    System.out.println();

                } else {
                    System.out.println("   📍 Anteproyecto: " + anteproyecto.getTitulo() +
                            " (ID:" + anteproyecto.getId() + ") → ❌ Sin proyecto asociado");
                }
            });
        }

        System.out.println("🔗 ============================================");
    }

    // ========== MÉTODOS DE HISTORIAL MEMENTO ==========

    public List<RequestMemento> obtenerHistorialAnteproyecto(Long anteproyectoId) {
        System.out.println("📊 CONSULTANDO HISTORIAL para Anteproyecto: " + anteproyectoId);
        List<RequestMemento> historial = historyManager.getRequestHistory("ANTEPROYECTO", anteproyectoId);
        System.out.println("📈 Historial encontrado: " + historial.size() + " versiones");
        return historial;
    }

    public RequestMemento obtenerEstadoAnteproyectoVersion(Long anteproyectoId, int version) {
        System.out.println("🔍 BUSCANDO versión " + version + " para Anteproyecto: " + anteproyectoId);
        RequestMemento memento = historyManager.restoreToRequestVersion("ANTEPROYECTO", anteproyectoId, version);
        System.out.println("✅ Versión " + version + " encontrada - Estado: " + memento.getEstado());
        return memento;
    }

    /**
     * ✅ RESTAURAR ANTEPROYECTO A VERSIÓN ANTERIOR
     */
    @Transactional
    public Anteproyecto restaurarAnteproyectoAVersion(Long anteproyectoId, int version) {
        System.out.println("⏪ RESTAURANDO Anteproyecto a versión " + version + " - ID: " + anteproyectoId);

        RequestMemento memento = historyManager.restoreToRequestVersion("ANTEPROYECTO", anteproyectoId, version);

        // Crear nuevo anteproyecto basado en el memento
        Map<String, Object> requestData = memento.getRequestData();
        AnteproyectoRequest request = convertirMapARequest(requestData);

        ProyectoGrado proyecto = proyectoGradoRepository.findById(request.idProyectoGrado())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + request.idProyectoGrado()));

        Anteproyecto anteproyectoRestaurado = convertirRequestAEntity(request, proyecto);
        anteproyectoRestaurado.setId(null); // Para que sea nueva entidad

        Anteproyecto guardado = anteproyectoRepository.save(anteproyectoRestaurado);

        // Guardar en historial como nueva versión
        RequestMemento nuevoMemento = historyManager.saveRequestState("ANTEPROYECTO",
                guardado.getId(), guardado.getEstado().name(), requestData);

        System.out.println("✅ ANTEPROYECTO RESTAURADO - Nueva ID: " + guardado.getId() +
                " | Nueva versión Memento: " + nuevoMemento.getVersion() +
                " | Estado: " + guardado.getEstado());

        return guardado;
    }

    // ========== MÉTODOS PRIVADOS DE CONVERSIÓN ==========

    /**
     * ✅ CONVERTIR REQUEST A ENTITY (Para API - puede tener ID)
     */
    private Anteproyecto convertirRequestAEntity(AnteproyectoRequest request, ProyectoGrado proyecto) {
        Anteproyecto entity = new Anteproyecto();
        entity.setTitulo(request.titulo());
        entity.setFecha(request.fecha());
        entity.setEstado(EnumEstadoAnteproyecto.valueOf(request.estado()));
        entity.setObservaciones(request.observaciones());
        entity.setProyectoGrado(proyecto);

        // ✅ Para API, permitir ID si viene (pero JPA lo ignorará si es nuevo)
        if (request.id() != null) {
            entity.setId(request.id());
        }

        return entity;
    }

    /**
     * ✅ CONVERTIR REQUEST A MAP (para Memento)
     */
    private Map<String, Object> convertirRequestAMap(AnteproyectoRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", request.id());
        map.put("titulo", request.titulo());
        map.put("fecha", request.fecha());
        map.put("estado", request.estado());
        map.put("observaciones", request.observaciones());
        map.put("idProyectoGrado", request.idProyectoGrado());
        return map;
    }

    /**
     * ✅ CREAR SNAPSHOT DE ENTIDAD
     */
    private Map<String, Object> crearSnapshotEntity(Anteproyecto anteproyecto) {
        Map<String, Object> snapshot = new HashMap<>();
        snapshot.put("id", anteproyecto.getId());
        snapshot.put("titulo", anteproyecto.getTitulo());
        snapshot.put("fecha", anteproyecto.getFecha());
        snapshot.put("estado", anteproyecto.getEstado().name());
        snapshot.put("observaciones", anteproyecto.getObservaciones());
        snapshot.put("idProyectoGrado", anteproyecto.getProyectoGrado() != null ? anteproyecto.getProyectoGrado().getId() : null);
        return snapshot;
    }

    /**
     * ✅ CONVERTIR MAP A REQUEST (para restauración)
     */
    private AnteproyectoRequest convertirMapARequest(Map<String, Object> map) {
        return new AnteproyectoRequest(
                (Long) map.get("id"),
                (String) map.get("titulo"),
                (java.time.LocalDate) map.get("fecha"),
                (String) map.get("estado"),
                (String) map.get("observaciones"),
                (Long) map.get("idProyectoGrado")
        );
    }

    /**
     * ✅ CONVERTIR ENTITY A RESPONSE
     */
    private AnteproyectoResponse convertirAResponse(Anteproyecto anteproyecto) {
        return new AnteproyectoResponse(
                anteproyecto.getId(),
                anteproyecto.getTitulo(),
                anteproyecto.getFecha(),
                anteproyecto.getEstado().name(),
                anteproyecto.getObservaciones(),
                anteproyecto.getProyectoGrado() != null ? anteproyecto.getProyectoGrado().getId() : null
        );
    }

    // ========== MÉTODOS ADICIONALES ==========

    /**
     * ✅ OBTENER TODOS LOS ANTEPROYECTOS (alias para listarTodos)
     */
    public List<AnteproyectoResponse> obtenerTodos() {
        return listarTodos();
    }

    /**
     * ✅ VERIFICAR SI EXISTE ANTEPROYECTO PARA PROYECTO
     */
    public boolean existeAnteproyectoParaProyecto(Long proyectoId) {
        List<Anteproyecto> anteproyectos = anteproyectoRepository.findAllByProyectoGradoId(proyectoId);
        return !anteproyectos.isEmpty();
    }
}