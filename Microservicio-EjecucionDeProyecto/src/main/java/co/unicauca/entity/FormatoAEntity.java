package co.unicauca.entity;


import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "formato_a")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormatoAEntity {

    @Id
    private String id; // viene como String desde Submission (puede ser UUID)

    private String title;
    private String mode;

    @Column(name = "director_email")
    private String projectManagerEmail;

    @Column(name = "codirector_email")
    private String projectCoManagerEmail;

    @Column(columnDefinition = "TEXT")
    private String generalObjetive;

    @Column(columnDefinition = "TEXT")
    private String specificObjetives;

    private String archivoPDF;   // ruta o identificador del archivo en el storage
    private String cartaLaboral; // idem

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "formato_a_estudiantes",
            joinColumns = @JoinColumn(name = "formato_a_id")
    )
    @Column(name = "email_estudiante")
    private List<String> estudiantes;

    private int counter; // número de revisión o versión

}
