package co.unicauca.infra.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Colas simples
    public static final String COLA_FORMATO_A = "formatoa.queue";
    public static final String COLA_ANTEPROYECTO = "anteproyecto.queue";
    public static final String COLA_FORMATO_A_VERSION = "formatoaversion.queue";
    public static final String COLA_PROYECTO_GRADO = "proyectogrado.queue";

    // Colas
    @Bean
    public Queue formatoAQueue() {
        return new Queue(COLA_FORMATO_A, true);
    }

    @Bean
    public Queue anteproyectoQueue() {
        return new Queue(COLA_ANTEPROYECTO, true);
    }

    @Bean
    public Queue formatoAVersionQueue() {
        return new Queue(COLA_FORMATO_A_VERSION, true);
    }

    @Bean
    public Queue proyectoGradoQueue() {
        return new Queue(COLA_PROYECTO_GRADO, true);
    }

    /**
     * RabbitTemplate configurado con convertidor JSON
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    /**
     * Convertidor de mensajes JSON
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