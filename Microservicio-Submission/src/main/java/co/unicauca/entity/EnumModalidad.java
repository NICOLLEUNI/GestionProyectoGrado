package co.unicauca.entity;

public enum EnumModalidad {

    INVESTIGACION("Investigación"),
    PRACTICA_PROFESIONAL("Práctica profesional"),
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
