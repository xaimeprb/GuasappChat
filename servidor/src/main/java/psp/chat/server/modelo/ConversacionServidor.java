package psp.chat.server.modelo;

import psp.chat.general.modelo.Mensaje;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa una conversación gestionada por el servidor.
 *
 * A diferencia del modelo de cliente, aquí nos interesa saber qué
 * clientes participan en la conversación.
 */
public class ConversacionServidor {

    private final String idConversacion;
    private final String idParticipanteA;
    private final String idParticipanteB;
    private final List<Mensaje> mensajes;

    public ConversacionServidor(String idConversacion,
                                String idParticipanteA,
                                String idParticipanteB) {
        this.idConversacion = idConversacion;
        this.idParticipanteA = idParticipanteA;
        this.idParticipanteB = idParticipanteB;
        this.mensajes = new ArrayList<>();
    }

    public String getIdConversacion() {
        return idConversacion;
    }

    public String getIdParticipanteA() {
        return idParticipanteA;
    }

    public String getIdParticipanteB() {
        return idParticipanteB;
    }

    public List<Mensaje> getMensajes() {
        return Collections.unmodifiableList(mensajes);
    }

    public void agregarMensaje(Mensaje mensaje) {
        if (mensaje == null) {
            return;
        }
        mensajes.add(mensaje);
    }
}
