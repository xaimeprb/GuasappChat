package psp.chat.server.persistencia;

import psp.chat.general.modelo.Contacto;
import psp.chat.general.util.JsonUtil;
import psp.chat.general.util.ArchivoUtil;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio encargado de gestionar la lista de contactos del servidor.
 *
 * Funciona así:
 *  - Carga contactos desde un archivo JSON al iniciar.
 *  - Mantiene la lista en memoria.
 *  - Permite crear un contacto si no existe (basado en IP).
 *  - Guarda automáticamente en disco tras cada modificación.
 */
public class RepositorioContacto {

    private final String archivoContactos = "data.contactos/contactos.json";
    private final JsonUtil jsonUtil;
    private final List<Contacto> contactos;

    public RepositorioContacto(JsonUtil jsonUtil) {
        this.jsonUtil = jsonUtil;
        this.contactos = cargarDesdeArchivo();
    }

    /**
     * Obtiene un contacto por IP.
     */
    public Contacto buscarPorIp(String ip) {
        if (ip == null || ip.isEmpty()) {
            return null;
        }

        for (Contacto c : contactos) {
            if (c != null && ip.equals(c.getIpRemota())) {
                return c;
            }
        }
        return null;
    }

    /**
     * Crea un contacto si no existe.
     * Si existe, lo devuelve.
     */
    public Contacto crearContactoSiNoExiste(String ip) {
        if (ip == null || ip.isEmpty()) {
            return null;
        }

        Contacto existente = buscarPorIp(ip);
        if (existente != null) {
            return existente;
        }

        Contacto nuevo = new Contacto(ip, ""); // alias vacío por defecto
        contactos.add(nuevo);
        guardarEnArchivo();
        return nuevo;
    }

    /**
     * Guarda el estado actual en JSON.
     */
    private void guardarEnArchivo() {
        String json = jsonUtil.toJson(contactos);
        ArchivoUtil.guardarTexto(Path.of(archivoContactos), json);
    }

    /**
     * Carga la lista desde JSON.
     */
    private List<Contacto> cargarDesdeArchivo() {
        String json = ArchivoUtil.leerTexto(Path.of(archivoContactos));

        List<Contacto> lista = jsonUtil.fromJsonLista(json, Contacto.class);
        if (lista == null) {
            return new ArrayList<>();
        }
        return lista;
    }

    /**
     * Para debug y administración futura.
     */
    public List<Contacto> obtenerTodos() {
        return new ArrayList<>(contactos);
    }
}
