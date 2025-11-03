package co.unicauca.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "proyecto_grado")
public class ProyectoGrado {
    @Id
    private Long id;

    // ✅ QUITAR unique = true para eliminar la constraint única
    @Column(nullable = false)  // ← CAMBIADO: sin unique=true
    private String nombre;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @ElementCollection
    @CollectionTable(name = "proyecto_estudiantes", joinColumns = @JoinColumn(name = "proyecto_id"))
    @Column(name = "email_estudiante")
    private List<String> estudiantesEmail = new ArrayList<>();

    @Column(name = "id_formato_a")
    private Long idFormatoA;

    // ✅ RELACIÓN OneToOne - VERIFICAR que no cree constraint única automática
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "formato_version_actual_id",
            unique = false  // ← IMPORTANTE: evitar constraint única
    )
    private FormatoAVersion formatoVersionActual;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_grado_id")
    private List<FormatoAVersion> historialFormatos = new ArrayList<>();

    private String estado;

    // Métodos de negocio
    public void agregarVersionFormato(FormatoAVersion version) {
        if (historialFormatos == null) {
            historialFormatos = new ArrayList<>();
        }
        historialFormatos.add(version);
        this.formatoVersionActual = version;
    }

    public void establecerIdFormatoA(Long idFormatoA) {
        this.idFormatoA = idFormatoA;
    }
}

