package co.unicauca.entity;

import java.time.LocalDate;

public class FormatoAVersion {

    private Long id;
    private int numeroVersion;
    public LocalDate fecha;
    private String title;
    private EnumModalidad mode;
    private String generalObjetive;
    private String specificObjetives;
    private String archivoPDF;
    private String cartaLaboral;
    private EnumEstado state;
    private String observations;
    private int counter;
    private Long idFormatoA;

    // Constructor vacío
    public FormatoAVersion() {
    }

    // Constructor con todos los parámetros
    public FormatoAVersion(Long id, int numeroVersion, LocalDate fecha, String title, EnumModalidad mode, String generalObjetive, String specificObjetives, String archivoPDF, String cartaLaboral, EnumEstado state, String observations, int counter, Long idFormatoA) {
        this.id = id;
        this.numeroVersion = numeroVersion;
        this.fecha = fecha;
        this.title = title;
        this.mode = mode;
        this.generalObjetive = generalObjetive;
        this.specificObjetives = specificObjetives;
        this.archivoPDF = archivoPDF;
        this.cartaLaboral = cartaLaboral;
        this.state = state;
        this.observations = observations;
        this.counter = counter;
        this.idFormatoA = idFormatoA;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumeroVersion() {
        return numeroVersion;
    }

    public void setNumeroVersion(int numeroVersion) {
        this.numeroVersion = numeroVersion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public EnumModalidad getMode() {
        return mode;
    }

    public void setMode(EnumModalidad mode) {
        this.mode = mode;
    }

    public String getGeneralObjetive() {
        return generalObjetive;
    }

    public void setGeneralObjetive(String generalObjetive) {
        this.generalObjetive = generalObjetive;
    }

    public String getSpecificObjetives() {
        return specificObjetives;
    }

    public void setSpecificObjetives(String specificObjetives) {
        this.specificObjetives = specificObjetives;
    }

    public String getArchivoPDF() {
        return archivoPDF;
    }

    public void setArchivoPDF(String archivoPDF) {
        this.archivoPDF = archivoPDF;
    }

    public String getCartaLaboral() {
        return cartaLaboral;
    }

    public void setCartaLaboral(String cartaLaboral) {
        this.cartaLaboral = cartaLaboral;
    }

    public EnumEstado getState() {
        return state;
    }

    public void setState(EnumEstado state) {
        this.state = state;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public Long getIdFormatoA() {
        return idFormatoA;
    }

    public void setIdFormatoA(Long idFormatoA) {
        this.idFormatoA = idFormatoA;
    }
}