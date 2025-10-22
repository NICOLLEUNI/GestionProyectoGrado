package co.unicauca.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class FormatoA {

    @Id
    private Long id;
    private String title;
    private String mode;
    private String projectManager;
    private String projectCoManager;
    private LocalDate date;
    private String generalObjetive;
    private String specificObjetives;
    private String archivoPDF;
    private String cartaLaboral;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "formatoa_estudiantes", joinColumns = @JoinColumn(name = "formato_a_id"))
    @Column(name = "email_estudiante")
    private List<String> estudiantes;
    private int counter;
    @Enumerated(EnumType.STRING)
    private EnumEstado state = EnumEstado.ENTREGADO ;
    private String observations;        // Observaciones del coordinador
}
