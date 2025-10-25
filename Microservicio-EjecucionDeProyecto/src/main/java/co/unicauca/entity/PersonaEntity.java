package co.unicauca.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonaEntity {

    @Id
    private Long id;

    private String name;
    private String lastname;
    private String email;

    // Roles como Set<String>
    @ElementCollection(targetClass = EnumRol.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "persona_roles", joinColumns = @JoinColumn(name = "idUsuario"))
    @Enumerated(EnumType.STRING)
    private Set<EnumRol> roles;

    private String department;

    // Relación inversa con proyectos
    @ManyToMany(mappedBy = "estudiantes")
    private List<ProyectoGradoEntity> proyectos = new ArrayList<>();


}

