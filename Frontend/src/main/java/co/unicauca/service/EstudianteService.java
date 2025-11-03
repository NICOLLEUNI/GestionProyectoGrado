package co.unicauca.service;

import co.unicauca.entity.Persona;
import co.unicauca.entity.ProyectoGrado;
import co.unicauca.entity.Anteproyecto;
import co.unicauca.entity.FormatoAVersion;
import co.unicauca.infra.Subject;
import co.unicauca.utils.HttpUtil;
import co.unicauca.utils.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Service exclusivo para llamadas HTTP a los microservicios de estudiante.
 */
public class EstudianteService extends Subject {

    private final Gson gson = GsonFactory.create();
    private final String BASE_URL = "http://localhost:8083/api";

    /**
     * ✅ CORREGIDO: Obtiene el FormatoAVersion actual de un proyecto
     */
    public FormatoAVersion findFormatoAVersionByProyectoId(Long proyectoId) {
        try {
            // 🔹 USAR EL ENDPOINT CORRECTO: desde ProyectoController, no FormatoAController
            String url = BASE_URL + "/proyectos-grado/" + proyectoId + "/formato-a";
            System.out.println("🔗 Buscando FormatoAVersion por proyecto: " + url);

            String jsonResponse = HttpUtil.get(url);
            System.out.println("📦 Respuesta FormatoAVersion: " + jsonResponse);

            if (jsonResponse == null || jsonResponse.trim().isEmpty()) {
                System.out.println("⚠️ No se encontró FormatoAVersion para el proyecto: " + proyectoId);
                return null;
            }

            return gson.fromJson(jsonResponse, FormatoAVersion.class);

        } catch (Exception e) {
            System.err.println("❌ Error buscando FormatoAVersion: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ✅ NUEVO MÉTODO: Obtiene FormatoAVersion por email del estudiante
     */
    public FormatoAVersion findFormatoAVersionByEstudiante(String email) {
        try {
            // 🔹 USAR ENDPOINT POR ESTUDIANTE
            String url = BASE_URL + "/proyectos-grado/estudiante/" + email + "/formato-a";
            System.out.println("🔗 Buscando FormatoAVersion por estudiante: " + url);

            String jsonResponse = HttpUtil.get(url);
            System.out.println("📦 Respuesta FormatoAVersion: " + jsonResponse);

            if (jsonResponse == null || jsonResponse.trim().isEmpty()) {
                System.out.println("⚠️ No se encontró FormatoAVersion para el estudiante: " + email);
                return null;
            }

            return gson.fromJson(jsonResponse, FormatoAVersion.class);

        } catch (Exception e) {
            System.err.println("❌ Error buscando FormatoAVersion por estudiante: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ✅ MÉTODO EXISTENTE - Ya está correcto
     */
    public List<FormatoAVersion> listFormatosAVersion(Long formatoAId) {
        try {
            String url = BASE_URL + "/formatos-a/versiones/formato/" + formatoAId;
            System.out.println("🔗 Llamando a: " + url);

            String jsonResponse = HttpUtil.get(url);
            System.out.println("📦 JSON recibido: " + jsonResponse);

            if (jsonResponse == null || jsonResponse.trim().isEmpty()) {
                System.out.println("⚠️ Respuesta vacía");
                return new ArrayList<>();
            }

            if (jsonResponse.trim().startsWith("[")) {
                Type listType = new TypeToken<List<FormatoAVersion>>() {}.getType();
                List<FormatoAVersion> versiones = gson.fromJson(jsonResponse, listType);
                System.out.println("✅ Array parseado - Versiones: " + versiones.size());
                return versiones;
            } else {
                System.out.println("⚠️ Se recibió objeto en lugar de array, intentando parsear como objeto único");
                try {
                    FormatoAVersion version = gson.fromJson(jsonResponse, FormatoAVersion.class);
                    List<FormatoAVersion> lista = new ArrayList<>();
                    if (version != null) {
                        lista.add(version);
                        System.out.println("✅ Objeto único convertido a lista - 1 versión");
                    }
                    return lista;
                } catch (Exception e) {
                    System.err.println("❌ Error parseando objeto único: " + e.getMessage());
                    return new ArrayList<>();
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Error en listFormatosAVersion: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * ✅ ELIMINAR: Este método ya no es necesario porque no tienes entidad FormatoA
     */
    // public FormatoA findFormatoAByProyectoId(Long proyectoId) { ... }

    /**
     * ✅ ELIMINAR: Este método ya no es necesario
     */
    // public List<FormatoA> listFormatosAByEstudiante(String email) { ... }

    // 🔹 MANTENER LOS MÉTODOS QUE SÍ FUNCIONAN:

    public Persona findPersonaByEmail(String email) {
        try {
            String url = BASE_URL + "/personas/email/" + email;
            System.out.println("🔗 Buscando persona: " + url);
            String jsonResponse = HttpUtil.get(url);
            System.out.println("📦 Respuesta persona: " + jsonResponse);
            return gson.fromJson(jsonResponse, Persona.class);
        } catch (Exception e) {
            System.err.println("❌ Error buscando persona: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public ProyectoGrado findProyectoByEstudiante(String email) {
        try {
            String url = BASE_URL + "/proyectos-grado/estudiante/" + email;
            System.out.println("🔗 Buscando proyecto: " + url);
            String jsonResponse = HttpUtil.get(url);
            System.out.println("📦 Respuesta proyecto: " + jsonResponse);
            return gson.fromJson(jsonResponse, ProyectoGrado.class);
        } catch (Exception e) {
            System.err.println("❌ Error buscando proyecto: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Anteproyecto findAnteproyectoByProyectoId(Long proyectoId) {
        try {
            String url = BASE_URL + "/anteproyectos/proyecto/" + proyectoId;
            System.out.println("🔗 Buscando anteproyecto: " + url);

            String jsonResponse = HttpUtil.get(url);
            System.out.println("📦 JSON recibido de anteproyecto: " + jsonResponse);

            if (jsonResponse == null || jsonResponse.trim().isEmpty()) {
                System.out.println("⚠️ Respuesta vacía de anteproyecto");
                return null;
            }

            if (jsonResponse.trim().startsWith("[")) {
                System.out.println("⚠️ Se recibió array en lugar de objeto para anteproyecto");
                Type listType = new TypeToken<List<Anteproyecto>>() {}.getType();
                List<Anteproyecto> anteproyectos = gson.fromJson(jsonResponse, listType);

                if (anteproyectos != null && !anteproyectos.isEmpty()) {
                    System.out.println("✅ Tomando primer anteproyecto del array - Total: " + anteproyectos.size());
                    return anteproyectos.get(0);
                } else {
                    System.out.println("❌ Array de anteproyectos vacío");
                    return null;
                }
            } else {
                System.out.println("✅ Parseando objeto único de anteproyecto");
                return gson.fromJson(jsonResponse, Anteproyecto.class);
            }

        } catch (Exception e) {
            System.err.println("❌ Error en findAnteproyectoByProyectoId: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}