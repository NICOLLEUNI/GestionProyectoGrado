package co.unicauca.infraestructure.adapters.output.persistence.entities;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class AnteproyectoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private LocalDate fechaCreacion;
    private String archivoPDF;
    private String estado;
    private Long idProyectoGrado;
    private String emailEvaluador1;
    private String emailEvaluador2;
}