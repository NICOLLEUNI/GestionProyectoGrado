package co.unicauca.infrastructure.dto.notification;

public record AnteproyectoResponseNotification(
        Long id,
        String titulo,
        String emailEvaluador1,
        String emailEvaluador2
) {
}
