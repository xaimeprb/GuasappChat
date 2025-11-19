package psp.chat.general.modelo;

import java.time.LocalDateTime;

/**
 * Resumen ligero de una conversación.
 *
 * Se usa para poblar la lista lateral de conversaciones sin
 * tener que cargar todo el historial completo.
 */
public class ResumenConversacion {

    private String idConversacion;
    private String ipRemota;
    private String aliasVisible;
    private String ultimoMensaje;
    private LocalDateTime fechaUltimoMensaje;
    private int mensajesNoLeidos;

    /**
     * Constructor vacío requerido por Gson.
     */
    public ResumenConversacion() {
    }

    /**
     * Crea un resumen completamente inicializado.
     *
     * @param idConversacion    identificador de la conversación.
     * @param ipRemota          IP o identificador remoto.
     * @param aliasVisible      alias a mostrar.
     * @param ultimoMensaje     texto del último mensaje.
     * @param fechaUltimoMensaje fecha/hora del último mensaje.
     * @param mensajesNoLeidos  número de mensajes pendientes de leer.
     */
    public ResumenConversacion(String idConversacion,
                               String ipRemota,
                               String aliasVisible,
                               String ultimoMensaje,
                               LocalDateTime fechaUltimoMensaje,
                               int mensajesNoLeidos) {
        this.idConversacion = idConversacion;
        this.ipRemota = ipRemota;
        this.aliasVisible = aliasVisible;
        this.ultimoMensaje = ultimoMensaje;
        this.fechaUltimoMensaje = fechaUltimoMensaje;
        this.mensajesNoLeidos = mensajesNoLeidos;
    }

    /**
     * @return identificador de la conversación.
     */
    public String getIdConversacion() {
        return idConversacion;
    }

    public void setIdConversacion(String idConversacion) {
        this.idConversacion = idConversacion;
    }

    /**
     * @return IP o identificador remoto asociado al resumen.
     */
    public String getIpRemota() {
        return ipRemota;
    }

    public void setIpRemota(String ipRemota) {
        this.ipRemota = ipRemota;
    }

    /**
     * @return alias visible de la conversación.
     */
    public String getAliasVisible() {
        return aliasVisible;
    }

    public void setAliasVisible(String aliasVisible) {
        this.aliasVisible = aliasVisible;
    }

    /**
     * @return texto del último mensaje.
     */
    public String getUltimoMensaje() {
        return ultimoMensaje;
    }

    public void setUltimoMensaje(String ultimoMensaje) {
        this.ultimoMensaje = ultimoMensaje;
    }

    /**
     * @return fecha/hora del último mensaje, o null si no hay mensajes.
     */
    public LocalDateTime getFechaUltimoMensaje() {
        return fechaUltimoMensaje;
    }

    public void setFechaUltimoMensaje(LocalDateTime fechaUltimoMensaje) {
        this.fechaUltimoMensaje = fechaUltimoMensaje;
    }

    /**
     * @return número de mensajes no leídos en esta conversación.
     */
    public int getMensajesNoLeidos() {
        return mensajesNoLeidos;
    }

    public void setMensajesNoLeidos(int mensajesNoLeidos) {
        this.mensajesNoLeidos = mensajesNoLeidos;
    }
}
