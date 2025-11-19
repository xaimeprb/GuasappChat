package psp.chat.general.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa una conversación entre el cliente local y otra IP/contacto.
 * Contiene todos los mensajes asociados y metadatos básicos.
 */
public class Conversacion {

    private String idConversacion;
    private String ipRemota;
    private String aliasVisible;
    private List<Mensaje> mensajes;

    /**
     * Constructor vacío requerido por Gson.
     * Inicializa la lista de mensajes para evitar nulls.
     */
    public Conversacion() {
        this.mensajes = new ArrayList<>();
    }

    /**
     * Crea una conversación con la información esencial.
     *
     * @param idConversacion identificador único de la conversación.
     * @param ipRemota       IP o identificador remoto.
     * @param aliasVisible   alias asociado a esa IP.
     */
    public Conversacion(String idConversacion, String ipRemota, String aliasVisible) {
        this.idConversacion = idConversacion;
        this.ipRemota = ipRemota;
        this.aliasVisible = aliasVisible;
        this.mensajes = new ArrayList<>();
    }

    /**
     * @return identificador de la conversación.
     */
    public String getIdConversacion() {
        return idConversacion;
    }

    /**
     * Establece el identificador de la conversación.
     *
     * @param idConversacion nuevo identificador.
     */
    public void setIdConversacion(String idConversacion) {
        this.idConversacion = idConversacion;
    }

    /**
     * @return IP o identificador remoto asociado a la conversación.
     */
    public String getIpRemota() {
        return ipRemota;
    }

    /**
     * Ajusta la IP o identificador remoto.
     *
     * @param ipRemota nuevo valor.
     */
    public void setIpRemota(String ipRemota) {
        this.ipRemota = ipRemota;
    }

    /**
     * @return alias que se mostrará para esta conversación.
     */
    public String getAliasVisible() {
        return aliasVisible;
    }

    /**
     * Cambia el alias visible asociado a esta conversación.
     *
     * @param aliasVisible nuevo alias.
     */
    public void setAliasVisible(String aliasVisible) {
        this.aliasVisible = aliasVisible;
    }

    /**
     * @return lista inmutable de mensajes de la conversación.
     */
    public List<Mensaje> getMensajes() {
        return Collections.unmodifiableList(mensajes);
    }

    /**
     * Reemplaza por completo el historial de mensajes.
     *
     * @param nuevosMensajes lista de mensajes a establecer.
     */
    public void setMensajes(List<Mensaje> nuevosMensajes) {
        mensajes.clear();
        if (nuevosMensajes != null) {
            mensajes.addAll(nuevosMensajes);
        }
    }

    /**
     * Añade un mensaje al final del historial.
     *
     * @param mensaje mensaje a añadir.
     */
    public void anadirMensaje(Mensaje mensaje) {
        if (mensaje == null) {
            return;
        }
        mensajes.add(mensaje);
    }
}
