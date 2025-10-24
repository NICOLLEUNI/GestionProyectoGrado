    package co.unicauca.mapper;

    import co.unicauca.dto.FormatoAVersionRequest;
    import co.unicauca.entity.FormatoAVersionEntity;
    import co.unicauca.entity.FormatoAEntity;

    public class FormatoAVersionMapper {

        /**
         * Convierte DTO de versión en entidad, asociando al FormatoA correspondiente
         */
        public static FormatoAVersionEntity fromRequest(FormatoAVersionRequest dto, FormatoAEntity formatoA) {
            if (dto == null) return null;

            return FormatoAVersionEntity.builder()
                    .id(dto.getId())
                    .numVersion(dto.getNumVersion())
                    .fecha(dto.getFecha())
                    .estado(dto.getEstado())
                    .observaciones(dto.getObservaciones())
                    .formatoA(formatoA) // vínculo con el formato principal
                    .build();
        }
    }
