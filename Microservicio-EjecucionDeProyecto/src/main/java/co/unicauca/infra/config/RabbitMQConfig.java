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
    public static final String QUEUE_SUBMISSION = "submission.proyecto.creado";
    public static final String QUEUE_APROBADO = "evaluacion.formatoa.aprobado";
    public static final String QUEUE_RECHAZADO = "evaluacion.formatoa.rechazado";

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
    public Queue queueProyectoCreado() {
        return new Queue(QUEUE_SUBMISSION, true); // durable
    }

    @Bean
    public Queue queueFormatoAprobado() {
        return new Queue(QUEUE_APROBADO, true); // durable
    }

    @Bean
    public Queue queueFormatoRechazado() {
        return new Queue(QUEUE_RECHAZADO, true); // durable
    }

    // Bindings
    @Bean
    public Binding bindingProyectoCreado(Queue queueProyectoCreado, TopicExchange proyectoExchange) {
        return BindingBuilder.bind(queueProyectoCreado).to(proyectoExchange).with(RK_PROYECTO_CREADO);
    }

    @Bean
    public Binding bindingFormatoAprobado(Queue queueFormatoAprobado, TopicExchange proyectoExchange) {
        return BindingBuilder.bind(queueFormatoAprobado).to(proyectoExchange).with(RK_FORMATO_APROBADO);
    }

    @Bean
    public Binding bindingFormatoRechazado(Queue queueFormatoRechazado, TopicExchange proyectoExchange) {
        return BindingBuilder.bind(queueFormatoRechazado).to(proyectoExchange).with(RK_FORMATO_RECHAZADO);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
