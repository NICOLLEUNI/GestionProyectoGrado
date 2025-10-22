package co.unicauca.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="proyecto_grado")
public class ProyectoGrado {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false, unique = true)
    private String codigoEstudiante; //un estudiante solo puede tener un proyecto activo

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "formato_a_actual_id")
    private FormatoA formatoAActual;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_grado_id")
    private List<FormatoAVersion> historialFormatosA = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "anteproyecto_id")
    private Anteproyecto anteproyecto;

    //quiero tener el atributo estado, pero quiero hacerlo usando el patron State

    //sin anteproyecto
    public ProyectoGrado(List<FormatoAVersion> historialFormatosA, FormatoA formatoAActual, String codigoEstudiante, LocalDateTime fechaCreacion, String nombre, long id) {
        this.historialFormatosA = historialFormatosA;
        this.formatoAActual = formatoAActual;
        this.codigoEstudiante = codigoEstudiante;
        this.fechaCreacion = fechaCreacion;
        this.nombre = nombre;
        this.id = id;
    }

    public ProyectoGrado(long id, String nombre, LocalDateTime fechaCreacion, String codigoEstudiante, FormatoA formatoAActual, List<FormatoAVersion> historialFormatosA, Anteproyecto anteproyecto) {
        this.id = id;
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
        this.codigoEstudiante = codigoEstudiante;
        this.formatoAActual = formatoAActual;
        this.historialFormatosA = historialFormatosA;
        this.anteproyecto = anteproyecto;
    }

    public ProyectoGrado() {}


}
