package co.unicauca.service;

import co.unicauca.entity.Persona;
import co.unicauca.utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class AuthService {
    private static final String BASE_URL = "http://localhost:8080/api/auth";
    private final Gson gson = new Gson();

    /**
     * Autentica un usuario contra el microservicio login/signin
     * ✅ TODA la lógica de validación está en el microservicio
     */
    public Persona login(String email, String password) throws Exception {
        String url = BASE_URL + "/login";

        // Crear JSON request
        JsonObject loginRequest = new JsonObject();
        loginRequest.addProperty("email", email);
        loginRequest.addProperty("password", password);

        String jsonRequest = gson.toJson(loginRequest);

        // Llamar al microservicio - ✅ LA LÓGICA ESTÁ AQUÍ
        String responseJson = HttpUtil.post(url, jsonRequest);

        // Procesar respuesta del microservicio
        JsonObject response = gson.fromJson(responseJson, JsonObject.class);

        boolean success = response.get("success").getAsBoolean();
        String message = response.get("message").getAsString();

        if (success) {
            JsonObject data = response.get("data").getAsJsonObject();
            JsonObject personaData = data.get("persona").getAsJsonObject();

            // Convertir JSON a Persona
            return mapJsonToPersona(personaData);
        } else {
            // ✅ EL MICROSERVICIO DECIDE EL MENSAJE DE ERROR
            throw new Exception(message);
        }
    }

    /**
     * Convierte JSON del microservicio a objeto Persona
     */
    private Persona mapJsonToPersona(JsonObject personaJson) {
        Persona persona = new Persona();

        persona.setIdUsuario(personaJson.get("id").getAsLong());
        persona.setName(personaJson.get("name").getAsString());
        persona.setLastname(personaJson.get("lastname").getAsString());
        persona.setEmail(personaJson.get("email").getAsString());

        // Procesar department (puede ser null)
        if (personaJson.has("department") && !personaJson.get("department").isJsonNull()) {
            persona.setDepartment(personaJson.get("department").getAsString());
        }

        // Procesar programa (puede ser null)
        if (personaJson.has("programa") && !personaJson.get("programa").isJsonNull()) {
            persona.setPrograma(personaJson.get("programa").getAsString());
        }

        // Procesar roles - convertir de String a EnumRol
        Set<co.unicauca.entity.EnumRol> roles = new HashSet<>();
        JsonArray rolesArray = personaJson.get("roles").getAsJsonArray();
        for (int i = 0; i < rolesArray.size(); i++) {
            String rolString = rolesArray.get(i).getAsString();
            try {
                co.unicauca.entity.EnumRol rol = co.unicauca.entity.EnumRol.valueOf(rolString);
                roles.add(rol);
            } catch (IllegalArgumentException e) {
                System.err.println("Rol desconocido: " + rolString);
            }
        }
        persona.setRoles(roles);

        return persona;
    }

    /**
     * Verifica disponibilidad de email
     */
    public boolean checkEmailAvailable(String email) throws Exception {
        String url = BASE_URL + "/check-email?email=" + email;
        String responseJson = HttpUtil.get(url);

        JsonObject response = gson.fromJson(responseJson, JsonObject.class);
        return response.get("success").getAsBoolean() &&
                response.get("data").getAsBoolean();
    }

    /**
     * Obtiene roles y programas disponibles
     */
    public JsonObject getAvailableRolesAndPrograms() throws Exception {
        String url = BASE_URL + "/roles";
        String responseJson = HttpUtil.get(url);
        return gson.fromJson(responseJson, JsonObject.class);
    }
}