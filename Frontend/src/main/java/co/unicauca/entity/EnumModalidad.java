package co.unicauca.entity;

import com.google.gson.annotations.SerializedName;

public enum EnumModalidad {

    @SerializedName("INVESTIGACION")
    INVESTIGACION("Investigación"),

    @SerializedName("PRACTICA_PROFESIONAL")
    PRACTICA_PROFESIONAL("Práctica profesional"),
    @SerializedName("PLAN_COTERMINAL")
    PLAN_COTERMINAL("Plan coterminal");

    private final String descripcion;

    EnumModalidad(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }

}
