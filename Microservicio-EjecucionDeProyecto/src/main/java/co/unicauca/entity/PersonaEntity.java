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
    private String id;


    private String name;
    private String lastname;
    // Correo electrónico
    private String email;

    // 🔹 Roles como Set<String>
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "persona_roles", joinColumns = @JoinColumn(name = "persona_id"))
    @Column(name = "rol")
    private Set<String> roles = new HashSet<>();

    // Departamento
    private String department;

    // 🔗 Relación inversa con proyectos
    @ManyToMany(mappedBy = "personas")
    private List<ProyectoGradoEntity> proyectos = new ArrayList<>();
}
