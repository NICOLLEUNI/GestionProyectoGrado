package co.unicauca.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table (name = "anteproyecto")
public class Anteproyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private LocalDate fechaCreacion;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumEstadoAnteproyecto estado = EnumEstadoAnteproyecto.ENTREGADO;


    // Relación con ProyectoGrado
    @OneToOne
    @JoinColumn(name = "proyecto_grado_id")
    private ProyectoGrado proyectoGrado;

    public Anteproyecto(Long id, String titulo, LocalDate fechaCreacion, EnumEstadoAnteproyecto estado, ProyectoGrado proyectoGrado) {
        this.id = id;
        this.titulo = titulo;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.proyectoGrado = proyectoGrado;
    }

    public Anteproyecto() {
    }
}
