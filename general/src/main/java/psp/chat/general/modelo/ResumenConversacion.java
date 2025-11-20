package psp.chat.general.modelo;

import java.time.LocalDateTime;

/**
 * Resumen ligero de una conversación
 * Utilizado para mostrar la lista lateral sin cargar el historial completo
 */
public class ResumenConversacion {

    private String idConversacion;
    private String ipRemota;
    private String aliasVisible;
    private String ultimoMensaje;
    private String fechaUltimoMensaje;   // ← en servidor se envía como String
    private int mensajesNoLeidos;

    /**
     * Constructor vacío requerido por Gson
     * Inicializa valores por defecto para evitar nulls
     */
    public ResumenConversacion() {

        this.idConversacion = "";
        this.ipRemota = "";
        this.aliasVisible = "";
        this.ultimoMensaje = "";
        this.fechaUltimoMensaje = "";
        this.mensajesNoLeidos = 0;

    }

    /**
     * Constructor COMPLETO usado por el servidor.
     *
     * Este constructor coincide exactamente con lo que crea tu
     * RepositorioConversacion en el servidor:
     *
     *     new ResumenConversacion(id, ipRemota, aliasVisible, preview, fecha);
     *
     * @param idConversacion     identificador de la conversación
     * @param ipRemota           IP remota del contacto
     * @param aliasVisible       alias visible del contacto
     * @param ultimoMensaje      texto del último mensaje
     * @param fechaUltimoMensaje fecha del último mensaje (String)
     */
    public ResumenConversacion(String idConversacion,
                               String ipRemota,
                               String aliasVisible,
                               String ultimoMensaje,
                               String fechaUltimoMensaje) {

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

        if (ultimoMensaje != null) {
            this.ultimoMensaje = ultimoMensaje;
        } else {
            this.ultimoMensaje = "";
        }

        if (fechaUltimoMensaje != null) {
            this.fechaUltimoMensaje = fechaUltimoMensaje;
        } else {
            this.fechaUltimoMensaje = "";
        }

        this.mensajesNoLeidos = 0;
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

    public String getUltimoMensaje() {
        return ultimoMensaje;
    }

    public void setUltimoMensaje(String ultimoMensaje) {

        if (ultimoMensaje != null) {
            this.ultimoMensaje = ultimoMensaje;
        } else {
            this.ultimoMensaje = "";
        }
    }

    public String getFechaUltimoMensaje() {
        return fechaUltimoMensaje;
    }

    public void setFechaUltimoMensaje(String fechaUltimoMensaje) {

        if (fechaUltimoMensaje != null) {
            this.fechaUltimoMensaje = fechaUltimoMensaje;
        } else {
            this.fechaUltimoMensaje = "";
        }
    }

    public int getMensajesNoLeidos() {
        return mensajesNoLeidos;
    }

    public void setMensajesNoLeidos(int mensajesNoLeidos) {

        if (mensajesNoLeidos >= 0) {
            this.mensajesNoLeidos = mensajesNoLeidos;
        } else {
            this.mensajesNoLeidos = 0;
        }
    }

    @Override
    public String toString() {
        return "ResumenConversacion{id='" + idConversacion +
                "', ip='" + ipRemota +
                "', alias='" + aliasVisible +
                "', ultimo='" + ultimoMensaje +
                "', fecha='" + fechaUltimoMensaje +
                "', noLeidos=" + mensajesNoLeidos +
                "}";
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof ResumenConversacion)) {
            return false;
        }

        ResumenConversacion otra = (ResumenConversacion) obj;
        return idConversacion.equals(otra.idConversacion);
    }

    @Override
    public int hashCode() {
        return idConversacion.hashCode();
    }
}
