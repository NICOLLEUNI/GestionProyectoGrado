package co.unicauca.service;

public class FormatoAVersionService {

    // SOLO URLs - sin lógica
    private static final String BASE_URL = "http://localhost:8082/api/formatos-a";

    public static final String LISTAR_VERSIONES = BASE_URL + "/versiones";

    public static String obtenerVersion(Long id) {
        return BASE_URL + "/versiones/" + id;
    }

    public static String obtenerHistorial(Long id) {
        return BASE_URL + "/versiones/" + id + "/historial";
    }
}
