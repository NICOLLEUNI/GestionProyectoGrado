package co.unicauca.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Anteproyecto {

    private Long id;
    private String titulo;
    public LocalDate fecha;
    private String estado;
    private Long idProyectoGrado;
    private String rutaPdf;



}
