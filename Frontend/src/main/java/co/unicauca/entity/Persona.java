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

    public Persona(Long idUsuario, String name, String lastname, String email, String department, String programa, Set<EnumRol> roles) {
        this.idUsuario = idUsuario;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.department = department;
        this.programa = programa;
        this.roles = roles;
    }
    
    

}