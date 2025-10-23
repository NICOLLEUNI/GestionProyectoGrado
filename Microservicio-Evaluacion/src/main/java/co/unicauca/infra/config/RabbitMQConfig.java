package co.unicauca.infra.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration

public class RabbitMQConfig {

    // 📨 Colas donde Evaluación RECIBE mensajes
    public static final String FORMATOA_QUEUE = "formatoa.queue";
    public static final String FORMATOA_EVALUADO_QUEUE = "formatoa.evaluado.queue"; // nueva cola
    public static final String ANTEPROYECTO_QUEUE = "anteproyecto.queue";
    public static final String USUARIO_QUEUE = "usuario.queue";

    /**
     * Declaración de las colas. Persistentes para que no se eliminen al reiniciar RabbitMQ.
     */
    @Bean
    public Queue formatoAQueue() {
        return new Queue(FORMATOA_QUEUE, true);
    }

    @Bean
    public Queue formatoAEvaluadoQueue() {
        return new Queue(FORMATOA_EVALUADO_QUEUE, true);
    }

    @Bean
    public Queue anteproyectoQueue() {
        return new Queue(ANTEPROYECTO_QUEUE, true);
    }

    @Bean
    public Queue usuarioQueue() {
        return new Queue(USUARIO_QUEUE, true);
    }

    /**
     * RabbitTemplate configurado con convertidor JSON.
     * Permite enviar y recibir objetos Java directamente como JSON.
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