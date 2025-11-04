package co.unicauca.infra.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // ================== COLAS (LAS MISMAS) ==================
    public static final String FORMATOA_EVALUACION_QUEUE = "formatoa.evaluacion.queue";
    public static final String FORMATOA_NOTIFICACIONES_QUEUE = "formatoa.notificaciones.queue";
    public static final String ANTEPROYECTO_EVALUACION_QUEUE = "anteproyecto.evaluacion.queue";
    public static final String ANTEPROYECTO_NOTIFICACIONES_QUEUE = "anteproyecto.notificaciones.queue";
    public static final String FORMATOAVERSION_HISTORICO_QUEUE = "formatoaversion.historico.queue";
    public static final String FORMATOAVERSION_NOTIFICACIONES_QUEUE = "formatoaversion.notificaciones.queue";
    public static final String PROYECTO_GRADO_CREADO_QUEUE = "proyectogrado.creado.queue";
    public static final String FORMATOA_EVALUADO_QUEUE = "formatoa.evaluado.queue";
    public static final String USUARIO_QUEUE = "usuario.queue";
    public static final String REPORTES_QUEUE = "reportes.queue";


    // ================== EXCHANGES (DEBEN COINCIDIR CON QUIEN ENVÍA) ==================
    public static final String ANTEPROYECTO_EXCHANGE = "anteproyecto.exchange";
    public static final String PROYECTO_GRADO_EXCHANGE = "proyectogrado.exchange";
    public static final String FORMATOA_EXCHANGE = "formatoa.exchange";
    public static final String USUARIO_EXCHANGE = "usuario.exchange";

    // ================== ROUTING KEYS (DEBEN COINCIDIR CON QUIEN ENVÍA) ==================
    public static final String ANTEPROYECTO_CREADO_ROUTING_KEY = "anteproyecto.creado";
    public static final String PROYECTO_GRADO_CREADO_ROUTING_KEY = "proyectogrado.creado";
    public static final String FORMATOA_CREADO_ROUTING_KEY = "formatoa.creado";
    public static final String USUARIO_ACTUALIZADO_ROUTING_KEY = "usuario.actualizado";

    // ================== BEANS DE EXCHANGES ==================

    @Bean
    public DirectExchange anteproyectoExchange() {
        return new DirectExchange(ANTEPROYECTO_EXCHANGE);
    }

    @Bean
    public DirectExchange proyectoGradoExchange() {
        return new DirectExchange(PROYECTO_GRADO_EXCHANGE);
    }

    @Bean
    public DirectExchange formatoAExchange() {
        return new DirectExchange(FORMATOA_EXCHANGE);
    }

    @Bean
    public DirectExchange usuarioExchange() {
        return new DirectExchange(USUARIO_EXCHANGE);
    }

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
    @Bean Queue reportesQueue() {
        return new Queue(REPORTES_QUEUE, true);
    }

    // ================== BINDINGS (LO MÁS IMPORTANTE) ==================

    @Bean
    public Binding bindingAnteproyectoEvaluacion() {
        return BindingBuilder.bind(anteproyectoEvaluacionQueue())
                .to(anteproyectoExchange())
                .with(ANTEPROYECTO_CREADO_ROUTING_KEY);
    }

    @Bean
    public Binding bindingProyectoGradoCreado() {
        return BindingBuilder.bind(proyectoGradoCreadoQueue())
                .to(proyectoGradoExchange())
                .with(PROYECTO_GRADO_CREADO_ROUTING_KEY);
    }

    @Bean
    public Binding bindingFormatoAEvaluacion() {
        return BindingBuilder.bind(formatoAEvaluacionQueue())
                .to(formatoAExchange())
                .with(FORMATOA_CREADO_ROUTING_KEY);
    }

    @Bean
    public Binding bindingUsuarioActualizado() {
        return BindingBuilder.bind(usuarioActualizadoQueue())
                .to(usuarioExchange())
                .with(USUARIO_ACTUALIZADO_ROUTING_KEY);
    }

    // ================== CONFIGURACIÓN JSON ==================

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

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }
}