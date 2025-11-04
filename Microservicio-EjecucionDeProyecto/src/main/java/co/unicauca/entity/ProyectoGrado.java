package co.unicauca.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "proyecto_grado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProyectoGrado {
    @Id
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @ElementCollection
    @CollectionTable(name = "proyecto_estudiantes", joinColumns = @JoinColumn(name = "proyecto_id"))
    @Column(name = "email_estudiante")
    private List<String> estudiantesEmail = new ArrayList<>();

    // ✅ SOLO el ID para la relación - COHERENTE con tu DTO
    @Column(name = "id_formato_a")
    private Long idFormatoA;

    /*
    // ✅ RELACIÓN con historial (usa proyecto_grado_id en la otra tabla)
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "proyecto_grado_id",
            referencedColumnName = "id",
            insertable = false,
            updatable = false
    )
    private List<FormatoAVersion> historialFormatos = new ArrayList<>();
*/
    private String estado;

    /*
    // Métodos de negocio
    public void agregarVersionAlHistorial(FormatoAVersion version) {
        if (historialFormatos == null) {
            historialFormatos = new ArrayList<>();
        }
        historialFormatos.add(version);
    }
*/
    public void establecerIdFormatoA(Long idFormatoA) {
        this.idFormatoA = idFormatoA;
    }

}