package co.unicauca.domain.entities;

import java.util.ArrayList;
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

    public FormatoA() {
        this.estudiantes = new ArrayList<>();
    }

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
        this.estudiantes = estudiantes != null ? estudiantes : new ArrayList<>();
        this.state = state;
        this.observations = observations;
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

    //setters que no son setters

    public void asignarManagerD(Persona manager) {
        if (manager == null) throw new IllegalArgumentException("Director no puede ser nulo");
        if (!manager.tieneRol(EnumRol.DOCENTE)) throw new IllegalStateException("Persona no es docente");
        this.projectManager = manager;
    }

    public void asignarCoManagerD(Persona co) {
        // Codirector puede ser null (opcional)
        if (co != null && !co.tieneRol(EnumRol.DOCENTE)) {
            throw new IllegalStateException("Persona no es docente");
        }
        this.projectCoManager = co;
    }

    public void asignarEstudiantesD(List<Persona> lista) {
        if (lista == null) return;

        this.estudiantes.clear();

        for (Persona e : lista) {
            if (e == null) throw new IllegalArgumentException("Estudiante inválido");
            if (!e.tieneRol(EnumRol.ESTUDIANTE)) throw new IllegalStateException("La persona no es estudiante");
            this.estudiantes.add(e);
        }
    }

    public void validarYAsignarEstadoInicialD(EnumEstado estado) {
        if (estado != EnumEstado.ENTREGADO) throw new IllegalStateException("El FormatoA debe recibirse como ENTREGADO");
        this.state = EnumEstado.ENTREGADO;
    }


    public void rechazarD(String observations) {
        if (observations == null || observations.isBlank()) throw new IllegalArgumentException("Debe proveer observaciones para rechazo");
        this.state = EnumEstado.RECHAZADO;
        this.observations = observations;
        this.counter = this.counter + 1;
    }

    public void actualizarTituloD(String title) {
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Título inválido");
        this.title = title;
    }

    public void actualizarModo(String mode) {
        if (mode == null || mode.isBlank()) throw new IllegalArgumentException("Modo inválido");
        this.mode = mode;
    }

    public void actualizarGeneralObjective(String obj) {
        this.generalObjective = obj;
    }

    public void actualizarSpecificObjectives(String obj) {
        this.specificObjectives = obj;
    }

    public void actualizarArchivoPDF(String path) {
        this.archivoPDF = path;
    }

    public void actualizarCartaLaboral(String path) {
        this.cartaLaboral = path;
    }

    public void actualizarObservations(String obs) {
        this.observations = obs;
    }

    public void actualizarCounter(int value) {
        this.counter = value;
    }


}
