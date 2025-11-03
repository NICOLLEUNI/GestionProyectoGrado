package co.unicauca.infra;

import co.unicauca.entity.EnumEstado;
import co.unicauca.entity.Persona;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoFormatoA {

        private Long id;
        private String title;
        private String mode;
        private Persona projectManager;
        private Persona projectCoManager;
        private List<Persona> estudiantes;
        private String generalObjetive;
        private String specificObjetives;
        private String archivoPDF;
        private String cartaLaboral;
        private int counter;
        private EnumEstado state;
        private String observations;

}
