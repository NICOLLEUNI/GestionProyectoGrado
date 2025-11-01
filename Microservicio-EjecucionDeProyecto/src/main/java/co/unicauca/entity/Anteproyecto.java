package co.unicauca.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "anteproyecto")
public class Anteproyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDate fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumEstadoAnteproyecto estado = EnumEstadoAnteproyecto.ENTREGADO;

    @Column(length = 1000) // Para observaciones más largas
    private String observaciones;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "proyecto_grado_id",
            unique = false,  // ← CAMBIA ESTO A false
            foreignKey = @ForeignKey(name = "fk_anteproyecto_proyecto")
    )
    private ProyectoGrado proyectoGrado;
}