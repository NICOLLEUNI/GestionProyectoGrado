package co.unicauca.service;

import co.unicauca.entity.Anteproyecto;
import co.unicauca.entity.FormatoA;
import co.unicauca.entity.Persona;
import co.unicauca.infra.Subject;
import co.unicauca.utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EvaluacionService extends Subject {
     private final String BASE_URL = "http://localhost:8080/api/formatoA"; // o tu gateway
     private final Gson gson = new Gson();

     /**
      * Obtiene todos los formatos A desde el microservicio.
      * @return lista de objetos FormatoA
      */
     public List<FormatoA> listFormatoA() {
         try {
             // 1️⃣ Petición GET al microservicio
             String jsonResponse = HttpUtil.get(BASE_URL);


             // 2️⃣ Convertir JSON a lista de FormatoA
             Type listType = new TypeToken<List<FormatoA>>() {}.getType();
             List<FormatoA> lista = gson.fromJson(jsonResponse, listType);

             // 4️⃣ Retornar la lista para uso adicional
             return lista;

         } catch (Exception e) {
             e.printStackTrace();
             return null;
         }
     }

    public FormatoA findById(int id) {
        try {
            // 1️⃣ Armar la URL del microservicio
            String url = BASE_URL + "/" + id;

            // 2️⃣ Hacer petición GET con HttpUtil
            String jsonResponse = HttpUtil.get(url);

            // 3️⃣ Convertir JSON a objeto FormatoA
            FormatoA formato = gson.fromJson(jsonResponse, FormatoA.class);

            // 4️⃣ Retornar el FormatoA obtenido
            return formato;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Persona findPersonaByEmail(String email) {
        try {
            // 1️⃣ Construir la URL completa del microservicio o gateway
            String url = "http://localhost:8080/api/personas/email/" + email;

            // 2️⃣ Realizar la petición GET usando tu clase HttpUtil
            String jsonResponse = HttpUtil.get(url);

            // 3️⃣ Convertir la respuesta JSON en un objeto Persona
            Persona persona = gson.fromJson(jsonResponse, Persona.class);

            // 4️⃣ Retornar el objeto Persona
            return persona;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateEstadoObservaciones(Long idFormato, String nuevoEstado, String observaciones) {
        try {
            // 1️⃣ Construir la URL del microservicio (usa tu API Gateway si existe)
            String url = BASE_URL + "/" + idFormato + "/estado/" + nuevoEstado + "/" + observaciones;

            // 2️⃣ Hacer la petición PUT (sin cuerpo, el backend usa path variables)
            String jsonResponse = HttpUtil.put(url, "");

            // 3️⃣ Verificar si la respuesta no está vacía → actualización exitosa
            boolean actualizado = jsonResponse != null && !jsonResponse.isEmpty();

            // 4️⃣ Si se actualizó correctamente, notificar a los observers (como antes)
            if (actualizado) {
                try {
                    this.notifyAllObserves(); // 🔔 Notifica a todos los paneles u observadores suscritos
                    System.out.println("✅ Observers notificados tras actualizar el estado del FormatoA.");
                } catch (NullPointerException e) {
                    System.out.println("⚠️ No hay observers registrados para notificar.");
                }
            }

            return actualizado;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<FormatoA> listarFormatosPorPrograma(String programa) {
        try {
            // 1️⃣ Construir la URL del microservicio con el nombre del programa
            String url = BASE_URL + "/programa/" + programa;

            // 2️⃣ Realizar la petición GET al backend
            String jsonResponse = HttpUtil.get(url);

            // 3️⃣ Convertir la respuesta JSON en una lista de objetos FormatoA
            Type listType = new TypeToken<List<FormatoA>>() {}.getType();
            List<FormatoA> lista = gson.fromJson(jsonResponse, listType);

            // 4️⃣ Retornar la lista de formatos A del programa
            return lista;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Anteproyecto> listarAnteproyectos() {
        try {
            // 1️⃣ Construir la URL hacia el endpoint del controlador de anteproyectos
            String url = "http://localhost:8080/api/anteproyectos";

            // 2️⃣ Realizar la petición GET usando HttpUtil
            String jsonResponse = HttpUtil.get(url);

            // 3️⃣ Convertir la respuesta JSON en una lista de objetos Anteproyecto
            Type listType = new TypeToken<List<Anteproyecto>>() {}.getType();
            List<Anteproyecto> lista = gson.fromJson(jsonResponse, listType);

            // 4️⃣ Retornar la lista (puede estar vacía pero no nula)
            return lista != null ? lista : new ArrayList<>();

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>(); // devolver lista vacía para evitar NullPointerException
        }
    }
    public Anteproyecto buscarAnteproyectoPorId(Long id) {
        try {
            // 1️⃣ Construir la URL hacia el endpoint del microservicio de anteproyectos
            String url = "http://localhost:8080/api/anteproyectos/" + id;

            // 2️⃣ Realizar la petición GET al backend
            String jsonResponse = HttpUtil.get(url);

            // 3️⃣ Si la respuesta es vacía o nula, retornar null
            if (jsonResponse == null || jsonResponse.isEmpty()) {
                System.err.println("⚠ No se encontró el anteproyecto con ID: " + id);
                return null;
            }

            // 4️⃣ Convertir la respuesta JSON en un objeto Anteproyecto
            Anteproyecto anteproyecto = gson.fromJson(jsonResponse, Anteproyecto.class);

            // 5️⃣ Retornar el objeto obtenido
            return anteproyecto;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<Persona> listarDocentesDisponibles(Long idFormatoA) {
        try {
            // Construir la URL completa hacia el endpoint del microservicio
            String url = "http://localhost:8080/api/personas/docentesDisponibles/" + idFormatoA;

            // Realizar la petición GET
            String jsonResponse = HttpUtil.get(url);

            // Convertir la respuesta JSON en lista de Personas
            Type listType = new TypeToken<List<Persona>>() {}.getType();
            List<Persona> docentes = gson.fromJson(jsonResponse, listType);

            // Retornar la lista (puede estar vacía)
            return docentes != null ? docentes : new ArrayList<>();

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
