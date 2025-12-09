package co.unicauca.infrastructure.adapters.output.persistence.entities;

import co.unicauca.domain.entities.EnumRol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "persona")
public class PersonaEntity {

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

    public boolean tieneRol(EnumRol rol) {
        return roles != null && roles.contains(rol);
    }

}
