package co.unicauca.dto;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AnteproyectoRequest {

   private Long id;
    private String titulo;
    private LocalDate fecha;
    private String estado;
    private String observaciones;
    private Long idProyectoGrado;
}
