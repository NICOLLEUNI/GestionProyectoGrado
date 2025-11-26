package co.unicauca.entity;

import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data

public class Anteproyecto {

    private Long id;
    private String titulo;

    // Estos nombres deben coincidir con el JSON exacto
    private String emailEvaluador1;
    private String emailEvaluador2;
}
