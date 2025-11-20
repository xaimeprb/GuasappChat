package psp.chat.general.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utilidad simple para lectura y escritura de ficheros de texto.
 * Métodos estáticos OK en utilidades puras (sin estado).
 *
 * Se usa para persistencia JSON tanto en cliente como en servidor.
 */
public final class ArchivoUtil {

    private ArchivoUtil() {
        // Evita instanciación
    }

    /**
     * Escribe texto en un archivo.
     *
     * @param ruta ruta del archivo
     * @param contenido contenido a escribir
     */
    public static void guardarTexto(Path ruta, String contenido) {
        try {
            Files.createDirectories(ruta.getParent());
            Files.writeString(ruta, contenido, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Error guardando archivo: " + ruta, e);
        }
    }

    /**
     * Lee un archivo de texto. Si no existe, devuelve "".
     *
     * @param ruta ruta del archivo
     * @return contenido del archivo o string vacío
     */
    public static String leerTexto(Path ruta) {
        try {
            if (!Files.exists(ruta)) {
                return "";
            }
            return Files.readString(ruta, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo archivo: " + ruta, e);
        }
    }
}
