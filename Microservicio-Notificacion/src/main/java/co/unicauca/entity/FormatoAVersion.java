package co.unicauca.entity;

import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FormatoAVersion {
   private Long versionId;
    private Long formatoAId;
    private int numeroVersion;
    private String estado;
    private List<String> estudiantesEmails; // ← Para buscar programa académico
    private String directorEmail;
}
