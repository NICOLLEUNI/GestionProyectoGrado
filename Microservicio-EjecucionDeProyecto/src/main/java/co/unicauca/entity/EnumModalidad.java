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

    public static EnumModalidad fromDescripcion(String descripcion) {
        for (EnumModalidad modalidad : values()) {
            if (modalidad.getDescripcion().equalsIgnoreCase(descripcion)) {
                return modalidad;
            }
        }
        throw new IllegalArgumentException("No existe modalidad con descripción: " + descripcion);
    }
}
