package co.unicauca.service;

import co.unicauca.entity.Persona;
import co.unicauca.entity.ProyectoGrado;
import co.unicauca.entity.Anteproyecto;
import co.unicauca.entity.FormatoA;
import co.unicauca.entity.FormatoAVersion;
import co.unicauca.infra.Subject;
import co.unicauca.utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Service exclusivo para llamadas HTTP a los microservicios de estudiante.
 */
public class EstudianteService extends Subject {

    private final Gson gson = new Gson();

    private final String PERSONA_URL = "http://localhost:8081/api/personas";
    private final String PROYECTO_URL = "http://localhost:8082/api/proyectos-grado";
    private final String ANTEPROYECTO_URL = "http://localhost:8083/api/anteproyectos";
    private final String FORMATO_A_URL = "http://localhost:8084/api/formatos-a";

    /**
     * Obtiene la información de una persona (estudiante) por su email.
     */
    public Persona findPersonaByEmail(String email) {
        try {
            String url = PERSONA_URL + "/email/" + email;
            String jsonResponse = HttpUtil.get(url);
            return gson.fromJson(jsonResponse, Persona.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene el proyecto de grado de un estudiante por su email.
     */
    public ProyectoGrado findProyectoByEstudiante(String email) {
        try {
            String url = PROYECTO_URL + "/estudiante/" + email;
            String jsonResponse = HttpUtil.get(url);
            return gson.fromJson(jsonResponse, ProyectoGrado.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene el anteproyecto asociado a un proyecto de grado por su ID.
     */
    public Anteproyecto findAnteproyectoByProyectoId(Long proyectoId) {
        try {
            String url = ANTEPROYECTO_URL + "/proyecto/" + proyectoId;
            String jsonResponse = HttpUtil.get(url);
            return gson.fromJson(jsonResponse, Anteproyecto.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene todas las versiones del Formato A de un FormatoA dado por ID.
     */
    public List<FormatoAVersion> listFormatosAVersion(Long formatoAId) {
        try {
            String url = FORMATO_A_URL + "/versiones/formato/" + formatoAId;
            String jsonResponse = HttpUtil.get(url);
            Type listType = new TypeToken<List<FormatoAVersion>>() {}.getType();
            return gson.fromJson(jsonResponse, listType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene directamente el FormatoA actual asociado a un proyecto de grado.
     */
    public FormatoA findFormatoAByProyectoId(Long proyectoId) {
        try {
            String url = FORMATO_A_URL + "/proyecto/" + proyectoId;
            String jsonResponse = HttpUtil.get(url);
            return gson.fromJson(jsonResponse, FormatoA.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene todos los FormatoA asociados a un estudiante (por email).
     */
    public List<FormatoA> listFormatosAByEstudiante(String email) {
        try {
            String url = FORMATO_A_URL + "/estudiante/" + email;
            String jsonResponse = HttpUtil.get(url);
            Type listType = new TypeToken<List<FormatoA>>() {}.getType();
            return gson.fromJson(jsonResponse, listType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
