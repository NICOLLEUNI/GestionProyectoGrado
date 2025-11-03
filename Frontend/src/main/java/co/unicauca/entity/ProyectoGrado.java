package co.unicauca.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProyectoGrado {

    private Long id;
    private String nombre;

    private LocalDate fecha;

    private List<String> estudiantesEmail;
    private FormatoA formatoAActual;
    private Long IdFormatoA;
    private List<FormatoAVersion> historialFormatosA = new ArrayList<>();
    private String estado;
    private Anteproyecto anteproyecto;

}