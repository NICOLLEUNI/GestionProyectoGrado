/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.entity;

import java.util.Set;


public class Persona {

    private Long idUsuario;
    private String name;
    private String lastname;
    private String email;
    private String department;
    private String programa;
    private Set<EnumRol> roles;

    // ✅ AGREGAR: Constructor vacío para JSON
    public Persona() {}

    public Persona(Long idUsuario, String name, String lastname, String email, String department, String programa, Set<EnumRol> roles) {
        this.idUsuario = idUsuario;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.department = department;
        this.programa = programa;
        this.roles = roles;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public Set<EnumRol> getRoles() {
        return roles;
    }

    public void setRoles(Set<EnumRol> roles) {
        this.roles = roles;

    }


    public boolean tieneRol(EnumRol rol) {
        return roles != null && roles.contains(rol);
    }

    // ✅ AGREGAR: Método para verificar roles por string
    public boolean hasRole(String roleName) {
        if (roles == null) return false;
        try {
            EnumRol rol = EnumRol.valueOf(roleName);
            return roles.contains(rol);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}