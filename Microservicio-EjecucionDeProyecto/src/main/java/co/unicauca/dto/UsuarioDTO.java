package co.unicauca.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {

    private Long id;
    private String nombreCompleto;
    private String correo;
    private String rol; // ESTUDIANTE, DIRECTOR, EVALUADOR, COMITE
}
