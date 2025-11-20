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
    private LocalDateTime fechaUltimoMensaje;
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
        this.fechaUltimoMensaje = null;
        this.mensajesNoLeidos = 0;

    }

    /**
     * Crea un resumen totalmente inicializado
     */
    public ResumenConversacion(String idConversacion, String ipRemota, String aliasVisible, String ultimoMensaje, LocalDateTime fechaUltimoMensaje, int mensajesNoLeidos) {

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

        this.fechaUltimoMensaje = fechaUltimoMensaje;
        this.mensajesNoLeidos = mensajesNoLeidos;
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

    public LocalDateTime getFechaUltimoMensaje() {
        return fechaUltimoMensaje;
    }

    public void setFechaUltimoMensaje(LocalDateTime fechaUltimoMensaje) {
        this.fechaUltimoMensaje = fechaUltimoMensaje;
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
                "', fecha=" + fechaUltimoMensaje +
                ", noLeidos=" + mensajesNoLeidos +
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
