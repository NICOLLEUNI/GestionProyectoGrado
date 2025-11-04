package co.unicauca.service;
import co.unicauca.entity.EnumRol; // ✅ AGREGAR
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

        // ✅ CORREGIDO: Usar getId() en lugar de getIdUsuario() del JSON
        persona.setIdUsuario(personaJson.get("id").getAsLong());
        persona.setName(personaJson.get("name").getAsString());
        persona.setLastname(personaJson.get("lastname").getAsString());
        persona.setEmail(personaJson.get("email").getAsString());

        if (personaJson.has("phone") && !personaJson.get("phone").isJsonNull()) {
            persona.setPhone(personaJson.get("phone").getAsString());
        }


        // Procesar department (puede ser null)
        if (personaJson.has("department") && !personaJson.get("department").isJsonNull()) {
            persona.setDepartment(personaJson.get("department").getAsString());
        }

        // Procesar programa (puede ser null)
        if (personaJson.has("programa") && !personaJson.get("programa").isJsonNull()) {
            persona.setPrograma(personaJson.get("programa").getAsString());
        }

        // Procesar roles - convertir de String a EnumRol
        Set<EnumRol> roles = new HashSet<>();
        JsonArray rolesArray = personaJson.get("roles").getAsJsonArray();
        for (int i = 0; i < rolesArray.size(); i++) {
            String rolString = rolesArray.get(i).getAsString();
            try {
                EnumRol rol = EnumRol.valueOf(rolString);
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


     //* Obtiene roles y programas disponibles

    public JsonObject getAvailableRolesAndPrograms() throws Exception {
        String url = BASE_URL + "/roles";
        String responseJson = HttpUtil.get(url);
        return gson.fromJson(responseJson, JsonObject.class);
    }
    /**
     * Registra un nuevo usuario en el microservicio
     * ✅ TODA la validación la hace el microservicio
     */
    public Persona register(String name, String lastname, String phone, String email,
                            String password, Set<EnumRol> roles, String programa,
                            String departamento) throws Exception {
        String url = BASE_URL + "/register";

        // Crear JSON request
        JsonObject registerRequest = new JsonObject();
        registerRequest.addProperty("name", name);
        registerRequest.addProperty("lastname", lastname);
        registerRequest.addProperty("phone", phone != null ? phone : "");
        registerRequest.addProperty("email", email);
        registerRequest.addProperty("password", password);

        // Convertir roles a JSON array
        JsonArray rolesArray = new JsonArray();
        for (EnumRol rol : roles) {
            rolesArray.add(rol.name());
        }
        registerRequest.add("roles", rolesArray);

        // Agregar programa y departamento si no son null
        if (programa != null && !programa.equals("Seleccione un programa")) {
            registerRequest.addProperty("programa", programa);
        }
        if (departamento != null && !departamento.equals("Seleccione un departamento")) {
            registerRequest.addProperty("departamento", departamento);
        }

        String jsonRequest = gson.toJson(registerRequest);

        // Llamar al microservicio - ✅ LA VALIDACIÓN ESTÁ AQUÍ
        String responseJson = HttpUtil.post(url, jsonRequest);

        // Procesar respuesta
        JsonObject response = gson.fromJson(responseJson, JsonObject.class);

        boolean success = response.get("success").getAsBoolean();
        String message = response.get("message").getAsString();

        if (success) {
            JsonObject data = response.get("data").getAsJsonObject();
            return mapJsonToPersona(data);
        } else {
            throw new Exception(message);
        }
    }

    /**
     * Verifica si un email está disponible
     */
    public boolean isEmailAvailable(String email) throws Exception {
        String url = BASE_URL + "/check-email?email=" + java.net.URLEncoder.encode(email, "UTF-8");
        String responseJson = HttpUtil.get(url);

        JsonObject response = gson.fromJson(responseJson, JsonObject.class);
        return response.get("success").getAsBoolean() && response.get("data").getAsBoolean();
    }

    /**
     * Obtiene roles, programas y departamentos disponibles del microservicio
     */
    public JsonObject getRegistrationOptions() throws Exception {
        String url = BASE_URL + "/roles";
        String responseJson = HttpUtil.get(url);
        return gson.fromJson(responseJson, JsonObject.class);
    }






}