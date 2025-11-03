package co.unicauca.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
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
    private LocalDate fechaCreacion;

    @ElementCollection
    @CollectionTable(name = "proyecto_estudiantes", joinColumns = @JoinColumn(name = "proyecto_id"))
    @Column(name = "email_estudiante")
    private List<String> estudiantesEmail; //un estudiante solo puede tener un proyecto activo

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "formato_a_actual_id")
    private FormatoA formatoAActual;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_grado_id")
    private List<FormatoAVersion> historialFormatosA = new ArrayList<>();

    private String estado; //este se trabajará con el patron state mas adelante

    @OneToOne(mappedBy = "proyectoGrado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Anteproyecto anteproyecto;


    public ProyectoGrado(long id, String nombre, LocalDate fechaCreacion, List<String> estudiantesEmail, FormatoA formatoAActual, List<FormatoAVersion> historialFormatosA, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
        this.estudiantesEmail = estudiantesEmail;
        this.formatoAActual = formatoAActual;
        this.historialFormatosA = historialFormatosA;
        this.estado = estado;
    }

    public ProyectoGrado(long id, String nombre, LocalDate fechaCreacion, List<String> estudiantesEmail, FormatoA formatoAActual, List<FormatoAVersion> historialFormatosA, String estado, Anteproyecto anteproyecto) {
        this.id = id;
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
        this.estudiantesEmail = estudiantesEmail;
        this.formatoAActual = formatoAActual;
        this.historialFormatosA = historialFormatosA;
        this.estado = estado;
        this.anteproyecto = anteproyecto;
    }

    public ProyectoGrado() {}


}
