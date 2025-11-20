package psp.chat.cliente.modelo;

import psp.chat.general.modelo.Mensaje;
import psp.chat.general.modelo.ResumenConversacion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una conversación en el cliente, con:
 * - Id de conversación
 * - IP remota
 * - Alias visible
 * - Lista de mensajes
 * - Datos de resumen (último mensaje y fecha)
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

        if (idConversacion == null) {

            throw new IllegalArgumentException("idConversacion no puede ser null");

        }

        if (ipRemota == null) {

            throw new IllegalArgumentException("ipRemota no puede ser null");

        }

        if (aliasVisible == null) {

            aliasVisible = "";

        }

        this.idConversacion = idConversacion;
        this.ipRemota = ipRemota;
        this.aliasVisible = aliasVisible;

        this.mensajes = new ArrayList<>();

        this.ultimoMensajeTexto = "";
        this.fechaUltimoMensaje = null;

    }

    /**
     * @return identificador de la conversación
     */
    public String getIdConversacion() {
        return idConversacion;
    }

    /**
     * @return IP del remoto
     */
    public String getIpRemota() {
        return ipRemota;
    }

    /**
     * @return alias visible del contacto remoto
     */
    public String getAliasVisible() {
        return aliasVisible;
    }

    /**
     * Actualiza el alias visible de la conversación
     * @param aliasVisible nuevo alias
     */
    public void setAliasVisible(String aliasVisible) {

        if (aliasVisible != null) {

            this.aliasVisible = aliasVisible;

        } else {

            this.aliasVisible = "";

        }

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

        if (nuevos != null) {

            for (Mensaje m : nuevos) {

                if (m != null) {

                    mensajes.add(m);

                }

            }

        }

        recalcularResumen();

    }

    /**
     * Añade un mensaje al final del historial y actualiza los datos
     * de resumen (último texto y fecha).
     *
     * @param mensaje mensaje a añadir.
     */
    public void anadirMensaje(Mensaje mensaje) {

        if (mensaje == null) {

            return; // lo ignoramos para evitar errores

        }

        mensajes.add(mensaje);

        String contenido = mensaje.getContenido();

        if (contenido != null) {

            ultimoMensajeTexto = contenido;

        } else {

            ultimoMensajeTexto = "";

        }

        fechaUltimoMensaje = mensaje.getFechaHora();

    }

    /**
     * Actualiza datos de resumen a partir de un {@link ResumenConversacion}
     * recibido del servidor.
     *
     * @param resumen resumen de conversación.
     */
    public void actualizarResumen(ResumenConversacion resumen) {

        if (resumen == null) {

            return;

        }

        String nuevoAlias = resumen.getAliasVisible();

        if (nuevoAlias != null) {

            aliasVisible = nuevoAlias;

        } else {

            aliasVisible = "";

        }

        String ultimo = resumen.getUltimoMensaje();

        if (ultimo != null) {

            ultimoMensajeTexto = ultimo;

        } else {

            ultimoMensajeTexto = "";

        }

        fechaUltimoMensaje = resumen.getFechaUltimoMensaje();

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

            return;

        }

        Mensaje ultimo = mensajes.get(mensajes.size() - 1);

        if (ultimo != null) {

            String contenido = ultimo.getContenido();

            if (contenido != null) {

                ultimoMensajeTexto = contenido;

            } else {

                ultimoMensajeTexto = "";

            }

            fechaUltimoMensaje = ultimo.getFechaHora();

        }

    }

    @Override
    public String toString() {

        return aliasVisible + " (" + ipRemota + ")";

    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {

            return true;

        }

        if (!(obj instanceof ConversacionLocal)) {

            return false;

        }

        ConversacionLocal otra = (ConversacionLocal) obj;
        return idConversacion.equals(otra.idConversacion);

    }

    @Override
    public int hashCode() {

        return idConversacion.hashCode();

    }

}
