package psp.chat.server.persistencia;

import psp.chat.general.modelo.Contacto;
import psp.chat.general.util.JsonUtil;
import psp.chat.general.util.ArchivoUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio encargado de gestionar la lista de contactos del servidor.
 *
 * Identidad del contacto = IP remota.
 *
 * Cada IP corresponde a un único Contacto.
 * Si se conecta alguien con la misma IP, se reutiliza el contacto
 * y se actualiza el alias en LOGIN.
 */
public class RepositorioContacto {

    private final Path archivoContactos = Path.of("data.contactos/contactos.json");

    private final JsonUtil jsonUtil;
    private final List<Contacto> contactos;

    public RepositorioContacto(JsonUtil jsonUtil) {
        this.jsonUtil = jsonUtil;
        asegurarEstructuraArchivos();
        this.contactos = cargarDesdeArchivo();
    }

    private void asegurarEstructuraArchivos() {
        try {
            Path carpeta = archivoContactos.getParent();

            if (carpeta != null && !Files.exists(carpeta)) {
                Files.createDirectories(carpeta);
            }

            if (!Files.exists(archivoContactos)) {
                Files.createFile(archivoContactos);
                ArchivoUtil.guardarTexto(archivoContactos, "[]");
            }

        } catch (IOException e) {
            throw new RuntimeException("No se pudo preparar archivo de contactos", e);
        }
    }

    /**
     * Busca un contacto por IP.
     */
    public Contacto buscarPorIp(String ip) {
        if (ip == null || ip.isBlank()) return null;

        for (Contacto c : contactos) {
            if (c != null && ip.equals(c.getIpRemota())) {
                return c;
            }
        }
        return null;
    }

    /**
     * Devuelve un contacto existente o crea uno nuevo si no existe.
     */
    public Contacto crearContactoSiNoExiste(String ip) {
        if (ip == null || ip.isBlank()) return null;

        Contacto existente = buscarPorIp(ip);
        if (existente != null) return existente;

        Contacto nuevo = new Contacto(ip, "");
        contactos.add(nuevo);
        guardarEnArchivo();
        return nuevo;
    }

    /**
     * Guarda o actualiza un contacto (identidad = IP).
     */
    public void guardar(Contacto contacto) {
        if (contacto == null) return;

        for (int i = 0; i < contactos.size(); i++) {
            if (contacto.getIpRemota().equals(contactos.get(i).getIpRemota())) {
                contactos.set(i, contacto);
                guardarEnArchivo();
                return;
            }
        }

        contactos.add(contacto);
        guardarEnArchivo();
    }

    public List<Contacto> obtenerTodos() {
        return new ArrayList<>(contactos);
    }

    private void guardarEnArchivo() {
        ArchivoUtil.guardarTexto(archivoContactos, jsonUtil.toJson(contactos));
    }

    private List<Contacto> cargarDesdeArchivo() {
        String json = ArchivoUtil.leerTexto(archivoContactos);
        if (json == null || json.isBlank()) return new ArrayList<>();

        List<Contacto> lista = jsonUtil.fromJsonLista(json, Contacto.class);
        return (lista != null) ? lista : new ArrayList<>();
    }
}
