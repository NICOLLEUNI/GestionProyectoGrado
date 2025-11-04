package co.unicauca.entity;


import co.unicauca.entity.state.FormatoAState;
import co.unicauca.entity.state.FormatoAStateFactory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table (name = "formatoA")
public class FormatoA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ NUEVO: Campo transitorio para el patrón State (no se persiste)
    @Transient
    @JsonIgnore
    private FormatoAState stateObject;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumModalidad mode;

    // CORREO del docente director
    //Deberia ser de tipo persona? No, yo no necesito el acoplamiento, solo envio los correos
    @Column(nullable = false)
    private String projectManagerEmail;

    // CORREO del codirector (opcional)
    private String projectCoManagerEmail;

    @Column(nullable = false)
    private LocalDate date;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String generalObjetive;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String specificObjetives;

    @Column(nullable = false)
    private String archivoPDF;

    private String cartaLaboral;

    // CORREOS de los estudiantes asociados
    @ElementCollection
    @CollectionTable(name = "formato_a_estudiantes", joinColumns = @JoinColumn(name = "formato_a_id"))
    @Column(name = "estudiante_email")
    private List<String> estudianteEmails = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumEstado state = EnumEstado.ENTREGADO;

    @Column(columnDefinition = "TEXT")
    private String observations;

    private int counter;

    // ✅ NUEVO: Método para obtener el state object
    public FormatoAState getStateObject() {
        if (stateObject == null) {
            this.stateObject = FormatoAStateFactory.createState(this.state);
        }
        return stateObject;
    }

    // ✅ NUEVO: Métodos delegados al state object
    public void evaluar(String observaciones) {
        FormatoAState nuevoEstado = getStateObject().evaluar(this, observaciones);
        transitionToState(nuevoEstado);
    }

    public void aprobar() {
        FormatoAState nuevoEstado = getStateObject().aprobar(this);
        transitionToState(nuevoEstado);
    }

    public void rechazar(String observaciones) {
        FormatoAState nuevoEstado = getStateObject().rechazar(this, observaciones);
        transitionToState(nuevoEstado);
    }

    public void reenviar() {
        FormatoAState nuevoEstado = getStateObject().reenviar(this);
        transitionToState(nuevoEstado);
    }

    // ✅ NUEVO: Métodos de consulta delegados
    public boolean puedeEditar() {
        return getStateObject().puedeEditar();
    }

    public boolean puedeReenviar() {
        return getStateObject().puedeReenviar();
    }

    public boolean puedeEvaluar() {
        return getStateObject().puedeEvaluar();
    }

    // ✅ NUEVO: Transición de estado interna
    private void transitionToState(FormatoAState nuevoEstado) {
        this.stateObject = nuevoEstado;
        this.state = nuevoEstado.toEnumState();
        nuevoEstado.onEnterState(this);
    }

    public FormatoA(Long id, String title, EnumModalidad mode, String projectManagerEmail, String projectCoManagerEmail, LocalDate date, String generalObjetive, String specificObjetives, String archivoPDF, String cartaLaboral, List<String> estudianteEmails, EnumEstado state, String observations, int counter) {
        this.id = id;
        this.title = title;
        this.mode = mode;
        this.projectManagerEmail = projectManagerEmail;
        this.projectCoManagerEmail = projectCoManagerEmail;
        this.date = date;
        this.generalObjetive = generalObjetive;
        this.specificObjetives = specificObjetives;
        this.archivoPDF = archivoPDF;
        this.cartaLaboral = cartaLaboral;
        this.estudianteEmails = estudianteEmails;
        this.state = state;
        this.observations = observations;
        this.counter = counter;
    }

    public FormatoA() {}
}
