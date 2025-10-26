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
@Table(name = "proyecto_grado")
public class ProyectoGrado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @ElementCollection
    @CollectionTable(name = "proyecto_estudiantes", joinColumns = @JoinColumn(name = "proyecto_id"))
    @Column(name = "email_estudiante")
    private List<String> estudiantesEmail = new ArrayList<>();

    // ✅ CAMPO CORREGIDO: idFormatoA en lugar de formatoAExternoId
    @Column(name = "id_formato_a")
    private Long idFormatoA;

    // NUESTRA VERSIÓN LOCAL DEL FORMATO
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "formato_version_actual_id")
    private FormatoAVersion formatoVersionActual;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_grado_id")
    private List<FormatoAVersion> historialFormatos = new ArrayList<>();

    private String estado;

    @OneToOne(mappedBy = "proyectoGrado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Anteproyecto anteproyecto;

    public ProyectoGrado() {}

    // Métodos de negocio
    public void agregarVersionFormato(FormatoAVersion version) {
        if (historialFormatos == null) {
            historialFormatos = new ArrayList<>();
        }
        historialFormatos.add(version);
        this.formatoVersionActual = version;
    }

    public void establecerAnteproyecto(Anteproyecto anteproyecto) {
        this.anteproyecto = anteproyecto;
        anteproyecto.setProyectoGrado(this);
    }

    // ✅ MÉTODO CORREGIDO: idFormatoA en lugar de formatoAExternoId
    public void establecerIdFormatoA(Long idFormatoA) {
        this.idFormatoA = idFormatoA;
    }
}