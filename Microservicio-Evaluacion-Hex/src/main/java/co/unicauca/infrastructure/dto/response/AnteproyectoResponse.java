package co.unicauca.infrastructure.dto.response;
import java.time.LocalDate;

public record AnteproyectoResponse (
        Long id,
        String titulo,
        LocalDate fecha,
        String estado,
        Long idProyectoGrado
){}
