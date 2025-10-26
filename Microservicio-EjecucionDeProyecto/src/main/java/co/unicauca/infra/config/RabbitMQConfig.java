package co.unicauca.infra.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // ================== EXCHANGES (Para múltiples consumidores) ==================
    public static final String FORMATOA_CREADO_EXCHANGE = "formatoa.creado.exchange";
    public static final String ANTEPROYECTO_CREADO_EXCHANGE = "anteproyecto.creado.exchange";
    public static final String FORMATOAVERSION_CREADA_EXCHANGE = "formatoaversion.creada.exchange";

    public static final String FORMATOA_CREADO_ROUTING_KEY = "formatoa.creado";
    public static final String ANTEPROYECTO_CREADO_ROUTING_KEY = "anteproyecto.creado";
    public static final String FORMATOAVERSION_CREADA_ROUTING_KEY = "formatoaversion.creada";

    // Colas para FormatoA (diferentes consumidores)
    public static final String FORMATOA_EVALUACION_QUEUE = "formatoa.evaluacion.queue";
    public static final String FORMATOA_NOTIFICACIONES_QUEUE = "formatoa.notificaciones.queue";

    // Colas para Anteproyecto (diferentes consumidores)
    public static final String ANTEPROYECTO_EVALUACION_QUEUE = "anteproyecto.evaluacion.queue";
    public static final String ANTEPROYECTO_NOTIFICACIONES_QUEUE = "anteproyecto.notificaciones.queue";

    // Colas para FormatoAVersion (diferentes consumidores)
    public static final String FORMATOAVERSION_HISTORICO_QUEUE = "formatoaversion.historico.queue";
    public static final String FORMATOAVERSION_NOTIFICACIONES_QUEUE = "formatoaversion.notificaciones.queue";

    // ================== COLAS DIRECTAS (Para un solo consumidor) ==================
    public static final String PROYECTO_GRADO_CREADO_QUEUE = "proyectogrado.creado.queue";
    public static final String FORMATOA_EVALUADO_QUEUE = "formatoa.evaluado.queue";
    public static final String USUARIO_QUEUE = "usuario.queue";

    // ================== EXCHANGES ==================
    @Bean
    public TopicExchange formatoACreadoExchange() {
        return new TopicExchange(FORMATOA_CREADO_EXCHANGE);
    }

    @Bean
    public TopicExchange anteproyectoCreadoExchange() {
        return new TopicExchange(ANTEPROYECTO_CREADO_EXCHANGE);
    }

    @Bean
    public TopicExchange formatoAVersionCreadaExchange() {
        return new TopicExchange(FORMATOAVERSION_CREADA_EXCHANGE);
    }

    // ================== QUEUES ==================
    // Colas para FormatoA
    @Bean
    public Queue formatoAEvaluacionQueue() {
        return new Queue(FORMATOA_EVALUACION_QUEUE, true);
    }

    @Bean
    public Queue formatoANotificacionesQueue() {
        return new Queue(FORMATOA_NOTIFICACIONES_QUEUE, true);
    }

    // Colas para Anteproyecto
    @Bean
    public Queue anteproyectoEvaluacionQueue() {
        return new Queue(ANTEPROYECTO_EVALUACION_QUEUE, true);
    }

    @Bean
    public Queue anteproyectoNotificacionesQueue() {
        return new Queue(ANTEPROYECTO_NOTIFICACIONES_QUEUE, true);
    }

    // Colas para FormatoAVersion
    @Bean
    public Queue formatoAVersionHistoricoQueue() {
        return new Queue(FORMATOAVERSION_HISTORICO_QUEUE, true);
    }

    @Bean
    public Queue formatoAVersionNotificacionesQueue() {
        return new Queue(FORMATOAVERSION_NOTIFICACIONES_QUEUE, true);
    }

    // Colas directas
    @Bean
    public Queue proyectoGradoCreadoQueue() {
        return new Queue(PROYECTO_GRADO_CREADO_QUEUE, true);
    }

    @Bean
    public Queue formatoAEvaluadoQueue() {
        return new Queue(FORMATOA_EVALUADO_QUEUE, true);
    }

    @Bean
    public Queue usuarioQueue() {
        return new Queue(USUARIO_QUEUE, true);
    }

    // ================== BINDINGS ==================
    // Bindings para FormatoA
    @Bean
    public Binding bindingFormatoAEvaluacion() {
        return BindingBuilder.bind(formatoAEvaluacionQueue())
                .to(formatoACreadoExchange())
                .with(FORMATOA_CREADO_ROUTING_KEY);
    }

    @Bean
    public Binding bindingFormatoANotificaciones() {
        return BindingBuilder.bind(formatoANotificacionesQueue())
                .to(formatoACreadoExchange())
                .with(FORMATOA_CREADO_ROUTING_KEY);
    }

    // Bindings para Anteproyecto
    @Bean
    public Binding bindingAnteproyectoEvaluacion() {
        return BindingBuilder.bind(anteproyectoEvaluacionQueue())
                .to(anteproyectoCreadoExchange())
                .with(ANTEPROYECTO_CREADO_ROUTING_KEY);
    }

    @Bean
    public Binding bindingAnteproyectoNotificaciones() {
        return BindingBuilder.bind(anteproyectoNotificacionesQueue())
                .to(anteproyectoCreadoExchange())
                .with(ANTEPROYECTO_CREADO_ROUTING_KEY);
    }

    // Bindings para FormatoAVersion
    @Bean
    public Binding bindingFormatoAVersionHistorico() {
        return BindingBuilder.bind(formatoAVersionHistoricoQueue())
                .to(formatoAVersionCreadaExchange())
                .with(FORMATOAVERSION_CREADA_ROUTING_KEY);
    }

    @Bean
    public Binding bindingFormatoAVersionNotificaciones() {
        return BindingBuilder.bind(formatoAVersionNotificacionesQueue())
                .to(formatoAVersionCreadaExchange())
                .with(FORMATOAVERSION_CREADA_ROUTING_KEY);
    }

    /**
     * RabbitTemplate configurado con un convertidor JSON.
     * Permite enviar y recibir objetos Java como JSON.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    /**
     * Configura el convertidor de mensajes JSON para RabbitMQ.
     */
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ParameterNamesModule());
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}