package co.unicauca.utils;

import com.google.gson.*;

import java.time.LocalDate;

public class GsonFactory {
    public static Gson create() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>)
                        (json, type, context) -> LocalDate.parse(json.getAsString()))
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>)
                        (src, type, context) -> new JsonPrimitive(src.toString()))
                .create();
    }
}
