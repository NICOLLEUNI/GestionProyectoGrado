package co.unicauca.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "formato_a_version")
public class FormatoAVersionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numVersion;
    private LocalDate fecha;  // `date` corresponde aquí

    private String title;

    @Enumerated(EnumType.STRING)  // Aquí usamos Enum para el mode
    private EnumModalidad mode; // Usamos EnumModalidad en lugar de String

    private String generalObjetive;
    private String specificObjetives;
    private String archivoPDF;
    private String cartaLaboral;

    @Enumerated(EnumType.STRING)  // Aquí usamos Enum para el state
    private EnumEstado state; // Usamos EnumEstado en lugar de String

    private String observations;  // `observations` corresponde aquí
    private int counter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formato_a_id")
    private FormatoAEntity formatoA;
}
