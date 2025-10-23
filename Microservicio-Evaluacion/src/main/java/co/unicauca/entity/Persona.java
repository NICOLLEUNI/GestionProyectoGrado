package co.unicauca.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.EnumSet;

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
    private String password;
    private String department;

    @ElementCollection(targetClass = EnumRol.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "persona_roles", joinColumns = @JoinColumn(name = "idUsuario"))
    @Enumerated(EnumType.STRING)
    private EnumSet<EnumRol> roles;
}
