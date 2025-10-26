package co.unicauca.infra.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.*;




@Configuration

public class RabbitMQConfig {

    // 📨 Colas donde Evaluación RECIBE mensajes
    public static final String FORMATOA_EVALUACION_QUEUE = "formatoa.evaluacion.queue";
    // 📨 Colas donde Evaluación ENVÍA mensajes al ser evaluado
    public static final String FORMATOA_EVALUADO_SUBMISSION_QUEUE = "formatoa.evaluado.submission.queue";
    public static final String FORMATOA_EVALUADO_NOTIFICATION_QUEUE = "formatoa.evaluado.notification.queue"; // nueva cola
    public static final String ANTEPROYECTO_CREADO_QUEUE = "anteproyecto.creado.queue";
    public static final String USUARIO_QUEUE = "usuario.queue";



    /**
     * Declaración de las colas. Persistentes para que no se eliminen al reiniciar RabbitMQ.
     */
    @Bean
    public Queue formatoAQueue() {
        return new Queue(FORMATOA_EVALUACION_QUEUE, true);
    }

    @Bean
    public Queue formatoAEvaluadoSubmissionQueue() {
        return new Queue(FORMATOA_EVALUADO_SUBMISSION_QUEUE, true);
    }

    @Bean
    public Queue formatoAEvaluadoNotificationQueue() {
        return new Queue(FORMATOA_EVALUADO_NOTIFICATION_QUEUE, true);
    }

    @Bean
    public Queue anteproyectoQueue() {
        return new Queue(ANTEPROYECTO_CREADO_QUEUE, true);
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