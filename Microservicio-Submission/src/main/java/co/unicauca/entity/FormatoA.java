package co.unicauca.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table (name = "formatoA")
public class FormatoA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumModalidad mode;

    // CORREO del docente director
    @Column(nullable = false)
    private String projectManagerEmail;

    // CORREO del codirector (opcional)
    private String projectCoManagerEmail;

    @Column(nullable = false)
    private LocalDate date;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String generalObjetive;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String specificObjetives;

    @Column(nullable = false)
    private String archivoPDF;

    private String cartaLaboral;

    // CORREOS de los estudiantes asociados
    @ElementCollection
    @CollectionTable(name = "formato_a_estudiantes", joinColumns = @JoinColumn(name = "formato_a_id"))
    @Column(name = "estudiante_email")
    private List<String> estudianteEmails = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumEstado state = EnumEstado.ENTREGADO;

    @Column(columnDefinition = "TEXT")
    private String observations;

    private int counter;

    public FormatoA(Long id, String title, EnumModalidad mode, String projectManagerEmail, String projectCoManagerEmail, LocalDate date, String generalObjetive, String specificObjetives, String archivoPDF, String cartaLaboral, List<String> estudianteEmails, EnumEstado state, String observations, int counter) {
        this.id = id;
        this.title = title;
        this.mode = mode;
        this.projectManagerEmail = projectManagerEmail;
        this.projectCoManagerEmail = projectCoManagerEmail;
        this.date = date;
        this.generalObjetive = generalObjetive;
        this.specificObjetives = specificObjetives;
        this.archivoPDF = archivoPDF;
        this.cartaLaboral = cartaLaboral;
        this.estudianteEmails = estudianteEmails;
        this.state = state;
        this.observations = observations;
        this.counter = counter;
    }

    public FormatoA() {}
}
