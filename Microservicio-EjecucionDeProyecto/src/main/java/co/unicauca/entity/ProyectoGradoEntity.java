package co.unicauca.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "proyecto_grado")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProyectoGradoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descripcion;
    private String estado;

    // 🔗 Relación 1:1 con Anteproyecto
    @OneToOne(mappedBy = "proyectoGrado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AnteproyectoEntity anteproyecto;

    // 🔗 Relación 1:N con FormatoA
    @OneToMany(mappedBy = "proyectoGrado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FormatoAEntity> formatosA = new ArrayList<>();

    // 🔗 Relación N:N con Personas (estudiantes, jurados, etc.)
    @ManyToMany
    @JoinTable(
            name = "proyecto_persona",
            joinColumns = @JoinColumn(name = "proyecto_id"),
            inverseJoinColumns = @JoinColumn(name = "persona_id")
    )
    private List<PersonaEntity> personas = new ArrayList<>();
}
