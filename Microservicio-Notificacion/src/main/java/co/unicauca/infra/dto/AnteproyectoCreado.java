package co.unicauca.infra.dto;

import java.util.List;

public record AnteproyectoCreado (
        Long anteproyectoId,
        String titulo,
        String directorEmail // ← Para buscar departamento del docente
){
}
