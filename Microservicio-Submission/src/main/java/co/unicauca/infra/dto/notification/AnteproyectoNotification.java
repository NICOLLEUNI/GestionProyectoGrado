package co.unicauca.infra.dto.notification;

public record AnteproyectoNotification(
        Long anteproyectoId,
        String titulo,
        String directorEmail // ← Para buscar departamento del docente
) {}
