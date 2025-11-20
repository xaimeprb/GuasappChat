package psp.chat.cliente.persistencia;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Repositorio de ajustes locales del cliente.
 *
 * Se usa para guardar y cargar pequeñas preferencias como:
 * - Último servidor usado
 * - Último alias introducido
 *
 * Los datos se almacenan en un fichero properties dentro de la
 * carpeta del usuario: ${user.home}/.guasappchat/ajustes.properties
 */
public class AjustesRepositorioLocal {

    private static final Logger LOG = Logger.getLogger(AjustesRepositorioLocal.class.getName());

    private final Path directorioConfig;
    private final Path ficheroConfig;

    /**
     * Inicializa el repositorio, creando el directorio de configuración
     * si aún no existe.
     */
    public AjustesRepositorioLocal() {

        String home = System.getProperty("user.home");
        this.directorioConfig = Paths.get(home, ".guasappchat");
        this.ficheroConfig = directorioConfig.resolve("ajustes.properties");

        crearDirectorioSiNoExiste();

    }

    /**
     * Garantiza que el directorio de configuración exista.
     */
    private void crearDirectorioSiNoExiste() {

        try {

            if (!Files.exists(directorioConfig)) {

                Files.createDirectories(directorioConfig);

            }

        } catch (IOException e) {

            LOG.log(Level.SEVERE, "Error creando directorio de configuración: " + e.getMessage(), e);

        }

    }

    /**
     * Carga el último servidor usado desde el fichero de ajustes
     * @return servidor guardado o "localhost" por defecto
     */
    public String cargarUltimoServidor() {

        Properties props = cargar();

        String servidor = props.getProperty("servidor");

        if (servidor != null) {

            return servidor;

        }

        return "localhost";

    }

    /**
     * Carga el último alias usado desde el fichero de ajustes
     * @return alias guardado o cadena vacía si no existe
     */
    public String cargarUltimoAlias() {

        Properties props = cargar();

        String alias = props.getProperty("alias");

        if (alias != null) {

            return alias;

        }

        return "";

    }

    /**
     * Guarda conjuntamente el servidor y el alias para futuras ejecuciones.
     *
     * @param servidor servidor a guardar.
     * @param alias    alias a guardar.
     */
    public void guardarUltimoServidorYAalias(String servidor, String alias) {

        Properties props = cargar();

        if (servidor != null) {

            props.setProperty("servidor", servidor);

        } else {

            props.setProperty("servidor", "");

        }

        if (alias != null) {

            props.setProperty("alias", alias);

        } else {

            props.setProperty("alias", "");

        }

        guardar(props);

    }

    /**
     * Carga el fichero de propiedades desde disco
     *
     * @return objeto {@link Properties} con los valores cargados
     */
    private Properties cargar() {

        Properties props = new Properties();

        if (Files.exists(ficheroConfig)) {

            try (InputStream in = Files.newInputStream(ficheroConfig)) {

                props.load(in);

            } catch (IOException e) {

                LOG.log(Level.SEVERE, "Error cargando ajustes: " + e.getMessage(), e);
            }
        }

        return props;

    }

    /**
     * Guarda el objeto {@link Properties} en el fichero de ajustes.
     *
     * @param props propiedades a persistir.
     */
    private void guardar(Properties props) {

        try (OutputStream out = Files.newOutputStream(ficheroConfig)) {

            props.store(out, "Ajustes GuasappChat cliente");

        } catch (IOException e) {

            LOG.log(Level.SEVERE, "Error guardando ajustes: " + e.getMessage(), e);
        }

    }
}