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

    // 📨 Definición de las colas para los proyectos.
    public static final String FORMATOA_QUEUE = "formatoa.queue";
    public static final String FORMATOA_EVALUADO_QUEUE = "formatoa.evaluado.queue"; // Nueva cola para evaluaciones
    public static final String ANTEPROYECTO_QUEUE = "anteproyecto.queue";
    public static final String USUARIO_QUEUE = "usuario.queue";

    /**
     * Declaración de las colas con persistencia.
     * Las colas no se eliminarán automáticamente al reiniciar RabbitMQ.
     */
    @Bean
    public Queue formatoAQueue() {
        return new Queue(FORMATOA_QUEUE, true); // Cola persistente
    }

    @Bean
    public Queue formatoAEvaluadoQueue() {
        return new Queue(FORMATOA_EVALUADO_QUEUE, true); // Cola persistente
    }

    @Bean
    public Queue anteproyectoQueue() {
        return new Queue(ANTEPROYECTO_QUEUE, true); // Cola persistente
    }

    @Bean
    public Queue usuarioQueue() {
        return new Queue(USUARIO_QUEUE, true); // Cola persistente
    }

    /**
     * RabbitTemplate configurado con un convertidor JSON.
     * Permite enviar y recibir objetos Java como JSON.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter()); // Establece el convertidor JSON
        return rabbitTemplate;
    }

    /**
     * Configura el convertidor de mensajes JSON para RabbitMQ.
     * Utiliza un `ObjectMapper` con varios módulos para soportar tipos de datos específicos.
     */
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new ParameterNamesModule()); // Módulo para el soporte de nombres de parámetros
        objectMapper.registerModule(new Jdk8Module()); // Módulo para tipos de Java 8
        objectMapper.registerModule(new JavaTimeModule()); // Módulo para soporte de Java 8 (fechas y horas)
        return new Jackson2JsonMessageConverter(objectMapper); // Convertidor JSON
    }
}
