package co.unicauca.entity;

import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FormatoA {
    private Long id;
    private String titulo;

    // Estos nombres deben coincidir con el JSON exacto
    private List<String> correosEstudiantes;
    private List<String> correosDocentes;
    private List<String> departamentosDocentes;

}
