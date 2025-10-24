package co.unicauca.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@Data
public class ProyectoGradoRequest {

    private Long id;
    private  String nombre;
    private LocalDate fecha;
    private List<String> estudiantesEmail;
    private  Long IdFormatoA;
    private  Long IdAnteproyecto;

}


