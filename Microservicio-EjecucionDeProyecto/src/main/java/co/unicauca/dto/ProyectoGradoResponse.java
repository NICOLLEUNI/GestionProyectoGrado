package co.unicauca.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class ProyectoGradoResponse {
    private Long id;
    private String titulo;
    private String estado;
    private List<PersonaResponse> personas;
    private AnteproyectoResponse anteproyecto;
    private List<FormatoAResponse> formatosA;

    @Data
    public static class PersonaResponse {
        private Long id;
        private String nombre;
        private String rol;
    }

    @Data
    public static class AnteproyectoResponse {
        private Long id;
        private String titulo;
        private String estado;
        private LocalDate fecha;
        private String observaciones;
    }

    @Data
    public static class FormatoAResponse {
        private Long id;
        private String titulo;
        private String estado;
        private String modalidad;
        private List<FormatoAVersionResponse> versiones;
    }

    @Data
    public static class FormatoAVersionResponse {
        private Long id;
        private int numVersion;
        private String estado;
        private LocalDate fecha;
        private String observaciones;
    }
}
