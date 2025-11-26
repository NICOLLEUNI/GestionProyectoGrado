package co.unicauca.infra.dto.notificacion;

import java.util.List;

public record AnteproyectoResponseNotificacion(
    Long id,
    String titulo,
    String emailEvaluador1,
    String emailEvaluador2
) {
}
