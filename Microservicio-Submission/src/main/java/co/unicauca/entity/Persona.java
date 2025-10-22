package co.unicauca.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table (name = "persona")
public class Persona {

    @Id
    private long id;

    private String name;
    private String lastname;
    private  String email;
    private String password;
    private List<String> roles;

}
