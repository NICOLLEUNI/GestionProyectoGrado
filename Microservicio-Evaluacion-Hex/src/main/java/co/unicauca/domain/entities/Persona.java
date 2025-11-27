package co.unicauca.domain.entities;

import java.util.Collections;
import java.util.Set;

public class Persona {
    private final Long idUsuario;
    private final String name;
    private final String lastname;
    private final String email;
    private final String department;
    private final String programa;
    private final Set<EnumRol> roles;

    public Persona(Long idUsuario, String name, String lastname, String email,
                   String department, String programa, Set<EnumRol> roles) {
        this.idUsuario = idUsuario;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.department = department;
        this.programa = programa;
        this.roles = roles != null ? Set.copyOf(roles) : Collections.emptySet();
    }

    // Getters
    public Long getIdUsuario() { return idUsuario; }
    public String getName() { return name; }
    public String getLastname() { return lastname; }
    public String getEmail() { return email; }
    public String getDepartment() { return department; }
    public String getPrograma() { return programa; }
    public Set<EnumRol> getRoles() { return Collections.unmodifiableSet(roles); }

    // Comportamiento de negocio que sí usamos
    public boolean tieneRol(EnumRol rol) {
        return roles.contains(rol);
    }
}
