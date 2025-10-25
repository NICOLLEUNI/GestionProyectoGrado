package co.unicauca.entity;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "anteproyecto")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnteproyectoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String estado;
    private LocalDate fechaCreacion;
    private String observaciones;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_id")
    private ProyectoGradoEntity proyectoGrado;
}
