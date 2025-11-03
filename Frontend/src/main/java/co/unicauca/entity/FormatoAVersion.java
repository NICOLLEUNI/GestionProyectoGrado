package co.unicauca.entity;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDate;

public class FormatoAVersion {
    private Long id;


    @SerializedName("numVersion")
    private Integer numeroVersion;

    private LocalDate fecha;

    @SerializedName("titulo")

    private String title;

    @SerializedName("modalidad")
    private String modalidadString;

    @SerializedName("estado")
    private String estadoString;

    @SerializedName("observaciones")
    private String observations;

    private Integer counter;

    @SerializedName("idFormatoA")
    private Long idFormatoA;

    // ✅ CONSTRUCTOR SIN PARÁMETROS (OBLIGATORIO PARA GSON)
    public FormatoAVersion() {
        // Constructor vacío necesario para Gson
    }

    // Constructor con todos los parámetros (opcional, para tu uso interno)
    public FormatoAVersion(Long id, int numeroVersion, LocalDate fecha, String title,
                           EnumModalidad mode, String generalObjetive, String specificObjetives,
                           String archivoPDF, String cartaLaboral, EnumEstado state,
                           String observations, int counter, Long idFormatoA) {
        this.id = id;
        this.numeroVersion = numeroVersion;
        this.fecha = fecha;
        this.title = title;
        // Para el constructor manual, puedes asignar directamente los enums
        this.modalidadString = (mode != null) ? mode.name() : null;
        this.estadoString = (state != null) ? state.name() : null;
        this.observations = observations;
        this.counter = counter;
        this.idFormatoA = idFormatoA;
    }

    // ✅ MÉTODOS PARA CONVERTIR LOS STRINGS A ENUMS
    public EnumModalidad getMode() {
        if (modalidadString != null) {
            try {
                return EnumModalidad.valueOf(modalidadString);
            } catch (IllegalArgumentException e) {
                System.err.println("❌ Modalidad no reconocida: " + modalidadString);
                return null;
            }
        }
        return null;
    }

    public EnumEstado getState() {
        if (estadoString != null) {
            try {
                return EnumEstado.valueOf(estadoString);
            } catch (IllegalArgumentException e) {
                System.err.println("❌ Estado no reconocido: " + estadoString);
                return null;
            }
        }
        return null;
    }

    // Getters y Setters (todos deben estar presentes)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getNumeroVersion() { return numeroVersion; }
    public void setNumeroVersion(Integer numeroVersion) { this.numeroVersion = numeroVersion; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getModalidadString() { return modalidadString; }
    public void setModalidadString(String modalidadString) { this.modalidadString = modalidadString; }

    public String getEstadoString() { return estadoString; }
    public void setEstadoString(String estadoString) { this.estadoString = estadoString; }

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }

    public Integer getCounter() { return counter; }
    public void setCounter(Integer counter) { this.counter = counter; }

    public Long getIdFormatoA() { return idFormatoA; }
    public void setIdFormatoA(Long idFormatoA) { this.idFormatoA = idFormatoA; }
}