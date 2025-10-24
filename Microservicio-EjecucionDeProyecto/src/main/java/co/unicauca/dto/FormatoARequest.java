package co.unicauca.dto;

import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class FormatoARequest {

    private Long id;
    private String title;
    private String mode;
    private String projectManagerEmail;
    private String projectCoManagerEmail;
    private String generalObjetive;
    private String specificObjetives;
    private String archivoPDF;
    private String cartaLaboral;
    private List<String> estudiante;
    private int counter;
}
