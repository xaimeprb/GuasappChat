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

    public String getIdConversacion() {
        return idConversacion;
    }

    public String getIpRemota() {
        return ipRemota;
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

    public List<Mensaje> getMensajes() {
        return mensajes;
    }

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
     * Añade un mensaje al final del historial y actualiza el resumen.
     */
    public void anadirMensaje(Mensaje mensaje) {

        if (mensaje == null) {
            return;
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

        // ---- CAMBIO IMPORTANTE ----
        // Convertimos String (servidor) → LocalDateTime (cliente)
        String fechaStr = resumen.getFechaUltimoMensaje();

        if (fechaStr != null && !fechaStr.isBlank()) {

            try {
                fechaUltimoMensaje = LocalDateTime.parse(fechaStr); // ISO-8601
            } catch (Exception ex) {
                fechaUltimoMensaje = null; // si no es parseable, lo ignoramos
            }

        } else {

            fechaUltimoMensaje = null;

        }
    }

    public String getUltimoMensajeTexto() {
        return ultimoMensajeTexto;
    }

    public LocalDateTime getFechaUltimoMensaje() {
        return fechaUltimoMensaje;
    }

    /**
     * Recalcula el resumen basado en el historial completo.
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
