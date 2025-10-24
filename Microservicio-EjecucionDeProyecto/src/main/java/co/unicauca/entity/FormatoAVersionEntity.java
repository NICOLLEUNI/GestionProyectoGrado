package co.unicauca.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "formato_a_version")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormatoAVersionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numVersion;
    private LocalDate fecha;
    private String titulo;
    private String modalidad;
    private String estado;
    private String observaciones;
    private int counter;
    private int IdformatoA;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formato_a_id")
    private FormatoAEntity formatoA;
}