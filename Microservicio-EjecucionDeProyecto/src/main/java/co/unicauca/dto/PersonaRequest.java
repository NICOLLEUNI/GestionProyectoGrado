package co.unicauca.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonaRequest {

    private Long id;
    private String name;
    private String lastname;
    private String email;
    private Set<String> roles; // ESTUDIANTE, DOCENTE, COORDINADOR,JEFE_DEPARTAMENTO
    private String departement;
}
