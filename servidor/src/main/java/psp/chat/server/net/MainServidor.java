package psp.chat.server.net;

import psp.chat.general.util.JsonUtil;
import psp.chat.server.modelo.ClienteConectado;
import psp.chat.server.persistencia.RepositorioContacto;
import psp.chat.server.persistencia.RepositorioConversacion;
import psp.chat.server.persistencia.RepositorioConversacionArchivo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Capa de aplicación del servidor.
 *
 * Coordina:
 *  - Repositorios (contactos, conversaciones).
 *  - Servidor de sockets.
 *  - Notificaciones a la UI mediante el observador.
 */
public class MainServidor {

    /**
     * Observador para comunicar eventos a la capa de presentación.
     */
    public interface ObservadorServidor {
        void onServidorArrancado(int puerto);

        void onServidorDetenido();

        void onClienteConectado(ClienteConectado cliente, List<ClienteConectado> clientesActuales);

        void onClienteDesconectado(ClienteConectado cliente, List<ClienteConectado> clientesActuales);

        void onMensajeLog(String mensaje);
    }

    private final ObservadorServidor observador;
    private final RepositorioContacto repositorioContacto;
    private final RepositorioConversacion repositorioConversacion;
    private final ServidorChat servidorChat;
    private final List<ClienteConectado> clientesConectados;

    public MainServidor(ObservadorServidor observador) {
        this.observador = observador;
        this.clientesConectados = new ArrayList<>();

        JsonUtil jsonUtil = new JsonUtil();

        this.repositorioContacto = new RepositorioContacto(jsonUtil);
        RepositorioConversacionArchivo repoArchivo =
                new RepositorioConversacionArchivo(jsonUtil);
        this.repositorioConversacion = new RepositorioConversacion(repoArchivo);

        int puerto = 5000; // configurable más adelante
        this.servidorChat = new ServidorChat(
                puerto,
                repositorioContacto,
                repositorioConversacion,
                jsonUtil,
                this
        );
    }

    /**
     * Arranca el servidor de sockets si no estaba ya en ejecución.
     */
    public void arrancarServidor() {
        boolean arrancado = servidorChat.iniciar();
        if (arrancado && observador != null) {
            observador.onServidorArrancado(servidorChat.getPuerto());
        }
    }

    /**
     * Detiene el servidor de sockets y todas las sesiones activas.
     */
    public void detenerServidor() {
        servidorChat.detener();
        if (observador != null) {
            observador.onServidorDetenido();
        }
    }

    /* ==========================
     *  API usada por ServidorChat
     * ========================== */

    void registrarClienteConectado(ClienteConectado clienteConectado) {
        if (clienteConectado == null) {
            return;
        }
        clientesConectados.add(clienteConectado);
        notificarClienteConectado(clienteConectado);
    }

    void registrarClienteDesconectado(ClienteConectado clienteDesconectado) {
        if (clienteDesconectado == null) {
            return;
        }
        clientesConectados.remove(clienteDesconectado);
        notificarClienteDesconectado(clienteDesconectado);
    }

    List<ClienteConectado> obtenerClientesConectados() {
        return Collections.unmodifiableList(clientesConectados);
    }

    void escribirLog(String texto) {
        if (observador != null) {
            observador.onMensajeLog(texto);
        }
    }

    private void notificarClienteConectado(ClienteConectado cliente) {
        if (observador != null) {
            observador.onClienteConectado(cliente, obtenerClientesConectados());
        }
    }

    private void notificarClienteDesconectado(ClienteConectado cliente) {
        if (observador != null) {
            observador.onClienteDesconectado(cliente, obtenerClientesConectados());
        }
    }
}
