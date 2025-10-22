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

    @Column(nullable = false)
    private String archivoPDF; // Ruta o nombre del archivo

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumEstadoAnteproyecto estado = EnumEstadoAnteproyecto.ENTREGADO;

    @Column(columnDefinition = "TEXT")
    private String observaciones; // Comentarios del jefe de departamento

    // Relación con ProyectoGrado
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_grado_id")
    private ProyectoGrado proyectoGrado;

    public Anteproyecto(Long id, String titulo, LocalDate fechaCreacion, String archivoPDF, EnumEstadoAnteproyecto estado, String observaciones, ProyectoGrado proyectoGrado) {
        this.id = id;
        this.titulo = titulo;
        this.fechaCreacion = fechaCreacion;
        this.archivoPDF = archivoPDF;
        this.estado = estado;
        this.observaciones = observaciones;
        this.proyectoGrado = proyectoGrado;
    }
}
