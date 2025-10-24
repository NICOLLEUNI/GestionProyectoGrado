package co.unicauca.mapper;

import co.unicauca.dto.FormatoARequest;
import co.unicauca.entity.FormatoAEntity;

public class FormatoAMapper {

    public static FormatoAEntity fromRequest(FormatoARequest dto) {
        return FormatoAEntity.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .mode(dto.getMode())
                .projectManagerEmail(dto.getProjectManagerEmail())
                .projectCoManagerEmail(dto.getProjectCoManagerEmail())
                .generalObjetive(dto.getGeneralObjetive())
                .specificObjetives(dto.getSpecificObjetives())
                .archivoPDF(dto.getArchivoPDF())
                .cartaLaboral(dto.getCartaLaboral())
                .estudiantes(dto.getEstudiante())
                .counter(dto.getCounter())
                .build();
    }
}
