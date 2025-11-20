package psp.chat.server.persistencia;

import psp.chat.general.modelo.Conversacion;
import psp.chat.general.modelo.Mensaje;
import psp.chat.general.modelo.ResumenConversacion;
import psp.chat.server.modelo.ClienteConectado;

import java.util.ArrayList;
import java.util.List;

/**
 * Capa de gestión de conversaciones en el servidor.
 *
 * Delegamos toda la persistencia real en {@link RepositorioConversacionArchivo}.
 *
 * Responsabilidades:
 *  - Cargar una conversación por ID.
 *  - Crear conversación nueva si no existía.
 *  - Guardar mensajes dentro de una conversación.
 *  - Generar resúmenes para el cliente.
 */
public class RepositorioConversacion {

    private final RepositorioConversacionArchivo archivo;

    public RepositorioConversacion(RepositorioConversacionArchivo archivo) {
        this.archivo = archivo;
    }

    /**
     * Carga una conversación existente o crea una nueva vacía.
     *
     * El servidor NO controla alias aquí.
     */
    public Conversacion obtenerConversacion(String id) {

        if (id == null || id.isBlank()) {
            return new Conversacion("", "", "");
        }

        Conversacion c = archivo.cargar(id);

        if (c != null) {
            return c;
        }

        // Si la conversación no existe → se crea nueva
        return new Conversacion(id, "", "");
    }

    /**
     * Guarda un mensaje en la conversación correspondiente.
     */
    public void guardarMensaje(Mensaje m) {

        if (m == null || m.getIdConversacion() == null) {
            return;
        }

        Conversacion c = obtenerConversacion(m.getIdConversacion());

        // Insertamos el mensaje
        c.anadirMensaje(m);

        // Persistimos en archivo
        archivo.guardar(c);
    }

    /**
     * Devuelve un listado de resúmenes de conversación
     * pertenecientes al cliente que acaba de conectarse.
     *
     * Para ello:
     *  - Se obtienen TODAS las conversaciones guardadas.
     *  - Filtramos solo las que contengan mensajes donde el cliente está
     *    como remitente o como destinatario.
     *  - Creamos un ResumenConversacion para cada una.
     */
    public List<ResumenConversacion> obtenerResumenes(ClienteConectado cliente) {

        List<ResumenConversacion> lista = new ArrayList<>();

        if (cliente == null || cliente.getContacto() == null) {
            return lista;
        }

        String ipCliente = cliente.getContacto().getIpRemota();

        // Cargar TODAS las conversaciones guardadas
        List<Conversacion> todas = archivo.cargarTodas();

        for (Conversacion conv : todas) {

            if (conv == null || conv.getMensajes() == null) {
                continue;
            }

            boolean pertenece = conv.getMensajes()
                    .stream()
                    .anyMatch(m ->
                            ipCliente.equals(m.getRemitente()) ||
                                    ipCliente.equals(m.getDestinatario())
                    );

            if (!pertenece) {
                continue;
            }

            // Construimos el resumen
            Mensaje ultimo = conv.obtenerUltimoMensaje();

            String preview = "";
            String fecha = "";
            String aliasVisible = conv.getAliasVisible();
            String ipRemota = conv.getIpRemota();

            if (ultimo != null) {
                preview = ultimo.getContenido() != null ? ultimo.getContenido() : "";
                fecha = ultimo.getFechaHora() != null ? ultimo.getFechaHora().toString() : "";
            }

            ResumenConversacion r = new ResumenConversacion(
                    conv.getIdConversacion(),
                    ipRemota,
                    aliasVisible,
                    preview,
                    fecha
            );

            lista.add(r);
        }

        return lista;
    }
}
