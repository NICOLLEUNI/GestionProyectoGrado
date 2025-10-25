package co.unicauca.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "persona")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String lastname;
    private String email;

    // Roles como Set<String>
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "persona_roles", joinColumns = @JoinColumn(name = "persona_id"))
    @Column(name = "rol")
    private Set<String> roles = new HashSet<>();

    private String department;

    // Relación inversa con proyectos
    @ManyToMany(mappedBy = "estudiantes")
    private List<ProyectoGradoEntity> proyectos = new ArrayList<>();

    // Constructor con parámetros
    public PersonaEntity(String name, String lastname, String email, Set<String> roles, String department) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.roles = roles != null ? roles : new HashSet<>();
        this.department = department;
    }

    // Constructor con parámetros para ProyectoGradoEntity
    public PersonaEntity(String name, String lastname, String email, Set<String> roles, String department, List<ProyectoGradoEntity> proyectos) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.roles = roles != null ? roles : new HashSet<>();
        this.department = department;
        this.proyectos = proyectos != null ? proyectos : new ArrayList<>();
    }


}

