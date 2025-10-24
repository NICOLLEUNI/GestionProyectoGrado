package co.unicauca.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table (name = "persona")
public class Persona {

    @Id
    private Long id;
    private String name;
    private String lastname;
    private String email;
    private List<String> roles;
    private String department;

    // MÉTODOS PARA VALIDAR ROLES
    public boolean esDocente() {
        return this.roles != null && this.roles.stream()
                .anyMatch(rol -> "DOCENTE".equalsIgnoreCase(rol));
    }

    public boolean esEstudiante() {
        return this.roles != null && this.roles.stream()
                .anyMatch(rol -> "ESTUDIANTE".equalsIgnoreCase(rol));
    }

    public boolean esCoordinador() {
        return this.roles != null && this.roles.stream()
                .anyMatch(rol -> "COORDINADOR".equalsIgnoreCase(rol));
    }

    public boolean esJefeDepartamento() {
        return this.roles != null && this.roles.stream()
                .anyMatch(rol -> "JEFE_DEPARTAMENTO".equalsIgnoreCase(rol));
    }


}
