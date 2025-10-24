package co.unicauca.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "proyecto_grado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProyectoGradoEntity {

    @Id
    private Long id;                         // mismo id que en Submission

    private String nombre;

    private LocalDate fecha;                 // fecha de creación o aprobación

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "proyecto_estudiantes",
            joinColumns = @JoinColumn(name = "proyecto_id")
    )
    @Column(name = "email_estudiante")
    private List<String> estudiantesEmail;   // correos de los estudiantes asociados

    @Column(name = "id_formato_a")
    private Long idFormatoA;

    @Column(name = "id_anteproyecto")
    private Long idAnteproyecto;

    // Estados propios del microservicio Ejecución
    private String estado;                   // EN_EJECUCION, FINALIZADO, SUSPENDIDO
    private String versionActiva;            // versión actual del Formato A o documento

}
