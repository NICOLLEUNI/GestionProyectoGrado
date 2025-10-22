package co.unicauca.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table (name = "formato_version")
public class FormatoAVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int numeroVersion; // 1, 2, 3
    private LocalDate fecha;

    // Copia de campos versionables
    private String title;
    private EnumModalidad mode;
    private String generalObjetive;
    private String specificObjetives;
    private String archivoPDF;
    private String cartaLaboral;

    // Estado de la versión
    private EnumEstado state;        // entregado, aprobado, rechazado
    private String observations;     // comentarios del coordinador

    // Relación con FormatoA principal
    @ManyToOne(fetch = FetchType.LAZY)
    private FormatoA formatoA;

    public FormatoAVersion(long id, int numeroVersion, LocalDate fecha, String title, EnumModalidad mode, String generalObjetive, String specificObjetives, String archivoPDF, String cartaLaboral, EnumEstado state, String observations, FormatoA formatoA) {
        this.id = id;
        this.numeroVersion = numeroVersion;
        this.fecha = fecha;
        this.title = title;
        this.mode = mode;
        this.generalObjetive = generalObjetive;
        this.specificObjetives = specificObjetives;
        this.archivoPDF = archivoPDF;
        this.cartaLaboral = cartaLaboral;
        this.state = state;
        this.observations = observations;
        this.formatoA = formatoA;
    }

    public FormatoAVersion() {}
}
