package co.unicauca.infra.dto;

import co.unicauca.entity.Anteproyecto;
import co.unicauca.entity.ProyectoGrado;
import co.unicauca.entity.FormatoAVersion;

public record RelacionCompletaResponse(
        Anteproyecto anteproyecto,
        ProyectoGrado proyecto,
        FormatoAVersion formatoA
) {}