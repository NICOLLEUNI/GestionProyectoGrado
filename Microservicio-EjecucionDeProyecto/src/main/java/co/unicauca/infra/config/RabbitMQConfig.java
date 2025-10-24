package co.unicauca.infra.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {


    // Exchanges
    public static final String EXCHANGE_PROYECTO = "proyecto.events";
    public static final String EXCHANGE_EJECUCION = "ejecucion.events";

    // Queues
    public static final String QUEUE_SUBMISSION = "submission.queue";
    public static final String QUEUE_PROYECTO_EJECUCION = "q.proyecto.ejecucion";
    public static final String QUEUE_PERSONAS_EJECUCION = "q.personas.ejecucion";
    public static final String QUEUE_FORMATOA_VERSION_CREATED = "q.formatoa.version.created";
    public static final String QUEUE_FORMATOA_CREATED = "q.formatoa.created";
    public static final String QUEUE_ANTEPROYECTO_CREATED = "q.anteproyecto.created";

    // Routing keys
    public static final String RK_PROYECTO_CREADO = "submission.proyecto.creado";
    public static final String RK_FORMATO_APROBADO = "evaluacion.formatoa.aprobado";
    public static final String RK_FORMATO_RECHAZADO = "evaluacion.formatoa.rechazado";

    // Exchanges
    @Bean
    public TopicExchange proyectoExchange() {
        return new TopicExchange(EXCHANGE_PROYECTO);
    }

    @Bean
    public TopicExchange ejecucionExchange() {
        return new TopicExchange(EXCHANGE_EJECUCION);
    }

    // Queues
    @Bean
    public Queue queueSubmission() {
        return new Queue(QUEUE_SUBMISSION, true);
    }

    @Bean
    public Queue queueProyectoEjecucion() {
        return new Queue(QUEUE_PROYECTO_EJECUCION, true);
    }

    @Bean
    public Queue queuePersonasEjecucion() {
        return new Queue(QUEUE_PERSONAS_EJECUCION, true);
    }

    @Bean
    public Queue queueFormatoAVersionCreated() {
        return new Queue(QUEUE_FORMATOA_VERSION_CREATED, true);
    }

    @Bean
    public Queue queueFormatoACreated() {
        return new Queue(QUEUE_FORMATOA_CREATED, true);
    }

    @Bean
    public Queue queueAnteproyectoCreated() {
        return new Queue(QUEUE_ANTEPROYECTO_CREATED, true);
    }

    // Bindings
    @Bean
    public Binding bindingSubmission(Queue queueSubmission, TopicExchange proyectoExchange) {
        return BindingBuilder.bind(queueSubmission).to(proyectoExchange).with(RK_PROYECTO_CREADO);
    }

    @Bean
    public Binding bindingFormatoAprobado(Queue queueFormatoACreated, TopicExchange proyectoExchange) {
        return BindingBuilder.bind(queueFormatoACreated).to(proyectoExchange).with(RK_FORMATO_APROBADO);
    }

    @Bean
    public Binding bindingFormatoRechazado(Queue queueFormatoACreated, TopicExchange proyectoExchange) {
        return BindingBuilder.bind(queueFormatoACreated).to(proyectoExchange).with(RK_FORMATO_RECHAZADO);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Binding bindingProyectoEjecucion(Queue queueProyectoEjecucion, TopicExchange proyectoExchange) {
        return BindingBuilder.bind(queueProyectoEjecucion)
                .to(proyectoExchange)
                .with("submission.proyecto.creado");
    }
}
