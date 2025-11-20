package psp.chat.server.persistencia;

import psp.chat.general.modelo.Conversacion;
import psp.chat.general.util.JsonUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Persistencia en disco de conversaciones:
 *   data/conversaciones/<id>.json
 */
public class RepositorioConversacionArchivo {

    private final String carpeta = "data/conversaciones/";
    private final JsonUtil json;

    public RepositorioConversacionArchivo(JsonUtil json) {
        this.json = json;

        File dir = new File(carpeta);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public Conversacion cargar(String id) {

        String ruta = carpeta + id + ".json";

        if (!Files.exists(Paths.get(ruta))) {
            return null;
        }

        try {
            String contenido = Files.readString(Paths.get(ruta));
            return json.fromJson(contenido, Conversacion.class);

        } catch (IOException ex) {
            return null;
        }
    }

    public void guardar(Conversacion c) {

        String ruta = carpeta + c.getIdConversacion() + ".json";

        String texto = json.toJson(c);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            bw.write(texto);
        } catch (IOException ignored) {}
    }
}
