package co.unicauca.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "persona")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PersonaEntity {


    @Id
    private Long id;                       // mismo ID que en PersonaRequest

    private String nombreCompleto;         // name + lastname
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "persona_roles",
            joinColumns = @JoinColumn(name = "persona_id")
    )
    @Column(name = "rol")
    private Set<String> roles;             // ESTUDIANTE, DOCENTE, COORDINADOR, etc.

    private String departamento;           // departement

    private Boolean activo;                // estado de sincronización
}
