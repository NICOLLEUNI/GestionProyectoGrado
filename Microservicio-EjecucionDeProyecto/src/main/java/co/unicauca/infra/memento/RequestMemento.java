package co.unicauca.infra.memento;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class RequestMemento {
    private final String entityType;
    private final Long entityId;
    private final String estado;
    private final int version;
    private final LocalDateTime timestamp;
    private final Map<String, Object> requestData;

    public RequestMemento(String entityType, Long entityId, String estado, int version, Map<String, Object> requestData) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.estado = estado;
        this.version = version;
        this.timestamp = LocalDateTime.now();
        this.requestData = requestData;
    }
}