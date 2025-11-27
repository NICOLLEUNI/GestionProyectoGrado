package co.unicauca.infrastructure.adapters.output.persistence.mapper;

import co.unicauca.domain.entities.FormatoA;
import co.unicauca.infrastructure.adapters.output.persistence.entities.FormatoAEntity;

import java.util.ArrayList;
import java.util.stream.Collectors;


public class FormatoAMaper {

    // =============================
    //        DOMAIN → ENTITY
    // =============================
    public static FormatoAEntity toEntity(FormatoA formato) {
        if (formato == null) return null;

        FormatoAEntity entity = new FormatoAEntity();

        entity.setId(formato.getId());
        entity.setTitle(formato.getTitle());
        entity.setMode(formato.getMode());
        entity.setProjectManager(PersonaMapper.toEntity(formato.getProjectManager()));
        entity.setProjectCoManager(PersonaMapper.toEntity(formato.getProjectCoManager()));
        entity.setGeneralObjective(formato.getGeneralObjective());
        entity.setSpecificObjectives(formato.getSpecificObjectives());
        entity.setArchivoPDF(formato.getArchivoPDF());
        entity.setCartaLaboral(formato.getCartaLaboral());
        entity.setCounter(formato.getCounter());
        entity.setState(formato.getState());
        entity.setObservations(formato.getObservations());

        entity.setEstudiantes(
                formato.getEstudiantes() == null
                        ? new ArrayList<>()
                        : formato.getEstudiantes()
                        .stream()
                        .map(PersonaMapper::toEntity)
                        .collect(Collectors.toList())
        );

        return entity;
    }

    // =============================
    //        ENTITY → DOMAIN
    // =============================
    public static FormatoA toDomain(FormatoAEntity entity) {
        if (entity == null) return null;

        return new FormatoA(
                entity.getId(),
                entity.getTitle(),
                entity.getMode(),
                PersonaMapper.toDomain(entity.getProjectManager()),
                PersonaMapper.toDomain(entity.getProjectCoManager()),
                entity.getGeneralObjective(),
                entity.getSpecificObjectives(),
                entity.getArchivoPDF(),
                entity.getCartaLaboral(),
                entity.getCounter(),
                (entity.getEstudiantes() == null)
                        ? new ArrayList<>()
                        : entity.getEstudiantes()
                        .stream()
                        .map(PersonaMapper::toDomain)
                        .collect(Collectors.toList()),
                entity.getState(),
                entity.getObservations()
        );
    }

}
