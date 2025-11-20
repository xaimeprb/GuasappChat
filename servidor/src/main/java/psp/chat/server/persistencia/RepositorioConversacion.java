package psp.chat.server.persistencia;

import psp.chat.general.modelo.Conversacion;
import psp.chat.general.modelo.Mensaje;
import psp.chat.general.modelo.ResumenConversacion;
import psp.chat.server.modelo.ClienteConectado;

import java.util.ArrayList;
import java.util.List;

/**
 * Capa que gestiona conversaciones, delega persistencia en archivo.
 */
public class RepositorioConversacion {

    private final RepositorioConversacionArchivo archivo;

    public RepositorioConversacion(RepositorioConversacionArchivo archivo) {
        this.archivo = archivo;
    }

    /**
     * Carga una conversación por id o crea una nueva si no existe.
     */
    public Conversacion obtenerConversacion(String id) {
        Conversacion c = archivo.cargar(id);

        if (c != null) {
            return c;
        }

        // Conversación nueva (para el servidor NO usamos alias ni ip)
        return new Conversacion(id, "", "");
    }

    /**
     * Guarda un mensaje dentro de la conversación asociada.
     */
    public void guardarMensaje(Mensaje m) {

        Conversacion c = obtenerConversacion(m.getIdConversacion());

        // Añadir nuevo mensaje al historial:
        c.anadirMensaje(m);

        // Persistir en archivo
        archivo.guardar(c);
    }

    /**
     * Devuelve un listado simplificado para LISTA_CONVERSACIONES
     * (de momento no tenemos índice → devolvemos lista vacía).
     */
    public List<ResumenConversacion> obtenerResumenes(ClienteConectado cliente) {
        return new ArrayList<>();
    }
}
