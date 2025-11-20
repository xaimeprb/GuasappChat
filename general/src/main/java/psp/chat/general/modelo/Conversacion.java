package psp.chat.general.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa una conversación entre el cliente local y otra IP/contacto
 * Contiene todos los mensajes asociados
 */
public class Conversacion {

    private String idConversacion;
    private String ipRemota;
    private String aliasVisible;
    private List<Mensaje> mensajes;

    /**
     * Constructor vacío requerido por Gson
     * Inicializa valores para evitar nulls
     */
    public Conversacion() {
        this.idConversacion = "";
        this.ipRemota = "";
        this.aliasVisible = "";
        this.mensajes = new ArrayList<>();
    }

    /**
     * Crea una conversación con la información esencial
     */
    public Conversacion(String idConversacion, String ipRemota, String aliasVisible) {

        if (idConversacion != null) {

            this.idConversacion = idConversacion;

        } else {

            this.idConversacion = "";

        }

        if (ipRemota != null) {

            this.ipRemota = ipRemota;

        } else {

            this.ipRemota = "";

        }

        if (aliasVisible != null) {

            this.aliasVisible = aliasVisible;

        } else {

            this.aliasVisible = "";

        }

        this.mensajes = new ArrayList<>();
    }

    public String getIdConversacion() {
        return idConversacion;
    }

    public void setIdConversacion(String idConversacion) {

        if (idConversacion != null) {

            this.idConversacion = idConversacion;

        } else {

            this.idConversacion = "";

        }

    }

    public String getIpRemota() {
        return ipRemota;
    }

    public void setIpRemota(String ipRemota) {

        if (ipRemota != null) {

            this.ipRemota = ipRemota;

        } else {

            this.ipRemota = "";

        }

    }

    public String getAliasVisible() {

        return aliasVisible;

    }

    public void setAliasVisible(String aliasVisible) {

        if (aliasVisible != null) {

            this.aliasVisible = aliasVisible;

        } else {

            this.aliasVisible = "";

        }

    }

    /**
     * Devuelve una lista inmutable de mensajes
     */
    public List<Mensaje> getMensajes() {
        return Collections.unmodifiableList(mensajes);
    }

    /**
     * Reemplaza por completo el historial
     */
    public void setMensajes(List<Mensaje> nuevosMensajes) {

        mensajes.clear();

        if (nuevosMensajes != null) {

            for (Mensaje m : nuevosMensajes) {

                if (m != null) {

                    mensajes.add(m);

                }

            }

        }

    }

    /**
     * Añade un mensaje al historial
     */
    public void anadirMensaje(Mensaje mensaje) {

        if (mensaje == null) {
            return;
        }

        mensajes.add(mensaje);
    }

    @Override
    public String toString() {

        return "Conversacion{id='" + idConversacion + "', ip='" + ipRemota + "', alias='" + aliasVisible + "', mensajes=" + mensajes.size() + "}";

    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {

            return true;

        }

        if (!(obj instanceof Conversacion)) {

            return false;

        }

        Conversacion otra = (Conversacion) obj;
        return idConversacion.equals(otra.idConversacion);

    }

    @Override
    public int hashCode() {
        return idConversacion.hashCode();
    }
}
