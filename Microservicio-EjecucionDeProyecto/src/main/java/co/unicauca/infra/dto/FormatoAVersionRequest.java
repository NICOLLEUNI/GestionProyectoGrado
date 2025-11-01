    package co.unicauca.infra.dto;

    import com.fasterxml.jackson.annotation.JsonAlias;

    import java.time.LocalDate;


    public record FormatoAVersionRequest(

            Long id,
            int numVersion,

            @JsonAlias({"date", "fecha"})
            LocalDate fecha,

            @JsonAlias({"title", "titulo"})
            String titulo,

            @JsonAlias({"mode", "modalidad"})
            String modalidad,

            @JsonAlias({"state", "estado"})
            String estado,

            String observaciones,
            @JsonAlias({"counter"})
            Integer counter,

            @JsonAlias({"IdFormatoA", "idFormatoA"})
            Long idFormatoA

    )
    {}
