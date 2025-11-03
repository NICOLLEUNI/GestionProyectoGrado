package co.unicauca.service;

import co.unicauca.entity.ProyectoGrado;
import co.unicauca.infra.dto.ProyectoGradoRequest;
import co.unicauca.infra.dto.ProyectoGradoResponse;
import co.unicauca.infra.memento.RequestHistoryManager;
import co.unicauca.infra.memento.RequestMemento;
import co.unicauca.repository.ProyectoGradoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProyectoGradoService {

    private final ProyectoGradoRepository proyectoRepository;
    private final RequestHistoryManager historyManager;

    /**
     * ✅ PROCESAR REQUEST DESDE LISTENER - CON MANEJO DE CONCURRENCIA
     */
    @Transactional
    public void procesarProyectoRequest(ProyectoGradoRequest request) {
        System.out.println("🔄 [SERVICE] Procesando Request: " + request.nombre() +
                " | ID recibido: " + request.id() +
                " | FormatoA: " + request.IdFormatoA());

        try {
            ProyectoGrado proyectoExistente = null;
            String accion = "";


            // 🔍 ESTRATEGIA 2: Buscar por FormatoA si no se encontró por ID
            if (proyectoExistente == null && request.IdFormatoA() != null) {
                Optional<ProyectoGrado> proyectoOpt = proyectoRepository.findByIdFormatoA(request.IdFormatoA());
                if (proyectoOpt.isPresent()) {
                    proyectoExistente = proyectoOpt.get();
                    accion = "";
                    System.out.println("🔍 Proyecto encontrado por FormatoA: " + request.IdFormatoA());
                }
            }

            // 🎯 EJECUTAR ACCIÓN
            if (proyectoExistente != null) {
                System.out.println("🔄 " + accion + " - ID: " + proyectoExistente.getId());
                actualizarProyectoExistente(proyectoExistente, request);
            } else {
                System.out.println("");
                crearNuevoProyectoSinId(request);
            }

            System.out.println("✅ [SERVICE] Request procesado exitosamente: " + request.nombre());

        } catch (Exception e) {
            System.out.println("❌ [SERVICE] Error procesando Request: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error procesando proyecto request", e);
        }
    }

    /**
     * ✅ CREAR NUEVO PROYECTO SIN ASIGNAR ID MANUAL (Para Listener)
     */
    private void crearNuevoProyectoSinId(ProyectoGradoRequest request) {
        System.out.println("PROYECTO - ID request: " + request.id());

        // ✅ Crear nueva entidad SIN asignar ID manualmente
        ProyectoGrado nuevoProyecto = new ProyectoGrado();
        nuevoProyecto.setNombre(request.nombre());
        nuevoProyecto.setFecha(request.fecha().atStartOfDay());
        nuevoProyecto.setEstudiantesEmail(request.estudiantesEmail());
        nuevoProyecto.setEstado("ENTREGADO");
        nuevoProyecto.setIdFormatoA(request.IdFormatoA());

        // ❌ NO asignar ID manualmente - dejar que JPA lo genere automáticamente
        ProyectoGrado guardado = proyectoRepository.save(nuevoProyecto);

        // 💾 GUARDAR EN MEMENTO
        Map<String, Object> requestData = convertirRequestAMap(request);
        RequestMemento memento = historyManager.saveRequestState("PROYECTO_GRADO",
                guardado.getId(), guardado.getEstado(), requestData);

        System.out.println("✅ PROYECTO CREADO - ID: " + guardado.getId() +
                " | Versión Memento: " + memento.getVersion() +
                " | FormatoA: " + guardado.getIdFormatoA());
    }

    /**
     * ✅ ACTUALIZAR PROYECTO EXISTENTE CON MEMENTO
     */
    private void actualizarProyectoExistente(ProyectoGrado proyectoExistente, ProyectoGradoRequest request) {
        // 💾 GUARDAR ESTADO ACTUAL EN MEMENTO
        Map<String, Object> estadoAnterior = crearSnapshotEntity(proyectoExistente);
        RequestMemento mementoAnterior = historyManager.saveRequestState("PROYECTO_GRADO",
                proyectoExistente.getId(), proyectoExistente.getEstado(), estadoAnterior);

        System.out.println("💾 Estado anterior guardado - Versión: " + mementoAnterior.getVersion());

        // ✏️ ACTUALIZAR CAMPOS
        proyectoExistente.setNombre(request.nombre());
        proyectoExistente.setEstudiantesEmail(request.estudiantesEmail());
        proyectoExistente.setIdFormatoA(request.IdFormatoA());

        ProyectoGrado actualizado = proyectoRepository.save(proyectoExistente);

        // 💾 GUARDAR NUEVO ESTADO EN MEMENTO
        Map<String, Object> requestData = convertirRequestAMap(request);
        RequestMemento mementoNuevo = historyManager.saveRequestState("PROYECTO_GRADO",
                actualizado.getId(), actualizado.getEstado(), requestData);

        System.out.println("✅ PROYECTO ACTUALIZADO - ID: " + actualizado.getId() +
                " | Versión Memento: " + mementoNuevo.getVersion() +
                " | Cambios: " + (mementoNuevo.getVersion() - mementoAnterior.getVersion()));
    }

    // ========== MÉTODOS PÚBLICOS PARA API ==========

    /**
     * ✅ CREAR PROYECTO DESDE API CON MEMENTO
     */
    @Transactional
    public ProyectoGradoResponse crearProyecto(ProyectoGradoRequest request) {
        System.out.println("🎓 CREAR PROYECTO desde API: " + request.nombre());

        ProyectoGrado proyecto = convertirRequestAEntity(request);
        ProyectoGrado guardado = proyectoRepository.save(proyecto);

        // 💾 GUARDAR EN MEMENTO
        Map<String, Object> requestData = convertirRequestAMap(request);
        RequestMemento memento = historyManager.saveRequestState("PROYECTO_GRADO",
                guardado.getId(), "ENTREGADO", requestData);

        ProyectoGradoResponse response = convertirAResponse(guardado);

        System.out.println("✅ PROYECTO CREADO desde API - ID: " + response.id() +
                " | Versión Memento: " + memento.getVersion());

        return response;
    }

    /**
     * ✅ CREAR PROYECTO INTERNO CON MEMENTO
     */
    @Transactional
    public ProyectoGradoResponse crearProyectoInterno(ProyectoGradoRequest request) {
        System.out.println("🔄 CREAR PROYECTO INTERNO: " + request.nombre());

        ProyectoGrado proyecto = convertirRequestAEntity(request);
        ProyectoGrado guardado = proyectoRepository.save(proyecto);

        // 💾 GUARDAR EN MEMENTO
        Map<String, Object> requestData = convertirRequestAMap(request);
        historyManager.saveRequestState("PROYECTO_GRADO", guardado.getId(), "ENTREGADO", requestData);

        System.out.println("✅ PROYECTO INTERNO CREADO - ID: " + guardado.getId());

        return convertirAResponse(guardado);
    }

    /**
     * ✅ ACTUALIZAR PROYECTO CON MEMENTO
     */
    @Transactional
    public ProyectoGradoResponse actualizarProyecto(Long id, ProyectoGradoRequest request) {
        System.out.println("✏️ ACTUALIZAR PROYECTO: " + id);

        ProyectoGrado proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + id));

        // 💾 GUARDAR ESTADO ACTUAL EN MEMENTO
        Map<String, Object> estadoAnterior = crearSnapshotEntity(proyecto);
        historyManager.saveRequestState("PROYECTO_GRADO", proyecto.getId(),
                proyecto.getEstado(), estadoAnterior);

        proyecto.setNombre(request.nombre());
        proyecto.setEstudiantesEmail(request.estudiantesEmail());
        proyecto.setIdFormatoA(request.IdFormatoA());

        ProyectoGrado actualizado = proyectoRepository.save(proyecto);

        // 💾 GUARDAR NUEVO ESTADO EN MEMENTO
        Map<String, Object> requestData = convertirRequestAMap(request);
        RequestMemento memento = historyManager.saveRequestState("PROYECTO_GRADO",
                actualizado.getId(), actualizado.getEstado(), requestData);

        ProyectoGradoResponse response = convertirAResponse(actualizado);

        System.out.println("✅ PROYECTO ACTUALIZADO - ID: " + id +
                " | Versión Memento: " + memento.getVersion());

        return response;
    }

    // ========== MÉTODOS DE CONSULTA ==========

    @Transactional(readOnly = true)
    public ProyectoGradoResponse buscarPorId(Long id) {
        System.out.println("🔍 BUSCANDO proyecto por ID: " + id);

        ProyectoGrado proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + id));

        System.out.println("✅ PROYECTO ENCONTRADO: " + proyecto.getId() + " - " + proyecto.getNombre());
        return convertirAResponse(proyecto);
    }

    @Transactional(readOnly = true)
    public List<ProyectoGradoResponse> listarTodos() {
        System.out.println("📋 LISTANDO todos los proyectos");

        List<ProyectoGrado> proyectos = proyectoRepository.findAll();
        List<ProyectoGradoResponse> responses = proyectos.stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());

        System.out.println("✅ Proyectos encontrados: " + responses.size());
        return responses;
    }

    @Transactional(readOnly = true)
    public Optional<ProyectoGrado> buscarPorFormatoAExternoId(Long idFormatoA) {
        System.out.println("🔍 BUSCANDO proyecto por FormatoA: " + idFormatoA);
        return proyectoRepository.findByIdFormatoA(idFormatoA);
    }

    // ========== MÉTODOS DE HISTORIAL MEMENTO ==========

    public List<RequestMemento> obtenerHistorialProyecto(Long proyectoId) {
        System.out.println("📊 CONSULTANDO HISTORIAL para Proyecto: " + proyectoId);
        List<RequestMemento> historial = historyManager.getRequestHistory("PROYECTO_GRADO", proyectoId);
        System.out.println("📈 Historial encontrado: " + historial.size() + " versiones");
        return historial;
    }

    public RequestMemento obtenerEstadoProyectoVersion(Long proyectoId, int version) {
        System.out.println("🔍 BUSCANDO versión " + version + " para Proyecto: " + proyectoId);
        RequestMemento memento = historyManager.restoreToRequestVersion("PROYECTO_GRADO", proyectoId, version);
        System.out.println("✅ Versión " + version + " encontrada - Estado: " + memento.getEstado());
        return memento;
    }

    // ========== MÉTODOS PRIVADOS DE CONVERSIÓN ==========

    /**
     * ✅ CONVERTIR REQUEST A ENTITY (Para API - puede tener ID)
     */
    private ProyectoGrado convertirRequestAEntity(ProyectoGradoRequest request) {
        ProyectoGrado entity = new ProyectoGrado();
        entity.setNombre(request.nombre());
        entity.setFecha(request.fecha().atStartOfDay());
        entity.setEstudiantesEmail(request.estudiantesEmail());
        entity.setEstado("ENTREGADO");
        entity.setIdFormatoA(request.IdFormatoA());

        // ✅ Para API, permitir ID si viene (pero JPA lo ignorará si es nuevo)
        if (request.id() != null) {
            entity.setId(request.id());
        }

        return entity;
    }

    /**
     * ✅ CONVERTIR REQUEST A MAP (para Memento)
     */
    private Map<String, Object> convertirRequestAMap(ProyectoGradoRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", request.id());
        map.put("nombre", request.nombre());
        map.put("fecha", request.fecha());
        map.put("estudiantesEmail", request.estudiantesEmail());
        map.put("IdFormatoA", request.IdFormatoA());
        map.put("estado", "ENTREGADO");
        return map;
    }

    /**
     * ✅ CREAR SNAPSHOT DE ENTIDAD
     */
    private Map<String, Object> crearSnapshotEntity(ProyectoGrado proyecto) {
        Map<String, Object> snapshot = new HashMap<>();
        snapshot.put("id", proyecto.getId());
        snapshot.put("nombre", proyecto.getNombre());
        snapshot.put("fecha", proyecto.getFecha().toLocalDate());
        snapshot.put("estudiantesEmail", proyecto.getEstudiantesEmail());
        snapshot.put("IdFormatoA", proyecto.getIdFormatoA());
        snapshot.put("estado", proyecto.getEstado());
        return snapshot;
    }

    /**
     * ✅ CONVERTIR ENTITY A RESPONSE
     */
    private ProyectoGradoResponse convertirAResponse(ProyectoGrado proyecto) {
        return new ProyectoGradoResponse(
                proyecto.getId(),
                proyecto.getNombre(),
                proyecto.getFecha().toLocalDate(),
                proyecto.getEstudiantesEmail(),
                proyecto.getIdFormatoA()
        );
    }

    // ========== MÉTODOS ADICIONALES (SI LOS NECESITAS) ==========

    /**
     * ✅ SINCRONIZAR FORMATA
     */
    @Transactional
    public void sincronizarFormatoA(Long proyectoId, Long idFormatoAExterno) {
        System.out.println("🔄 SINCRONIZANDO FORMATA para proyecto: " + proyectoId);

        ProyectoGrado proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado con ID: " + proyectoId));

        // 💾 GUARDAR ESTADO ACTUAL EN MEMENTO
        Map<String, Object> estadoAnterior = crearSnapshotEntity(proyecto);
        historyManager.saveRequestState("PROYECTO_GRADO", proyecto.getId(),
                proyecto.getEstado(), estadoAnterior);

        proyecto.setIdFormatoA(idFormatoAExterno);
        proyectoRepository.save(proyecto);

        // 💾 GUARDAR NUEVO ESTADO EN MEMENTO
        Map<String, Object> nuevoEstado = crearSnapshotEntity(proyecto);
        RequestMemento memento = historyManager.saveRequestState("PROYECTO_GRADO",
                proyecto.getId(), proyecto.getEstado(), nuevoEstado);

        System.out.println("✅ FORMATA SINCRONIZADO - Proyecto " + proyectoId + " → FormatoA " + idFormatoAExterno +
                " | Versión Memento: " + memento.getVersion());
    }

    /**
     * ✅ OBTENER PROYECTOS CON RELACIONES
     */
    @Transactional(readOnly = true)
    public List<ProyectoGradoResponse> obtenerTodosConRelaciones() {
        System.out.println("🔗 OBTENIENDO proyectos con relaciones");

        // Si tienes un método específico en el repository
        List<ProyectoGrado> proyectos = proyectoRepository.findAll();

        List<ProyectoGradoResponse> responses = proyectos.stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());

        System.out.println("✅ Proyectos con relaciones encontrados: " + responses.size());
        return responses;
    }

    /**
     * ✅ BUSCAR VERSIONES POR PROYECTO
     */
    @Transactional(readOnly = true)
    public List<ProyectoGradoResponse> buscarVersionesPorProyecto(Long proyectoId) {
        System.out.println("📋 BUSCANDO versiones para proyecto: " + proyectoId);

        // Implementar según tu lógica de versiones
        // Por ahora retornar lista vacía o buscar por FormatoA asociado
        return List.of();
    }

    /**
     * ✅ RESTAURAR PROYECTO A VERSIÓN ANTERIOR
     */
    @Transactional
    public ProyectoGrado restaurarProyectoAVersion(Long proyectoId, int version) {
        System.out.println("⏪ RESTAURANDO Proyecto a versión " + version + " - ID: " + proyectoId);

        RequestMemento memento = historyManager.restoreToRequestVersion("PROYECTO_GRADO", proyectoId, version);

        // Crear nuevo proyecto basado en el memento
        Map<String, Object> requestData = memento.getRequestData();
        ProyectoGradoRequest request = convertirMapARequest(requestData);

        ProyectoGrado proyectoRestaurado = convertirRequestAEntity(request);
        proyectoRestaurado.setId(null); // Para que sea nueva entidad

        ProyectoGrado guardado = proyectoRepository.save(proyectoRestaurado);

        // Guardar en historial como nueva versión
        RequestMemento nuevoMemento = historyManager.saveRequestState("PROYECTO_GRADO",
                guardado.getId(), guardado.getEstado(), requestData);

        System.out.println("✅ PROYECTO RESTAURADO - Nueva ID: " + guardado.getId() +
                " | Nueva versión Memento: " + nuevoMemento.getVersion());

        return guardado;
    }

    /**
     * ✅ CONVERTIR MAP A REQUEST (para restauración)
     */
    private ProyectoGradoRequest convertirMapARequest(Map<String, Object> map) {
        return new ProyectoGradoRequest(
                (Long) map.get("id"),
                (String) map.get("nombre"),
                (java.time.LocalDate) map.get("fecha"),
                (List<String>) map.get("estudiantesEmail"),
                (Long) map.get("IdFormatoA")
        );
    }

    @Transactional(readOnly = true)
    public ProyectoGradoResponse buscarPorEmailEstudiante(String email) {
        System.out.println("🔍 BUSCANDO proyecto de grado para estudiante: " + email);

        ProyectoGrado proyecto = proyectoRepository.findByEstudiantesEmailContaining(email)
                .orElseThrow(() -> new RuntimeException("No se encontró proyecto para el estudiante: " + email));

        System.out.println("✅ PROYECTO ENCONTRADO - ID: " + proyecto.getId() + " | Estudiantes: " + proyecto.getEstudiantesEmail());
        return convertirAResponse(proyecto);
    }
}