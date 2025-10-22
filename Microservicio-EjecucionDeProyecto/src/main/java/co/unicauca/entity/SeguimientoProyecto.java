package co.unicauca.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "seguimiento_proyecto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SeguimientoProyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK interno del microservicio

    @Column(nullable = false, unique = true)
    private Long idProyecto; // ID proveniente de Submission

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String modalidad; // Práctica Profesional o Investigación

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoProyecto estado; // PENDIENTE, EN_EJECUCION, RECHAZADO, FINALIZADO

    private Long idEstudiante;
    private Long idDirector;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

}

