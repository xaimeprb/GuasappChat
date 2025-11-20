package psp.chat.server.net;

import psp.chat.general.net.EmpaquetadoDatos;
import psp.chat.general.net.TipoComando;
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
 * Gestiona:
 *   - Repositorios persistentes
 *   - Lista de clientes conectados
 *   - Comunicación con la capa UI (ObservadorServidor)
 *   - Difusión de eventos a todos los clientes (lista de conectados)
 */
public class MainServidor {

    /**
     * Observador usado desde la UI del servidor para recibir eventos
     */
    public interface ObservadorServidor {
        void onServidorArrancado(int puerto);
        void onServidorDetenido();
        void onClienteConectado(ClienteConectado cliente, List<ClienteConectado> clientesActuales);
        void onClienteDesconectado(ClienteConectado cliente, List<ClienteConectado> clientesActuales);
        void onMensajeLog(String mensaje);
    }

    private final ObservadorServidor observador;
    private final JsonUtil json;

    private final RepositorioContacto repositorioContacto;
    private final RepositorioConversacion repositorioConversacion;

    private final ServidorChat servidorChat;
    private final List<ClienteConectado> clientesConectados;

    public MainServidor(ObservadorServidor observador) {

        this.observador = observador;
        this.clientesConectados = new ArrayList<>();

        this.json = new JsonUtil();

        this.repositorioContacto = new RepositorioContacto(json);
        this.repositorioConversacion = new RepositorioConversacion(
                new RepositorioConversacionArchivo(json)
        );

        int puerto = 5000;

        this.servidorChat = new ServidorChat(
                puerto,
                repositorioContacto,
                repositorioConversacion,
                json,
                this
        );
    }

    /**
     * Arranca el servidor y notifica a la UI.
     */
    public void arrancarServidor() {

        boolean arrancado = servidorChat.iniciar();

        if (arrancado && observador != null) {

            observador.onServidorArrancado(servidorChat.getPuerto());

        }
    }

    /**
     * Detiene el servidor y notifica a la UI.
     */
    public void detenerServidor() {

        servidorChat.detener();

        if (observador != null) {

            observador.onServidorDetenido();

        }
    }

    /* ==========================================================
     *          API INTERNA — llamada desde ServidorChat
     * ========================================================== */

    void registrarClienteConectado(ClienteConectado cliente) {

        if (cliente == null) {
            return;
        }

        clientesConectados.add(cliente);

        notificarClienteConectado(cliente);

        enviarListaConectadosATodos();
    }

    void registrarClienteDesconectado(ClienteConectado cliente) {

        if (cliente == null) {
            return;
        }

        clientesConectados.remove(cliente);

        notificarClienteDesconectado(cliente);

        enviarListaConectadosATodos();
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

    /* ==========================================================
     *      Difusión global: lista de contactos conectados
     * ========================================================== */

    /**
     * Envía a *todos* los clientes activos la lista completa de contactos conectados.
     *
     * El payload es una lista de objetos Contacto, NO Strings.
     * Esto permite que cada cliente tenga alias/IP actualizados.
     */
    public void enviarListaConectadosATodos() {

        // Extraer solo Contacto
        List<?> contactosParaEnviar = clientesConectados.stream()
                .map(ClienteConectado::getContacto)
                .toList();

        EmpaquetadoDatos paquete = new EmpaquetadoDatos(
                TipoComando.LISTA_CONTACTOS_CONECTADOS,
                json.toJson(contactosParaEnviar)
        );

        // Enviar a cada cliente con sesión activa
        for (ClienteConectado c : clientesConectados) {

            if (c.getSesionCliente() != null) {

                c.getSesionCliente().enviarPaquete(paquete);

            }
        }
    }
}
