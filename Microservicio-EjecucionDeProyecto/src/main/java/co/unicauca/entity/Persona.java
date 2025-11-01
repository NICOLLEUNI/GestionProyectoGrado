package co.unicauca.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "persona")
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

}
