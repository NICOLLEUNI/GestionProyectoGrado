package co.unicauca.infraestructure.adapters.output.persistence.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import co.unicauca.domain.entities.EnumEstado;

@Entity
@Table(name = "formato_a")
public class FormatoAEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String mode;

    // RELACIÓN CON DIRECTOR
    @ManyToOne
    @JoinColumn(name = "project_manager_id")
    private PersonaEntity projectManager;

    // RELACIÓN CON CO-DIRECTOR
    @ManyToOne
    @JoinColumn(name = "project_comanager_id")
    private PersonaEntity projectCoManager;

    private String generalObjective;

    @Column(length = 3000)
    private String specificObjectives;

    private String archivoPDF;

    private String cartaLaboral;

    private int counter;

    // LISTA DE ESTUDIANTES
    @ManyToMany
    @JoinTable(
            name = "formato_a_estudiantes",
            joinColumns = @JoinColumn(name = "formato_a_id"),
            inverseJoinColumns = @JoinColumn(name = "persona_id")
    )
    private List<PersonaEntity> estudiantes;

    @Enumerated(EnumType.STRING)
    private EnumEstado state;

    @Column(length = 2000)
    private String observations;

    public FormatoAEntity() {
    }

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
        this.estudiantes = estudiantes;
        this.state = state;
        this.observations = observations;
    }

    // ---------- GETTERS Y SETTERS ------------

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
    public void setEstudiantes(List<PersonaEntity> estudiantes) { this.estudiantes = estudiantes; }

    public EnumEstado getState() { return state; }
    public void setState(EnumEstado state) { this.state = state; }

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }


}
