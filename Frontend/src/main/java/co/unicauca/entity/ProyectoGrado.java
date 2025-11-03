package co.unicauca.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProyectoGrado {

    private long id;
    private String nombre;
    private LocalDateTime fechaCreacion;
    private List<String> estudiantesEmail;
    private FormatoA formatoAActual;
    private List<FormatoAVersion> historialFormatosA = new ArrayList<>();
    private String estado;
    private Anteproyecto anteproyecto;

    public ProyectoGrado(long id, String nombre, LocalDateTime fechaCreacion, List<String> estudiantesEmail, FormatoA formatoAActual, List<FormatoAVersion> historialFormatosA, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
        this.estudiantesEmail = estudiantesEmail;
        this.formatoAActual = formatoAActual;
        this.historialFormatosA = historialFormatosA;
        this.estado = estado;
    }

    public ProyectoGrado(long id, String nombre, LocalDateTime fechaCreacion, List<String> estudiantesEmail, FormatoA formatoAActual, List<FormatoAVersion> historialFormatosA, String estado, Anteproyecto anteproyecto) {
        this.id = id;
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
        this.estudiantesEmail = estudiantesEmail;
        this.formatoAActual = formatoAActual;
        this.historialFormatosA = historialFormatosA;
        this.estado = estado;
        this.anteproyecto = anteproyecto;
    }

    public ProyectoGrado() {}

    // Getters y Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public List<String> getEstudiantesEmail() {
        return estudiantesEmail;
    }

    public void setEstudiantesEmail(List<String> estudiantesEmail) {
        this.estudiantesEmail = estudiantesEmail;
    }

    public FormatoA getFormatoAActual() {
        return formatoAActual;
    }

    public void setFormatoAActual(FormatoA formatoAActual) {
        this.formatoAActual = formatoAActual;
    }

    public List<FormatoAVersion> getHistorialFormatosA() {
        return historialFormatosA;
    }

    public void setHistorialFormatosA(List<FormatoAVersion> historialFormatosA) {
        this.historialFormatosA = historialFormatosA;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Anteproyecto getAnteproyecto() {
        return anteproyecto;
    }

    public void setAnteproyecto(Anteproyecto anteproyecto) {
        this.anteproyecto = anteproyecto;
    }
}