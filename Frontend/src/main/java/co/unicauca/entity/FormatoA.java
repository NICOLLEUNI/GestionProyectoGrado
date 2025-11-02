package co.unicauca.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FormatoA {

        private Long id;
        private String title;
        private EnumModalidad mode;
        private String projectManagerEmail;
        private String projectCoManagerEmail;
        private LocalDate date;
        private String generalObjetive;
        private String specificObjetives;
        private String archivoPDF;
        private String cartaLaboral;
        private List<String> estudianteEmails = new ArrayList<>();
        private EnumEstado state = EnumEstado.ENTREGADO;
        private String observations;
        private int counter;

        public FormatoA(Long id, String title, EnumModalidad mode, String projectManagerEmail, String projectCoManagerEmail, LocalDate date, String generalObjetive, String specificObjetives, String archivoPDF, String cartaLaboral, List<String> estudianteEmails, EnumEstado state, String observations, int counter) {
            this.id = id;
            this.title = title;
            this.mode = mode;
            this.projectManagerEmail = projectManagerEmail;
            this.projectCoManagerEmail = projectCoManagerEmail;
            this.date = date;
            this.generalObjetive = generalObjetive;
            this.specificObjetives = specificObjetives;
            this.archivoPDF = archivoPDF;
            this.cartaLaboral = cartaLaboral;
            this.estudianteEmails = estudianteEmails;
            this.state = state;
            this.observations = observations;
            this.counter = counter;
        }

        public FormatoA() {}
    }




