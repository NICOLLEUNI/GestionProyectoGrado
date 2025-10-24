package co.unicauca.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class FormatoAVersionRequest {

    private Long id;
    private int numVersion;
    private LocalDate fecha;
    private String titulo;
    private String modalidad;
    private String estado;
    private String observaciones;
    private int counter;
    private  int formatoAId;
}
