package psp.chat.general.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

/**
 * Utilidad de serialización y deserialización JSON basada en Gson.
 *
 * Se instancia como objeto para respetar tu norma de evitar métodos estáticos
 * en tu propio código de aplicación.
 */
public class JsonUtil {

    private final Gson gson;

    /**
     * Inicializa la utilidad JSON con una configuración estándar.
     * Se registra un adaptador para {@link LocalDateTime} para
     * serializarlo como texto ISO.
     */
    public JsonUtil() {
        GsonBuilder builder = new GsonBuilder();

        // Adaptador simple para LocalDateTime (ISO-8601).
        builder.registerTypeAdapter(LocalDateTime.class,
                (com.google.gson.JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                        src == null
                                ? null
                                : context.serialize(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));

        builder.registerTypeAdapter(LocalDateTime.class,
                (com.google.gson.JsonDeserializer<LocalDateTime>) (json, typeOfT, context) -> {
                    if (json == null) {
                        return null;
                    }
                    String texto = json.getAsString();
                    if (texto == null) {
                        return null;
                    }
                    if (texto.trim().isEmpty()) {
                        return null;
                    }
                    return LocalDateTime.parse(texto, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                });

        gson = builder.create();
    }

    /**
     * Convierte un objeto Java a su representación JSON.
     *
     * @param objeto objeto a serializar.
     * @return cadena JSON generada.
     */
    public String toJson(Object objeto) {
        return gson.toJson(objeto);
    }

    /**
     * Convierte una cadena JSON a un objeto del tipo indicado.
     *
     * @param json  texto JSON de entrada.
     * @param tipo  clase destino.
     * @param <T>   tipo genérico del resultado.
     * @return instancia deserializada o null si el JSON es nulo o vacío.
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
     * Deserializa una lista de elementos desde una cadena JSON.
     *
     * @param json         texto JSON que representa un array.
     * @param tipoElemento clase de los elementos de la lista.
     * @param <T>          tipo de los elementos.
     * @return lista deserializada, o lista vacía si el JSON es nulo o vacío.
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
        List<T> resultado = gson.fromJson(texto, tipoLista);

        if (resultado == null) {
            return Collections.emptyList();
        }

        return resultado;
    }
}
