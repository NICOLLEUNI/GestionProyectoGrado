package co.unicauca.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "formato_a")
public class FormatoAEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)  // Aquí usamos Enum para el modo
    @Column(nullable = false)
    private EnumModalidad mode; // Usamos EnumModalidad en lugar de String

    @Column(nullable = false)
    private String projectManagerEmail;

    private String projectCoManagerEmail;

    @Column(nullable = true) // Si puede ser nulo
    private String generalObjetive; // Objetivo general

    @Column(nullable = true) // Si puede ser nulo
    private String specificObjetives; // Objetivos específicos

    @Column(nullable = true) // Si puede ser nulo
    private String archivoPDF; // Link o nombre del archivo PDF

    @Column(nullable = true) // Si puede ser nulo
    private String cartaLaboral; // Link o nombre de la carta laboral

    @ManyToMany
    @JoinTable(
            name = "formato_a_estudiantes",
            joinColumns = @JoinColumn(name = "formato_a_id"),
            inverseJoinColumns = @JoinColumn(name = "persona_id")
    )
    private List<PersonaEntity> estudiantes = new ArrayList<>(); // Relación N:M con PersonaEntity

    public List<String> getEstudiantesEmails() {
        return estudiantes.stream()
                .map(PersonaEntity::getEmail)  // Asumiendo que PersonaEntity tiene un método getEmail()
                .collect(Collectors.toList());
    }

    @Enumerated(EnumType.STRING)  // Aquí usamos Enum para el estado
    @Column(nullable = false)
    private EnumEstado state = EnumEstado.ENTREGADO; // Usamos EnumEstado en lugar de String

    private int counter;

    @OneToMany(mappedBy = "formatoA", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FormatoAVersionEntity> versiones = new ArrayList<>();  // Relación 1:N con versiones


}

