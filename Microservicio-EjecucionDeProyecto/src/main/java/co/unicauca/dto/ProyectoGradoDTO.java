package co.unicauca.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
public class ProyectoGradoDTO   {

    private Long id;
    private String titulo;
    private String modalidad;   // Práctica / Investigación
    private String estado;      // PENDIENTE, etc.
    private Long idEstudiante;
    private Long idDirector;

}


