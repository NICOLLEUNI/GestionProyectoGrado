package co.unicauca.service;

import co.unicauca.entity.FormatoA;
import co.unicauca.entity.Persona;
import co.unicauca.utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio encargado de consumir los endpoints del microservicio Submission.
 */
public class SubmissionService {

    private final String BASE_URL = "http://localhost:8081/api"; // Microservicio Submission
    private final Gson gson = new Gson();

    // =====================================================
    // 🔹 MÉTODOS PARA FORMATO A
    // =====================================================

    /**
     * Obtiene todos los formatos A registrados.
     */
    public List<FormatoA> listFormatoA() {
        try {
            String url = BASE_URL + "/formatoA";
            String jsonResponse = HttpUtil.get(url);

            Type listType = new TypeToken<List<FormatoA>>() {}.getType();
            return gson.fromJson(jsonResponse, listType);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // mejor devolver lista vacía
        }
    }

    /**
     * Lista los FormatoA de un docente según su email.
     */
    public List<FormatoA> getFormatosPorDocente(String email) {
        try {
            String url = BASE_URL + "/docente/" + email;
            String jsonResponse = HttpUtil.get(url); // Aquí se hace GET al endpoint
            Type listType = new TypeToken<List<FormatoA>>() {}.getType();
            return gson.fromJson(jsonResponse, listType);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>(); // Retornamos lista vacía en caso de error
        }
    }

    /**
     * Crea un nuevo FormatoA en el microservicio submission.
     */
    public FormatoA createFormatoA(FormatoA formato) {
        try {
            String url = BASE_URL + "/formatoA";
            String jsonRequest = gson.toJson(formato);

            String jsonResponse = HttpUtil.post(url, jsonRequest);
            return gson.fromJson(jsonResponse, FormatoA.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Busca un FormatoA por su ID.
     */
    public FormatoA findFormatoAById(Long id) {
        try {
            String url = BASE_URL + "/formatoA/" + id;
            String jsonResponse = HttpUtil.get(url);

            return gson.fromJson(jsonResponse, FormatoA.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reenvía un FormatoA rechazado (actualización).
     */
    public FormatoA reenviarFormatoA(FormatoA formato) {
        try {
            String url = BASE_URL + "/formatoA/reenviar-rechazado";
            String jsonRequest = gson.toJson(formato);

            String jsonResponse = HttpUtil.put(url, jsonRequest);
            return gson.fromJson(jsonResponse, FormatoA.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Actualiza un FormatoA evaluado.
     */
    public FormatoA actualizarFormatoAEvaluado(FormatoA formato) {
        try {
            String url = BASE_URL + "/formatoA/evaluacion";
            String jsonRequest = gson.toJson(formato);

            String jsonResponse = HttpUtil.put(url, jsonRequest);
            return gson.fromJson(jsonResponse, FormatoA.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sube el PDF de un FormatoA y devuelve la ruta o JSON de respuesta
     */
    public String subirFormatoAPDF(Long id, File file) {
        try {
            String url = BASE_URL + "/formatoA/" + id + "/pdf";
            String respuesta = HttpUtil.postMultipart(url, file, "file");
            if (respuesta != null && !respuesta.isEmpty()) {
                System.out.println("✅ PDF subido correctamente. Respuesta: " + respuesta);
                return respuesta;
            } else {
                System.err.println("⚠️ Error al subir el PDF del FormatoA");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sube la carta laboral de un FormatoA y devuelve la ruta o JSON de respuesta
     */
    public String subirCartaLaboral(Long id, File file) {
        try {
            String url = BASE_URL + "/formatoA/" + id + "/carta-laboral";
            String respuesta = HttpUtil.postMultipart(url, file, "file");
            if (respuesta != null && !respuesta.isEmpty()) {
                System.out.println("✅ Carta laboral subida correctamente. Respuesta: " + respuesta);
                return respuesta;
            } else {
                System.err.println("⚠️ Error al subir la carta laboral del FormatoA");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Elimina un FormatoA
     */
    public boolean eliminarFormatoA(Long id) {
        try {
            String url = BASE_URL + "/formatoA/" + id;
            String response = HttpUtil.delete(url);
            return true; // Si llega aquí, la eliminación fue exitosa
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // =====================================================
    // 🔹 MÉTODOS PARA PERSONAS
    // =====================================================

    public List<Persona> listAllPersonas() {
        try {
            String url = BASE_URL + "/personas";
            String jsonResponse = HttpUtil.get(url);

            Type listType = new TypeToken<List<Persona>>() {}.getType();
            return gson.fromJson(jsonResponse, listType);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Persona> listPersonasByRol(String rol) {
        try {
            String url = BASE_URL + "/personas/rol/" + rol.toUpperCase();
            String jsonResponse = HttpUtil.get(url);

            Type listType = new TypeToken<List<Persona>>() {}.getType();
            return gson.fromJson(jsonResponse, listType);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public Persona findPersonaByEmail(String email) {
        try {
            String url = BASE_URL + "/personas/email/" + email;
            String jsonResponse = HttpUtil.get(url);

            return gson.fromJson(jsonResponse, Persona.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
