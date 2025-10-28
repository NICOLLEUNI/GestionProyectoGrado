package co.unicauca.infra.memento;

import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RequestHistoryManager {
    private final Map<String, List<RequestMemento>> histories = new ConcurrentHashMap<>();

    public RequestMemento saveRequestState(String entityType, Long entityId, String estado, Map<String, Object> requestData) {
        String key = generateKey(entityType, entityId);

        List<RequestMemento> history = histories.computeIfAbsent(key, k -> new ArrayList<>());
        int newVersion = history.size() + 1;

        // ✅ Crear una COPIA del map para evitar modificaciones
        Map<String, Object> dataCopy = new HashMap<>(requestData);

        RequestMemento memento = new RequestMemento(entityType, entityId, estado, newVersion, dataCopy);
        history.add(memento);

        System.out.println("💾 MEMENTO Guardado - Tipo: " + entityType + " | ID: " + entityId +
                " | Versión: " + newVersion + " | Estado: " + estado);

        return memento;
    }

    public RequestMemento getLastRequest(String entityType, Long entityId) {
        String key = generateKey(entityType, entityId);
        List<RequestMemento> history = histories.get(key);

        if (history != null && !history.isEmpty()) {
            return history.get(history.size() - 1);
        }
        return null;
    }

    public List<RequestMemento> getRequestHistory(String entityType, Long entityId) {
        String key = generateKey(entityType, entityId);
        return histories.getOrDefault(key, Collections.emptyList());
    }

    public RequestMemento restoreToRequestVersion(String entityType, Long entityId, int version) {
        String key = generateKey(entityType, entityId);
        List<RequestMemento> history = histories.get(key);

        if (history != null) {
            return history.stream()
                    .filter(m -> m.getVersion() == version)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Version not found: " + version));
        }
        throw new IllegalArgumentException("No history found for: " + key);
    }

    private String generateKey(String entityType, Long entityId) {
        return entityType + ":" + entityId;
    }
}