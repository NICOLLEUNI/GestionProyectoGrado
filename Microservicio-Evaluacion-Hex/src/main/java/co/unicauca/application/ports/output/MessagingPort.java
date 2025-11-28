package co.unicauca.application.ports.output;

public interface MessagingPort {
    void publishFormatoA(Object formatoAResponse);
    void publishFormatoAEvaluado(Object formatoAResponse);
    void publishFormatoAEvaluadoNotificacion(Object formatoAResponseNotificacion);

    void publishAnteproyectoAsignacion(Object anteproyectoResponseNotificacion);
}
