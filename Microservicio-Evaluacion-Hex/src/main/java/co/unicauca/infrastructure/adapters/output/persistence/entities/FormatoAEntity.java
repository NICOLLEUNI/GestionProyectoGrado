package co.unicauca.infrastructure.adapters.output.persistence.entities;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
import co.unicauca.domain.entities.EnumEstado;

@Entity
@Table(name = "formato_a")
public class FormatoAEntity {

    @Id

    private Long id;

    private String title;

    private String mode;

    @ManyToOne
    @JoinColumn(name = "project_manager_id")
    private PersonaEntity projectManager;

    @ManyToOne
    @JoinColumn(name = "project_comanager_id")
    private PersonaEntity projectCoManager;

    private String generalObjective;

    @Column(length = 3000)
    private String specificObjectives;

    private String archivoPDF;

    private String cartaLaboral;

    private int counter;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "formato_a_estudiantes",
            joinColumns = @JoinColumn(name = "formato_a_id"),
            inverseJoinColumns = @JoinColumn(name = "persona_id")
    )
    private List<PersonaEntity> estudiantes = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private EnumEstado state = EnumEstado.ENTREGADO; // valor por defecto

    @Column(length = 2000)
    private String observations;

    public FormatoAEntity() {}

    public FormatoAEntity(Long id, String title, String mode, PersonaEntity projectManager, PersonaEntity projectCoManager,
                          String generalObjective, String specificObjectives, String archivoPDF, String cartaLaboral,
                          int counter, List<PersonaEntity> estudiantes, EnumEstado state, String observations) {
        this.id = id;
        this.title = title;
        this.mode = mode;
        this.projectManager = projectManager;
        this.projectCoManager = projectCoManager;
        this.generalObjective = generalObjective;
        this.specificObjectives = specificObjectives;
        this.archivoPDF = archivoPDF;
        this.cartaLaboral = cartaLaboral;
        this.counter = counter;
        this.estudiantes = estudiantes != null ? estudiantes : new ArrayList<>();
        this.state = state != null ? state : EnumEstado.ENTREGADO;
        this.observations = observations;
    }

    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }

    public PersonaEntity getProjectManager() { return projectManager; }
    public void setProjectManager(PersonaEntity projectManager) { this.projectManager = projectManager; }

    public PersonaEntity getProjectCoManager() { return projectCoManager; }
    public void setProjectCoManager(PersonaEntity projectCoManager) { this.projectCoManager = projectCoManager; }

    public String getGeneralObjective() { return generalObjective; }
    public void setGeneralObjective(String generalObjective) { this.generalObjective = generalObjective; }

    public String getSpecificObjectives() { return specificObjectives; }
    public void setSpecificObjectives(String specificObjectives) { this.specificObjectives = specificObjectives; }

    public String getArchivoPDF() { return archivoPDF; }
    public void setArchivoPDF(String archivoPDF) { this.archivoPDF = archivoPDF; }

    public String getCartaLaboral() { return cartaLaboral; }
    public void setCartaLaboral(String cartaLaboral) { this.cartaLaboral = cartaLaboral; }

    public int getCounter() { return counter; }
    public void setCounter(int counter) { this.counter = counter; }

    public List<PersonaEntity> getEstudiantes() { return estudiantes; }
    public void setEstudiantes(List<PersonaEntity> estudiantes) {
        this.estudiantes = estudiantes != null ? estudiantes : new ArrayList<>();
    }

    public EnumEstado getState() { return state; }
    public void setState(EnumEstado state) { this.state = state != null ? state : EnumEstado.ENTREGADO; }

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
}
