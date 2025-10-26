package co.unicauca.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Persona {
    @Id
    @Column(nullable = false)
    private Long idUsuario;
    private String name;
    private String lastname;

    @Column(nullable = false, unique = true)
    private String email;
    private String department;
    private String programa;

    @ElementCollection(targetClass = EnumRol.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "persona_roles", joinColumns = @JoinColumn(name = "idUsuario"))
    @Enumerated(EnumType.STRING)
    private Set<EnumRol> roles;

    // comparan con EnumRol
    public boolean esDocente() {
        return this.roles != null && this.roles.contains(EnumRol.DOCENTE);
    }

    public boolean esEstudiante() {
        return this.roles != null && this.roles.contains(EnumRol.ESTUDIANTE);
    }

    public boolean esCoordinador() {
        return this.roles != null && this.roles.contains(EnumRol.COORDINADOR);
    }

    public boolean esJefeDepartamento() {
        return this.roles != null && this.roles.contains(EnumRol.JEFE_DEPARTAMENTO);
    }

    //MÉTODO GENERAL PARA CUALQUIER ROL
    public boolean tieneRol(EnumRol rol) {
        return this.roles != null && this.roles.contains(rol);
    }
}

