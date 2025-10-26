package co.unicauca.entity;

public enum EnumEstadoAnteproyecto {

    ENTREGADO("ENTREGADO"),
    APROBADO("APROBADO"),
    RECHAZADO("RECHAZADO");

    private final String descripcion;

    EnumEstadoAnteproyecto(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }


}
