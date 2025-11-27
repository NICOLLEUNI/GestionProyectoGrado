package co.unicauca.application;

import co.unicauca.identity.config.RabbitMQConfig;
import co.unicauca.identity.entity.Persona;
import co.unicauca.identity.enums.EnumPrograma;
import co.unicauca.identity.enums.enumRol;
import co.unicauca.identity.messaging.dto.UsuarioMessage;
import co.unicauca.identity.service.EventPublisherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventPublisherServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private EventPublisherService eventPublisherService;

    @Captor
    private ArgumentCaptor<Message> messageCaptor;

    @Captor
    private ArgumentCaptor<String> queueCaptor;

    private Persona persona;

    @BeforeEach
    void setup() {
        persona = new Persona(
                "Carlos",
                "López",
                "3123456789",
                "carlos.lopez@unicauca.edu.co",
                "hashedPassword",
                EnumSet.of(enumRol.DOCENTE),
                null,
                co.unicauca.identity.enums.EnumDepartamento.SISTEMAS
        );
        persona.setIdUsuario(1L);
    }



    @Test
    void testPublishLoginSuccessEvent_Success() throws Exception {
        // Arrange
        UsuarioMessage usuarioMessage = UsuarioMessage.fromEntity(persona);
        when(objectMapper.writeValueAsBytes(any(UsuarioMessage.class))).thenReturn("{}".getBytes());

        // Act
        eventPublisherService.publishLoginSuccessEvent(persona);

        // Assert
        verify(rabbitTemplate).send(eq(RabbitMQConfig.USUARIO_QUEUE), any(Message.class));
    }

    @Test
    void testPublishLoginFailedEvent_Success() throws Exception {
        // Arrange
        when(objectMapper.writeValueAsBytes(any())).thenReturn("{}".getBytes());

        // Act
        eventPublisherService.publishLoginFailedEvent("test@unicauca.edu.co");

        // Assert
        verify(rabbitTemplate).send(eq(RabbitMQConfig.USUARIO_QUEUE), any(Message.class));
    }

    @Test
    void testIsRabbitMQAvailable_True() {
        // Arrange
        when(rabbitTemplate.execute(any())).thenReturn(null);

        // Act
        boolean result = eventPublisherService.isRabbitMQAvailable();

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsRabbitMQAvailable_False() {
        // Arrange
        when(rabbitTemplate.execute(any())).thenThrow(new RuntimeException("Connection failed"));

        // Act
        boolean result = eventPublisherService.isRabbitMQAvailable();

        // Assert
        assertFalse(result);
    }
}