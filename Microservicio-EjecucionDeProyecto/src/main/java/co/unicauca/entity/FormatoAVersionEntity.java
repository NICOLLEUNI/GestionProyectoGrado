package co.unicauca.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class FormatoAVersionEntity {

    @Id
    private Long id;

    private int numeroVersion;// 1, 2, 3

    private LocalDate fecha;

    // Copia de campos versionables
    private String title;
    private EnumModalidad mode;
    private String generalObjetive;
    private String specificObjetives;
    private String archivoPDF;
    private String cartaLaboral; //opcional

    // Estado de la versión
    private EnumEstado state;        // entregado, aprobado, rechazado
    private String observations;     // comentarios del coordinador - inicia en null
    private int counter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formato_a_id")
    private FormatoAEntity formatoA;
}
