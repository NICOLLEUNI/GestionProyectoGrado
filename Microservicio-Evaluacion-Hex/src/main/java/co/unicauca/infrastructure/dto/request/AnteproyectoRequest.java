package co.unicauca.infrastructure.dto.request;


import java.time.LocalDate;

public record AnteproyectoRequest(
        Long id,
        String titulo,
        LocalDate fecha,
        String estado,
        Long idProyectoGrado
) {
}

