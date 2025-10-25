package co.unicauca.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "proyecto_grado")
public class ProyectoGradoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    // Almacenar los correos electrónicos de los estudiantes como String
    @Column(nullable = false)
    @ElementCollection  // Usamos ElementCollection para manejar la lista de correos
    private List<String> estudiantesEmail = new ArrayList<>(); // Lista de correos electrónicos de los estudiantes

    @ManyToMany
    @JoinTable(
            name = "proyecto_persona", // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "proyecto_id"), // Clave foránea en la tabla intermedia que hace referencia a ProyectoGradoEntity
            inverseJoinColumns = @JoinColumn(name = "persona_id") // Clave foránea en la tabla intermedia que hace referencia a PersonaEntity
    )
    private List<PersonaEntity> estudiantes = new ArrayList<>(); // Relación con la entidad Persona (estudiantes)

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "formato_a_actual_id")
    private FormatoAEntity formatoAActual; // Relación con FormatoA

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "proyecto_grado_id")
    private List<FormatoAVersionEntity> historialFormatosA = new ArrayList<>(); // Relación con el historial de versiones

    private String estado; // Estado del proyecto

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "anteproyecto_id")
    private AnteproyectoEntity anteproyecto; // Relación con Anteproyecto

    // Constructor vacío
    public ProyectoGradoEntity() {}

    // Constructor con todos los campos
    public ProyectoGradoEntity(Long id, String titulo, LocalDateTime fechaCreacion, List<String> estudiantesEmail,
                               List<PersonaEntity> estudiantes, FormatoAEntity formatoAActual, List<FormatoAVersionEntity> historialFormatosA,
                               String estado, AnteproyectoEntity anteproyecto) {
        this.id = id;
        this.titulo = titulo;
        this.fechaCreacion = fechaCreacion;
        this.estudiantesEmail = estudiantesEmail;
        this.estudiantes = estudiantes;
        this.formatoAActual = formatoAActual;
        this.historialFormatosA = historialFormatosA;
        this.estado = estado;
        this.anteproyecto = anteproyecto;
    }

    // Método para actualizar la lista de correos electrónicos a partir de los estudiantes asociados
    public void actualizarEstudiantesEmail() {
        this.estudiantesEmail = estudiantes.stream()
                .map(PersonaEntity::getEmail) // Obtener el correo electrónico de cada estudiante
                .collect(Collectors.toList());
    }

    // Método para agregar estudiantes a la lista
    public void agregarEstudiante(PersonaEntity estudiante) {
        this.estudiantes.add(estudiante);
        actualizarEstudiantesEmail(); // Actualizamos los correos electrónicos al agregar un estudiante
    }

    // Método para eliminar estudiantes de la lista
    public void eliminarEstudiante(PersonaEntity estudiante) {
        this.estudiantes.remove(estudiante);
        actualizarEstudiantesEmail(); // Actualizamos los correos electrónicos al eliminar un estudiante
    }
}