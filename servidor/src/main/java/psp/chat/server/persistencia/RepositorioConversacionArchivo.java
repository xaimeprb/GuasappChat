package psp.chat.server.persistencia;

import psp.chat.general.modelo.Conversacion;
import psp.chat.general.util.JsonUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Persistencia en disco de conversaciones:
 *   data/conversaciones/<id>.json
 *
 * Se encarga de:
 *  - Guardar una conversación completa en archivo
 *  - Cargar una conversación individual
 *  - Cargar TODAS las conversaciones desde disco (necesario para los resúmenes)
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

    /**
     * Carga una conversación concreta por su ID.
     *
     * @param id identificador único
     * @return objeto Conversacion o null si no existe
     */
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

    /**
     * Guarda una conversación completa en disco.
     */
    public void guardar(Conversacion c) {

        if (c == null || c.getIdConversacion() == null) {
            return;
        }

        String ruta = carpeta + c.getIdConversacion() + ".json";

        String texto = json.toJson(c);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {

            bw.write(texto);

        } catch (IOException ignored) {}
    }

    /**
     * Carga TODAS las conversaciones existentes en carpeta.
     *
     * Es fundamental para poder generar:
     *  - lista de conversaciones del cliente
     *  - resúmenes
     *  - últimos mensajes
     */
    public List<Conversacion> cargarTodas() {

        List<Conversacion> todas = new ArrayList<>();

        try {

            Files.list(Path.of(carpeta))
                    .filter(p -> p.toString().endsWith(".json"))
                    .forEach(path -> {

                        String nombre = path.getFileName().toString()
                                .replace(".json", "");

                        Conversacion c = cargar(nombre);

                        if (c != null) {
                            todas.add(c);
                        }

                    });

        } catch (IOException ignored) {}

        return todas;
    }
}
