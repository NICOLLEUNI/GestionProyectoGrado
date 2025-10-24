package co.unicauca.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "formato_a")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormatoAEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String titulo;
    private String estado;
    private String modalidad;
    private int counter;
    private String observaciones;

    // 🔗 Relación inversa con Proyecto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_id")
    private ProyectoGradoEntity proyectoGrado;

    // 🔗 Relación 1:N con versiones
    @OneToMany(mappedBy = "formatoA", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FormatoAVersionEntity> versiones = new ArrayList<>();
}
