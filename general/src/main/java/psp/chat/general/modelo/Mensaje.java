package psp.chat.general.modelo;

import psp.chat.general.net.TipoMensaje;

import java.time.LocalDateTime;

/**
 * Representa un mensaje dentro de una conversación.
 * Incluye información de remitente, destinatario, tipo y contenido.
 */
public class Mensaje {

    private String idConversacion;
    private String remitente;
    private String destinatario;
    private TipoMensaje tipoMensaje;
    private String contenido;
    private LocalDateTime fechaHora;

    /**
     * Constructor vacío requerido por Gson para deserializar.
     */
    public Mensaje() {
    }

    /**
     * Crea un mensaje nuevo. La fecha/hora se toma en el momento de crear el objeto.
     *
     * @param idConversacion identificador de la conversación a la que pertenece el mensaje.
     * @param remitente      identificador o alias del emisor.
     * @param destinatario   identificador o alias del receptor.
     * @param tipoMensaje    tipo de mensaje (texto, imagen, etc.).
     * @param contenido      contenido del mensaje.
     */
    public Mensaje(String idConversacion,
                   String remitente,
                   String destinatario,
                   TipoMensaje tipoMensaje,
                   String contenido) {
        this.idConversacion = idConversacion;
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.tipoMensaje = tipoMensaje;
        this.contenido = contenido;
        this.fechaHora = LocalDateTime.now();
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
     * @return remitente del mensaje.
     */
    public String getRemitente() {
        return remitente;
    }

    /**
     * Establece el remitente del mensaje.
     *
     * @param remitente nuevo remitente.
     */
    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    /**
     * @return destinatario del mensaje.
     */
    public String getDestinatario() {
        return destinatario;
    }

    /**
     * Establece el destinatario del mensaje.
     *
     * @param destinatario nuevo destinatario.
     */
    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    /**
     * @return tipo de mensaje (texto, archivo, etc.).
     */
    public TipoMensaje getTipoMensaje() {
        return tipoMensaje;
    }

    /**
     * Cambia el tipo de mensaje.
     *
     * @param tipoMensaje nuevo tipo.
     */
    public void setTipoMensaje(TipoMensaje tipoMensaje) {
        this.tipoMensaje = tipoMensaje;
    }

    /**
     * @return contenido textual del mensaje.
     */
    public String getContenido() {
        return contenido;
    }

    /**
     * Establece el contenido textual del mensaje.
     *
     * @param contenido nuevo contenido.
     */
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    /**
     * @return fecha y hora en la que se creó el mensaje.
     */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /**
     * Permite ajustar la fecha/hora del mensaje (por ejemplo, al reconstruir histórico).
     *
     * @param fechaHora fecha y hora a establecer.
     */
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}
