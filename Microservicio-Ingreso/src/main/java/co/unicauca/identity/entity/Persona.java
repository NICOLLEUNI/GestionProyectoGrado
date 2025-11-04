package co.unicauca.identity.entity;

import co.unicauca.identity.enums.EnumDepartamento;
import co.unicauca.identity.enums.EnumPrograma;
import co.unicauca.identity.enums.enumRol;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Set;

/**
 * Entidad Persona - SINGLE_TABLE strategy para manejar múltiples roles en una sola tabla
 * JPA Compliant - Estrategia estándar y optimizada
 */
@Entity
@Table(name = "personas")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private Long idUsuario;

    @Column(name = "name", nullable = false, length = 100)
    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{2,}$",
            message = "El nombre debe contener solo letras y tener al menos 2 caracteres")
    private String name;

    @Column(name = "lastname", nullable = false, length = 100)
    @NotBlank(message = "Los apellidos son obligatorios")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]{2,}$",
            message = "Los apellidos deben contener solo letras y tener al menos 2 caracteres")
    private String lastname;

    @Column(name = "phone", length = 20)
    @Pattern(regexp = "^[0-9]{10}$",
            message = "El celular debe tener 10 dígitos numéricos")
    private String phone;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "programa", length = 100)
    private EnumPrograma programa;

    @Enumerated(EnumType.STRING)
    @Column(name = "departamento", length = 50)
    private EnumDepartamento departamento;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "persona_roles",
            joinColumns = @JoinColumn(name = "idUsuario")
    )
    @Column(name = "rol")
    @NotNull(message = "Los roles son obligatorios")
    private Set<enumRol> roles = EnumSet.noneOf(enumRol.class);

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Persona() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Persona(String name, String lastname, String phone, String email, String password) {
        this();
        this.name = name;
        this.lastname = lastname;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    public Persona(String name, String lastname, String phone, String email,
                   String password, Set<enumRol> roles, EnumPrograma programa,
                   EnumDepartamento departamento) {
        this();
        this.name = name;
        this.lastname = lastname;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.roles = roles != null ? roles : EnumSet.noneOf(enumRol.class);
        this.programa = programa;
        this.departamento = departamento;
    }

    // Getters y Setters (mantener todos los existentes)
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public EnumPrograma getPrograma() { return programa; }
    public void setPrograma(EnumPrograma programa) { this.programa = programa; }
    public EnumDepartamento getDepartamento() { return departamento; }
    public void setDepartamento(EnumDepartamento departamento) { this.departamento = departamento; }
    public Set<enumRol> getRoles() { return roles; }
    public void setRoles(Set<enumRol> roles) {
        this.roles = roles != null ? roles : EnumSet.noneOf(enumRol.class);
        this.updatedAt = LocalDateTime.now();
    }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Métodos de utilidad para roles
    public void addRol(enumRol rol) {
        this.roles.add(rol);
        this.updatedAt = LocalDateTime.now();
    }

    public void removeRol(enumRol rol) {
        this.roles.remove(rol);
        this.updatedAt = LocalDateTime.now();
    }

    public boolean tieneRol(enumRol rol) {
        return this.roles.contains(rol);
    }

    // Métodos de negocio
    public boolean esEstudiante() {
        return this.roles.contains(enumRol.ESTUDIANTE);
    }

    public boolean requiereDepartamento() {
        return this.roles.contains(enumRol.DOCENTE) ||
                this.roles.contains(enumRol.JEFE_DEPARTAMENTO);
    }

    // ✅ CORREGIDO: Método para verificar si requiere programa
    public boolean requierePrograma() {
        return this.roles.contains(enumRol.ESTUDIANTE) ||
                this.roles.contains(enumRol.COORDINADOR);
    }


    // ✅ CORREGIDO: Método para verificar combinaciones
    public boolean tieneCombinacionRoles() {
        return requierePrograma() && requiereDepartamento();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Persona)) return false;
        Persona persona = (Persona) o;
        return idUsuario != null && idUsuario.equals(persona.idUsuario);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Persona{" +
                "idUsuario=" + idUsuario +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", programa=" + programa +
                ", departamento=" + departamento +
                ", roles=" + roles +
                '}';
    }
}