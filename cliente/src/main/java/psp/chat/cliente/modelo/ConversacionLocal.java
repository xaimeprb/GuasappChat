package psp.chat.cliente.modelo;

import psp.chat.general.modelo.Mensaje;
import psp.chat.general.modelo.ResumenConversacion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una conversación en el cliente, con:
 * - Identificador de conversación.
 * - IP remota asociada.
 * - Alias visible (para mostrar en la UI).
 * - Lista de mensajes.
 * - Datos de resumen (último mensaje y fecha).
 */
public class ConversacionLocal {

    private final String idConversacion;
    private final String ipRemota;
    private String aliasVisible;

    private final List<Mensaje> mensajes;

    private String ultimoMensajeTexto;
    private LocalDateTime fechaUltimoMensaje;

    /**
     * Crea una conversación local sin historial inicial.
     *
     * @param idConversacion identificador único de la conversación.
     * @param ipRemota       ip/remoto con el que se conversa.
     * @param aliasVisible   alias que se mostrará al usuario.
     */
    public ConversacionLocal(String idConversacion, String ipRemota, String aliasVisible) {
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
     * @return IP del interlocutor remoto.
     */
    public String getIpRemota() {
        return ipRemota;
    }

    /**
     * @return alias visible del contacto remoto.
     */
    public String getAliasVisible() {
        return aliasVisible;
    }

    /**
     * Actualiza el alias visible de la conversación (p.ej. si el usuario
     * cambia el nombre del contacto).
     *
     * @param aliasVisible nuevo alias.
     */
    public void setAliasVisible(String aliasVisible) {
        this.aliasVisible = aliasVisible;
    }

    /**
     * @return lista de mensajes asociados a la conversación.
     */
    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    /**
     * Sustituye completamente el historial de la conversación.
     *
     * @param nuevos lista de mensajes que pasan a ser el historial.
     */
    public void setMensajes(List<Mensaje> nuevos) {
        mensajes.clear();
        mensajes.addAll(nuevos);
        recalcularResumen();
    }

    /**
     * Añade un mensaje al final del historial y actualiza los datos
     * de resumen (último texto y fecha).
     *
     * @param mensaje mensaje a añadir.
     */
    public void anadirMensaje(Mensaje mensaje) {
        mensajes.add(mensaje);
        ultimoMensajeTexto = mensaje.getContenido();
        fechaUltimoMensaje = mensaje.getFechaHora();
    }

    /**
     * Actualiza datos de resumen a partir de un {@link ResumenConversacion}
     * recibido del servidor.
     *
     * @param resumen resumen de conversación.
     */
    public void actualizarResumen(ResumenConversacion resumen) {
        this.aliasVisible = resumen.getAliasVisible();
        this.ultimoMensajeTexto = resumen.getUltimoMensaje();
        this.fechaUltimoMensaje = resumen.getFechaUltimoMensaje();
    }

    /**
     * @return texto del último mensaje de la conversación,
     * o cadena vacía si no hay mensajes.
     */
    public String getUltimoMensajeTexto() {
        return ultimoMensajeTexto;
    }

    /**
     * @return fecha del último mensaje, o {@code null} si no hay mensajes.
     */
    public LocalDateTime getFechaUltimoMensaje() {
        return fechaUltimoMensaje;
    }

    /**
     * Recalcula el resumen (último mensaje y fecha) a partir de la lista
     * de mensajes actual.
     */
    private void recalcularResumen() {
        if (mensajes.isEmpty()) {
            ultimoMensajeTexto = "";
            fechaUltimoMensaje = null;
        } else {
            Mensaje ultimo = mensajes.get(mensajes.size() - 1);
            ultimoMensajeTexto = ultimo.getContenido();
            fechaUltimoMensaje = ultimo.getFechaHora();
        }
    }

    @Override
    public String toString() {
        return aliasVisible + " (" + ipRemota + ")";
    }
}
