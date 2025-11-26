package co.unicauca.infra.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    // El nombre de la cola debe coincidir con el del microservicio Submission
    public static final String FORMATOA_EVALUADO_NOTIFICATION_QUEUE = "formatoa.evaluado.notification.queue";
    public static final String FORMATOA_NOTIFICACIONES_QUEUE = "formatoa.notificaciones.queue";
    public static final String ANTEPROYECTO_NOTIFICACIONES_QUEUE = "anteproyecto.notificaciones.queue";
    public static final String NOTIFICACION_QUEUE = "notificacion.queue";
    public static final String FORMATOAVERSION_NOTIFICACIONES_QUEUE = "formatoaversion.notificaciones.queue";
    public static final String ANTEPROYECTO_ASIGNACION_QUEUE = "anteproyecto.asignacion.queue";

    // Declara la cola para que Spring pueda escucharla
    @Bean
    public Queue FormatoEvaluadoQueue() {
        return new Queue(FORMATOA_EVALUADO_NOTIFICATION_QUEUE, true);
    }
    @Bean
    public Queue FormatoCreadoQueue() {
        return new Queue(FORMATOA_NOTIFICACIONES_QUEUE, true);
    }
    @Bean
    public Queue AnteproyectoCreadoQueue() {
        return new Queue( ANTEPROYECTO_NOTIFICACIONES_QUEUE, true);
    }
    @Bean
    public Queue FormatoAVersionQueue() {
        return new Queue( FORMATOAVERSION_NOTIFICACIONES_QUEUE, true);
    }
    @Bean
    public Queue usuarioQueue() {
        return new Queue(NOTIFICACION_QUEUE, true);
    }
    @Bean
    public Queue anteproyectoAsignacionQueue() {
        return new Queue(ANTEPROYECTO_ASIGNACION_QUEUE, true);
    }

    // Convierte automáticamente los mensajes JSON a objetos Java (y viceversa)
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
