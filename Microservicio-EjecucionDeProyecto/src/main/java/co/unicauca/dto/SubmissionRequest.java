package co.unicauca.dto;


import lombok.*;
import java.util.List;

/**
 * Representa el mensaje completo que envía el microservicio Submission
 * a través de RabbitMQ.
 * Este DTO se utiliza únicamente para deserializar el evento entrante.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SubmissionRequest {

    private List<PersonaRequest> personas;              // Usuarios involucrados (estudiantes, docentes, etc.)
    private ProyectoGradoRequest proyectoGrado;         // Proyecto principal
    private FormatoARequest formatoA;                   // Formato A asociado
    private List<FormatoAVersionRequest> formatoAVersiones; // Versiones del Formato A
    private AnteproyectoRequest anteproyecto;           // Anteproyecto vinculado
}

