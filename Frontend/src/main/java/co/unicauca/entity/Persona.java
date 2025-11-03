/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class Persona {

    private Long idUsuario;
    private String name;
    private String lastname;
    private String email;
    private String department;
    private String programa;
    private Set<EnumRol> roles;
    private String phone;

    // ✅ AGREGAR: Constructor vacío para JSON
    public Persona() {}

    public Persona(Long idUsuario, String name, String lastname, String email, String department, String programa, Set<EnumRol> roles, String phone) {
        this.idUsuario = idUsuario;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.department = department;
        this.programa = programa;
        this.roles = roles;
        this.phone = phone;
    }


    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

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

    //Para mostrar en el front los correos y no la entidad completa
    @Override
    public String toString() {
        return this.email; // o cualquier atributo que quieras mostrar en el combo
    }

}