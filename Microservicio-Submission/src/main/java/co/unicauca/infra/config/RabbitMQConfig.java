package co.unicauca.infra.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig{

    // ================== EXCHANGES (Para múltiples consumidores) ==================
    public static final String FORMATOA_CREADO_EXCHANGE = "formatoa.creado.exchange";
    public static final String ANTEPROYECTO_CREADO_EXCHANGE = "anteproyecto.creado.exchange";
    public static final String FORMATOAVERSION_CREADA_EXCHANGE = "formatoaversion.creada.exchange"; // ← NUEVO EXCHANGE

    public static final String FORMATOA_CREADO_ROUTING_KEY = "formatoa.creado";
    public static final String ANTEPROYECTO_CREADO_ROUTING_KEY = "anteproyecto.creado";
    public static final String FORMATOAVERSION_CREADA_ROUTING_KEY = "formatoaversion.creada"; // ← NUEVO ROUTING KEY

    // Colas para FormatoA (diferentes consumidores)
    public static final String FORMATOA_EVALUACION_QUEUE = "formatoa.evaluacion.queue";
    public static final String FORMATOA_NOTIFICACIONES_QUEUE = "formatoa.notificaciones.queue";
    public static final String ANTEPROYECTO_EJECUCION_QUEUE = "anteproyecto.ejecucion.queue";


    // Colas para Anteproyecto (diferentes consumidores)
    public static final String ANTEPROYECTO_EVALUACION_QUEUE = "anteproyecto.evaluacion.queue";
    public static final String ANTEPROYECTO_NOTIFICACIONES_QUEUE = "anteproyecto.notificaciones.queue";

    // Colas para FormatoAVersion (diferentes consumidores) ← NUEVAS COLAS
    public static final String FORMATOAVERSION_HISTORICO_QUEUE = "formatoaversion.historico.queue";
    public static final String FORMATOAVERSION_NOTIFICACIONES_QUEUE = "formatoaversion.notificaciones.queue";

    // ================== COLAS DIRECTAS (Para un solo consumidor) ==================
    public static final String PROYECTO_GRADO_CREADO_QUEUE = "proyectogrado.creado.queue";
    public static final String FORMATOA_EVALUADO_QUEUE = "formatoa.evaluado.queue";
    public static final String USUARIO_QUEUE = "usuario.queue";
    // ================== EXCHANGES Y BINDINGS ==================

    // ================== EVENTO ELIMINACIÓN ==================
    public static final String FORMATOA_ELIMINADO_EXCHANGE = "formatoa.eliminado.exchange";
    public static final String FORMATOA_ELIMINADO_ROUTING_KEY = "formatoa.eliminado";
    public static final String FORMATOA_ELIMINADO_QUEUE = "formatoa.eliminado.queue";

    // ================== ELIMINACIÓN DE PROYECTO ==================
    public static final String PROYECTO_ELIMINADO_EXCHANGE = "proyecto.eliminado.exchange";
    public static final String PROYECTO_ELIMINADO_ROUTING_KEY = "proyecto.eliminado";
    public static final String PROYECTO_ELIMINADO_QUEUE = "proyecto.eliminado.queue";

    // Exchange y colas para FormatoA
    @Bean
    public DirectExchange formatoACreadoExchange() {
        return new DirectExchange(FORMATOA_CREADO_EXCHANGE);
    }

    @Bean
    public Queue formatoAEvaluacionQueue() {
        return new Queue(FORMATOA_EVALUACION_QUEUE, true);
    }

    @Bean
    public Queue formatoANotificacionesQueue() {
        return new Queue(FORMATOA_NOTIFICACIONES_QUEUE, true);
    }

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

    // Exchange y colas para Anteproyecto
    @Bean
    public DirectExchange anteproyectoCreadoExchange() {
        return new DirectExchange(ANTEPROYECTO_CREADO_EXCHANGE);
    }

    @Bean
    public Queue anteproyectoEvaluacionQueue() {
        return new Queue(ANTEPROYECTO_EVALUACION_QUEUE, true);
    }

    @Bean
    public Queue anteproyectoEjecucionQueue() {
        return new Queue(ANTEPROYECTO_EJECUCION_QUEUE, true);
    }


    @Bean
    public Queue anteproyectoNotificacionesQueue() {
        return new Queue(ANTEPROYECTO_NOTIFICACIONES_QUEUE, true);
    }

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

    @Bean
    public Binding bindingAnteproyectoEjecucion() {
        return BindingBuilder.bind(anteproyectoEjecucionQueue())
                .to(anteproyectoCreadoExchange())
                .with(ANTEPROYECTO_CREADO_ROUTING_KEY);
    }

    // Exchange y colas para FormatoAVersion ← NUEVO
    @Bean
    public DirectExchange formatoAVersionCreadaExchange() {
        return new DirectExchange(FORMATOAVERSION_CREADA_EXCHANGE);
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

    @Bean
    public DirectExchange formatoAEliminadoExchange() {
        return new DirectExchange(FORMATOA_ELIMINADO_EXCHANGE);
    }

    @Bean
    public Queue formatoAEliminadoQueue() {
        return new Queue(FORMATOA_ELIMINADO_QUEUE, true);
    }

    @Bean
    public Binding bindingFormatoAEliminado() {
        return BindingBuilder.bind(formatoAEliminadoQueue())
                .to(formatoAEliminadoExchange())
                .with(FORMATOA_ELIMINADO_ROUTING_KEY);
    }

    // Exchange y cola para eliminación de Proyecto
    @Bean
    public DirectExchange proyectoEliminadoExchange() {
        return new DirectExchange(PROYECTO_ELIMINADO_EXCHANGE);
    }

    @Bean
    public Queue proyectoEliminadoQueue() {
        return new Queue(PROYECTO_ELIMINADO_QUEUE, true);
    }

    @Bean
    public Binding bindingProyectoEliminado() {
        return BindingBuilder.bind(proyectoEliminadoQueue())
                .to(proyectoEliminadoExchange())
                .with(PROYECTO_ELIMINADO_ROUTING_KEY);
    }

    // ================== COLAS DIRECTAS ==================

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
        return new Queue(USUARIO_QUEUE, true); // ← COLA DE USUARIO
    }

    /**
     * Configuración para enviar/recibir objetos como JSON
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ParameterNamesModule());
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}





