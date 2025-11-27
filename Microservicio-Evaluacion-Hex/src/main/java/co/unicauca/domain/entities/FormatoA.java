package co.unicauca.domain.entities;

import java.util.List;

public class FormatoA {

    private Long id;
    private String title;
    private String mode;

    private Persona projectManager;   // esta Persona también debería ser de dominio
    private Persona projectCoManager;

    private String generalObjective;
    private String specificObjectives;
    private String archivoPDF;
    private String cartaLaboral;
    private int counter;

    private List<Persona> estudiantes;
    private EnumEstado state;
    private String observations;

    public FormatoA(Long id, String title, String mode, Persona projectManager, Persona projectCoManager, String generalObjective, String specificObjectives, String archivoPDF, String cartaLaboral, int counter, List<Persona> estudiantes, EnumEstado state, String observations) {
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

    public void asignarManager(Persona projectManager) {
        if (projectManager == null) throw new IllegalArgumentException("Director no puede ser nulo");
        if (!projectManager.tieneRol(EnumRol.DOCENTE)) throw new IllegalStateException("Persona no es docente");
        this.projectManager = projectManager;
    }

    public void asignarCoManager(Persona projectCoManager) {
        if (projectCoManager == null) throw new IllegalArgumentException("El Co-Director no puede ser nulo");
        if (!projectCoManager.tieneRol(EnumRol.DOCENTE)) throw new IllegalStateException("Persona no es docente");
        this.projectCoManager = projectCoManager;
    }

    public void addEstudiante(Persona estudiante) {
        if (estudiante == null) throw new IllegalArgumentException("Estudiante inválido");
        if (!estudiante.tieneRol(EnumRol.ESTUDIANTE)) throw new IllegalStateException("La persona no es estudiante");
        this.estudiantes.add(estudiante);
    }

    public void validarYAsignarEstadoInicial(EnumEstado estado) {
        if (estado != EnumEstado.ENTREGADO) {
            throw new IllegalStateException("El FormatoA debe recibirse como ENTREGADO");
        }
        this.state = EnumEstado.ENTREGADO;
    }

    public void aprobar() {
        if (this.state != EnumEstado.ENTREGADO) {
            throw new IllegalStateException("Sólo se puede aprobar un formato entregado");
        }
        this.state = EnumEstado.APROBADO;
    }

    public void rechazar(String observations) {
        if (observations == null || observations.isBlank()) {
            throw new IllegalArgumentException("Debe proveer observaciones para rechazo");
        }
        this.state = EnumEstado.RECHAZADO;
        this.observations = observations;
        this.counter = this.counter + 1;
    }



    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMode() {
        return mode;
    }

    public Persona getProjectManager() {
        return projectManager;
    }

    public Persona getProjectCoManager() {
        return projectCoManager;
    }

    public String getGeneralObjective() {
        return generalObjective;
    }

    public String getSpecificObjectives() {
        return specificObjectives;
    }

    public String getArchivoPDF() {
        return archivoPDF;
    }

    public String getCartaLaboral() {
        return cartaLaboral;
    }

    public int getCounter() {
        return counter;
    }

    public List<Persona> getEstudiantes() {
        return estudiantes;
    }

    public EnumEstado getState() {
        return state;
    }

    public String getObservations() {
        return observations;
    }
}
