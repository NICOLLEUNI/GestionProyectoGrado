package co.unicauca.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class FormatoAEntity {

    @Id
    @Column(nullable = false)
    private Long id;
    private String title;
    private String mode;


    @ManyToOne
    @JoinColumn(name = "director_id")
    private PersonaEntity projectManager;

    @ManyToOne
    @JoinColumn(name = "codirector_id")
    private PersonaEntity projectCoManager;
    private String generalObjetive;
    private String specificObjetives;
    private String archivoPDF;
    private String cartaLaboral;
    private int counter;


    @ElementCollection
    @CollectionTable(name = "formato_a_estudiantes", joinColumns = @JoinColumn(name = "formato_a_id"))
    @Column(name = "estudiante_email")
    private List<String> estudianteEmails = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "formatoa_estudiantes",
            joinColumns = @JoinColumn(name = "formatoa_id"),
            inverseJoinColumns = @JoinColumn(name = "persona_id")
    )
    private List<PersonaEntity> estudiantes = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private EnumEstado state = EnumEstado.ENTREGADO ;

    @OneToMany(mappedBy = "formatoA", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FormatoAVersionEntity> versiones = new ArrayList<>();  // Relación 1:N con versiones


}

