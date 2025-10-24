package co.unicauca.entity;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "anteproyecto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AnteproyectoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private LocalDate fecha;

    private String estado;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    // Relación: muchos anteproyectos pueden estar ligados a un proyecto de grado (histórico o iteraciones)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_grado_id")
    private ProyectoGradoEntity proyectoGrado;
}
