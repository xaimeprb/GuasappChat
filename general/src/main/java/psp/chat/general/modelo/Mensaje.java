package psp.chat.general.modelo;

import psp.chat.general.net.TipoMensaje;

import java.time.LocalDateTime;

/**
 * Representa un mensaje dentro de una conversación
 * Incluye remitente, destinatario, tipo, contenido y fecha/hora
 */
public class Mensaje {

    private String idConversacion;
    private String remitente;
    private String destinatario;
    private TipoMensaje tipoMensaje;
    private String contenido;
    private LocalDateTime fechaHora;

    /**
     * Constructor vacío requerido por Gson
     * Se inicializan valores por defecto para evitar nulls
     */
    public Mensaje() {

        this.idConversacion = "";
        this.remitente = "";
        this.destinatario = "";
        this.tipoMensaje = TipoMensaje.TEXTO;
        this.contenido = "";
        this.fechaHora = LocalDateTime.now();

    }

    /**
     * Crea un mensaje nuevo
     * La fecha/hora se toma en el instante de creación
     */
    public Mensaje(String idConversacion, String remitente, String destinatario, TipoMensaje tipoMensaje, String contenido) {

        if (idConversacion != null) {

            this.idConversacion = idConversacion;

        } else {

            this.idConversacion = "";

        }

        if (remitente != null) {

            this.remitente = remitente;

        } else {

            this.remitente = "";

        }

        if (destinatario != null) {

            this.destinatario = destinatario;

        } else {

            this.destinatario = "";

        }

        if (tipoMensaje != null) {

            this.tipoMensaje = tipoMensaje;

        } else {

            this.tipoMensaje = TipoMensaje.TEXTO;

        }

        if (contenido != null) {

            this.contenido = contenido;

        } else {

            this.contenido = "";

        }

        this.fechaHora = LocalDateTime.now();

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

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {

        if (remitente != null) {

            this.remitente = remitente;

        } else {

            this.remitente = "";

        }

    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {

        if (destinatario != null) {

            this.destinatario = destinatario;

        } else {

            this.destinatario = "";

        }

    }

    /**
     * Alias para compatibilidad con RepositorioConversacion
     * (ipCliente.equals(m.getDestino()))
     */
    public String getDestino() {
        return destinatario;
    }

    public TipoMensaje getTipoMensaje() {
        return tipoMensaje;
    }

    public void setTipoMensaje(TipoMensaje tipoMensaje) {

        if (tipoMensaje != null) {

            this.tipoMensaje = tipoMensaje;

        } else {

            this.tipoMensaje = TipoMensaje.TEXTO;

        }

    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {

        if (contenido != null) {

            this.contenido = contenido;

        } else {

            this.contenido = "";

        }

    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {

        if (fechaHora != null) {

            this.fechaHora = fechaHora;

        } else {

            this.fechaHora = LocalDateTime.now();

        }

    }

    @Override
    public String toString() {

        return "Mensaje{conv='" + idConversacion + "', de='" + remitente + "', para='" + destinatario
                + "', tipo=" + tipoMensaje + ", contenido='" + contenido + "', fecha=" + fechaHora + "}";

    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {

            return true;

        }

        if (!(obj instanceof Mensaje)) {

            return false;

        }

        Mensaje otro = (Mensaje) obj;

        return idConversacion.equals(otro.idConversacion)
                && fechaHora.equals(otro.fechaHora)
                && remitente.equals(otro.remitente)
                && contenido.equals(otro.contenido);

    }

    @Override
    public int hashCode() {
        return idConversacion.hashCode();
    }
}
