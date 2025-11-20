package psp.chat.general.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

/**
 * Utilidad de serialización/deserialización JSON basada en Gson
 * Sin métodos estáticos, siguiendo tu criterio
 */
public class JsonUtil {

    private final Gson gson;

    /**
     * Inicializa Gson con adaptadores personalizados sin usar lambdas.
     */
    public JsonUtil() {

        GsonBuilder builder = new GsonBuilder();

        // --- Adaptador de serialize LocalDateTime ---
        builder.registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {

                    @Override
                    public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {

                        if (src == null) {

                            return JsonNull.INSTANCE;

                        }

                        String texto = src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                        return new JsonPrimitive(texto);

                    }
                }
        );

        // --- Adaptador de deserialize LocalDateTime ---
        builder.registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {

                    @Override
                    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {

                        if (json == null) {

                            return null;

                        }

                        String texto = json.getAsString();

                        if (texto == null) {

                            return null;

                        }

                        String limpio = texto.trim();
                        if (limpio.isEmpty()) {

                            return null;

                        }

                        return LocalDateTime.parse(limpio, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    }
                }
        );

        this.gson = builder.create();
    }

    /**
     * Convierte un objeto Java en JSON.
     */
    public String toJson(Object objeto) {
        return gson.toJson(objeto);
    }

    /**
     * Convierte JSON en un objeto del tipo indicado.
     */
    public <T> T fromJson(String json, Class<T> tipo) {

        if (json == null) {

            return null;

        }

        String texto = json.trim();

        if (texto.isEmpty()) {

            return null;

        }

        return gson.fromJson(texto, tipo);
    }

    /**
     * Deserializa un JSON que contiene una lista
     */
    public <T> List<T> fromJsonLista(String json, Class<T> tipoElemento) {

        if (json == null) {

            return Collections.emptyList();

        }

        String texto = json.trim();

        if (texto.isEmpty()) {

            return Collections.emptyList();

        }

        Type tipoLista = TypeToken.getParameterized(List.class, tipoElemento).getType();
        List<T> lista = gson.fromJson(texto, tipoLista);

        if (lista == null) {

            return Collections.emptyList();

        }

        return lista;

    }

}
