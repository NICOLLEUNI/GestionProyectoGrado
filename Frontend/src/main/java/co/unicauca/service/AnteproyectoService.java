package co.unicauca.service;

public class AnteproyectoService {

    private static final String BASE_URL = "http://localhost:8081/api/anteproyectos";

    public static final String LISTAR_TODOS = BASE_URL;

    public static String obtenerPorId(Long id) {
        return BASE_URL + "/" + id;
    }

    public static String obtenerHistorial(Long id) {
        return BASE_URL + "/" + id + "/historial";
    }

    public static String buscarPorProyecto(Long proyectoId) {
        return BASE_URL + "/proyecto/" + proyectoId;
    }
}
