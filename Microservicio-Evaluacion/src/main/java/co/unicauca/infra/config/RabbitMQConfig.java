package co.unicauca.infra.config;
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
        public static final String ANTEPROYECTO_QUEUE = "anteproyecto.queue";
        public static final String USUARIO_QUEUE = "usuario.queue";

        // 📤 Colas o routing keys donde Evaluación ENVÍA mensajes
        public static final String EVALUACION_EXCHANGE = "evaluacion.exchange";
        public static final String FORMATOA_ROUTING_KEY = "formatoa.actualizado";
        public static final String ANTEPROYECTO_ROUTING_KEY = "anteproyecto.recibido";

        /**
         * Declaración de las colas que este microservicio escuchará.
         * Son persistentes (true) para que no se eliminen al reiniciar el broker.
         */
        @Bean
        public Queue formatoAQueue() {
            return new Queue(FORMATOA_QUEUE, true);
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
         * Declaración del exchange principal.
         * Tipo "topic" permite enrutar mensajes según la routing key.
         */
        @Bean
        public TopicExchange evaluacionExchange() {
            return new TopicExchange(EVALUACION_EXCHANGE);
        }

        /**
         * Bindings: conectan las colas con el exchange usando las routing keys.
         * Esto define qué mensajes van a qué cola.
         */
        @Bean
        public Binding formatoABinding(Queue formatoAQueue, TopicExchange evaluacionExchange) {
            return BindingBuilder.bind(formatoAQueue)
                    .to(evaluacionExchange)
                    .with(FORMATOA_ROUTING_KEY);
        }

        @Bean
        public Binding anteproyectoBinding(Queue anteproyectoQueue, TopicExchange evaluacionExchange) {
            return BindingBuilder.bind(anteproyectoQueue)
                    .to(evaluacionExchange)
                    .with(ANTEPROYECTO_ROUTING_KEY);
        }

        /**
         * Configura el RabbitTemplate con un convertidor JSON.
         * Esto permite enviar/recibir objetos Java directamente como JSON.
         */
        @Bean
        public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
            RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
            rabbitTemplate.setMessageConverter(jsonMessageConverter());
            return rabbitTemplate;
        }

        @Bean
        public Jackson2JsonMessageConverter jsonMessageConverter() {
            return new Jackson2JsonMessageConverter();
        }
    }

