package co.unicauca.infra.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig{

    // ================== COLAS ==================

    // Colas para FormatoA
    public static final String FORMATOA_EVALUACION_QUEUE = "formatoa.evaluacion.queue";
    public static final String FORMATOA_NOTIFICACIONES_QUEUE = "formatoa.notificaciones.queue";

    // Colas para Anteproyecto
    public static final String ANTEPROYECTO_EVALUACION_QUEUE = "anteproyecto.evaluacion.queue";
    public static final String ANTEPROYECTO_NOTIFICACIONES_QUEUE = "anteproyecto.notificaciones.queue";

    // Colas para FormatoAVersion
    public static final String FORMATOAVERSION_HISTORICO_QUEUE = "formatoaversion.historico.queue";
    public static final String FORMATOAVERSION_NOTIFICACIONES_QUEUE = "formatoaversion.notificaciones.queue";

    // Colas directas
    public static final String PROYECTO_GRADO_CREADO_QUEUE = "proyectogrado.creado.queue";
    public static final String FORMATOA_EVALUADO_QUEUE = "formatoa.evaluado.queue";
    public static final String USUARIO_QUEUE = "usuario.queue";

    // ================== BEANS DE COLAS ==================

    @Bean
    public Queue formatoAEvaluacionQueue() {
        return new Queue(FORMATOA_EVALUACION_QUEUE, true);
    }

    @Bean
    public Queue formatoANotificacionesQueue() {
        return new Queue(FORMATOA_NOTIFICACIONES_QUEUE, true);
    }

    @Bean
    public Queue anteproyectoEvaluacionQueue() {
        return new Queue(ANTEPROYECTO_EVALUACION_QUEUE, true);
    }

    @Bean
    public Queue anteproyectoNotificacionesQueue() {
        return new Queue(ANTEPROYECTO_NOTIFICACIONES_QUEUE, true);
    }

    @Bean
    public Queue formatoAVersionHistoricoQueue() {
        return new Queue(FORMATOAVERSION_HISTORICO_QUEUE, true);
    }

    @Bean
    public Queue formatoAVersionNotificacionesQueue() {
        return new Queue(FORMATOAVERSION_NOTIFICACIONES_QUEUE, true);
    }

    @Bean
    public Queue proyectoGradoCreadoQueue() {
        return new Queue(PROYECTO_GRADO_CREADO_QUEUE, true);
    }

    @Bean
    public Queue formatoAEvaluadoQueue() {
        return new Queue(FORMATOA_EVALUADO_QUEUE, true);
    }

    @Bean
    public Queue usuarioActualizadoQueue() {
        return new Queue(USUARIO_QUEUE, true);
    }

    /**
     * Configuración para convertir mensajes JSON a objetos Java
     */
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    /**
     * Configura el MessageConverter para los listeners
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }
}