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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "formato_a_versiones")
public class FormatoAVersion {

    @Id
    @Column(nullable = false)
    private Long id;

    private int numeroVersion;
    private LocalDate fecha;
    private String title;

    @Enumerated(EnumType.STRING)
    private EnumModalidad mode;

    @Enumerated(EnumType.STRING)
    private EnumEstado state;

    private String observations;
    private int counter;
    private Long idFormatoA;



}