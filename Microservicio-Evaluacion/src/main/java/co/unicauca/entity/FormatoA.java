package co.unicauca.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class FormatoA {

    @Id
    @Column(nullable = false)
    private Long id;
    private String title;
    private String mode;

    @ManyToOne
    @JoinColumn(name = "director_id")
    private Persona projectManager;

    @ManyToOne
    @JoinColumn(name = "codirector_id")
    private Persona projectCoManager;
    private String generalObjetive;
    private String specificObjetives;
    private String archivoPDF;
    private String cartaLaboral;
    private int counter;

    // 🔹 Relación con Persona (estudiantes)
    @ManyToMany
    @JoinTable(
            name = "formatoa_estudiantes",
            joinColumns = @JoinColumn(name = "formatoa_id"),
            inverseJoinColumns = @JoinColumn(name = "persona_id")
    )
    private List<Persona> estudiantes = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private EnumEstado state = EnumEstado.ENTREGADO ;
    private String observations;        // Observaciones del coordinador
}
