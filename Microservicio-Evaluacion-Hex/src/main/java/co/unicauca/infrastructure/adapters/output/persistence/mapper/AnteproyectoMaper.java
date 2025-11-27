package co.unicauca.infrastructure.adapters.output.persistence.mapper;

import co.unicauca.domain.entities.Anteproyecto;
import co.unicauca.infrastructure.adapters.output.persistence.entities.AnteproyectoEntity;

public class AnteproyectoMaper {
    public static AnteproyectoEntity toAnteproyectoEntity(Anteproyecto ante) {
        AnteproyectoEntity entity = new AnteproyectoEntity();
        entity.setId(ante.getId());
        entity.setTitulo(ante.getTitulo());
        entity.setFechaCreacion(ante.getFechaCreacion());
        entity.setArchivoPDF(ante.getArchivoPDF());
        entity.setEstado(ante.getEstado());
        entity.setIdProyectoGrado(ante.getIdProyectoGrado());
        entity.setEmailEvaluador1(ante.getEmailEvaluador1());
        entity.setEmailEvaluador2(ante.getEmailEvaluador2());
        return entity;
    }


    public static Anteproyecto toAnteproyecto(AnteproyectoEntity entity) {
        return new Anteproyecto(
                entity.getId(),
                entity.getTitulo(),
                entity.getFechaCreacion(),
                entity.getArchivoPDF(),
                entity.getEstado(),
                entity.getIdProyectoGrado(),
                entity.getEmailEvaluador1(),
                entity.getEmailEvaluador2()
        );
    }
}
