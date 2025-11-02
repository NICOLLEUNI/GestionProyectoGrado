package co.unicauca.service;

import co.unicauca.entity.FormatoA;
import co.unicauca.entity.Persona;
import co.unicauca.infra.Subject;
import co.unicauca.utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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
 }
