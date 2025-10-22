package co.unicauca.entity;

public enum EnumEstado {
    ENTREGADO("ENTREGADO"),
    APROBADO("APROBADO"),
    RECHAZADO("RECHAZADO"),
    RECHAZADO_DEFINITIVAMENTE("RECHAZADO DEFINITIVAMENTE");

    private final String descripcion;

    EnumEstado(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
