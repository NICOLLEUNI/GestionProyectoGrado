package co.unicauca.domain.entities;

import java.util.Collections;
import java.util.Set;
import java.util.regex.Pattern;

public class Persona {
    private Long idUsuario;
    private String name;
    private String lastname;
    private String email;
    private String department;
    private String programa;
    private Set<EnumRol> roles;

    // Patrón para validación de email
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    // Constructor principal con validaciones
    public Persona(Long idUsuario, String name, String lastname, String email,
                   String department, String programa, Set<EnumRol> roles) {
        this.setIdUsuario(idUsuario);
        this.setName(name);
        this.setLastname(lastname);
        this.setEmail(email);
        this.setDepartment(department);
        this.setPrograma(programa);
        this.setRoles(roles);
    }

    // Constructor protegido para JPA/frameworks
    protected Persona() {}

    // Método de actualización controlado
    public void actualizarDatos(String name, String lastname, String email,
                                String department, String programa, Set<EnumRol> roles) {
        this.setName(name);
        this.setLastname(lastname);
        this.setEmail(email);
        this.setDepartment(department);
        this.setPrograma(programa);
        this.setRoles(roles);
    }

    // Getters
    public Long getIdUsuario() {
        return idUsuario;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    public String getPrograma() {
        return programa;
    }

    public Set<EnumRol> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    // Setters con validaciones
    public void setIdUsuario(Long idUsuario) {
        if (idUsuario == null || idUsuario <= 0) {
            throw new IllegalArgumentException("El ID de usuario debe ser válido y mayor a 0");
        }
        this.idUsuario = idUsuario;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.name = name.trim();
    }

    public void setLastname(String lastname) {
        if (lastname == null || lastname.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }
        this.lastname = lastname.trim();
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }

        String trimmedEmail = email.trim();
        if (!EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            throw new IllegalArgumentException("El formato del email no es válido");
        }

        this.email = trimmedEmail;
    }

    public void setDepartment(String department) {
        // Departamento puede ser null (opcional)
        this.department = department != null ? department.trim() : null;
    }

    public void setPrograma(String programa) {
        // Programa puede ser null (opcional)
        this.programa = programa != null ? programa.trim() : null;
    }

    public void setRoles(Set<EnumRol> roles) {
        this.roles = roles != null ? Set.copyOf(roles) : Collections.emptySet();
    }

    // Comportamiento de negocio
    public boolean tieneRol(EnumRol rol) {
        return roles.contains(rol);
    }

    // Métodos de igualdad basados en identidad (idUsuario)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Persona persona = (Persona) o;
        return java.util.Objects.equals(idUsuario, persona.idUsuario);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(idUsuario);
    }

}