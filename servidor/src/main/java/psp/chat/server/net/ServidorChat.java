package psp.chat.server.net;

import psp.chat.general.util.JsonUtil;
import psp.chat.general.net.EmpaquetadoDatos;
import psp.chat.general.net.TipoComando;
import psp.chat.server.modelo.ClienteConectado;
import psp.chat.server.persistencia.RepositorioContacto;
import psp.chat.server.persistencia.RepositorioConversacion;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Servidor TCP responsable de:
 *  - Abrir un puerto y aceptar conexiones.
 *  - Crear una SesionCliente por cada cliente.
 *  - Notificar eventos a MainServidor.
 *
 * La lectura de mensajes se hace línea a línea (un JSON por línea).
 */
public class ServidorChat {

    private final int puerto;
    private final RepositorioContacto repoContacto;
    private final RepositorioConversacion repoConversacion;
    private final JsonUtil jsonUtil;
    private final MainServidor mainServidor;

    private ServerSocket serverSocket;
    private boolean activo;

    public ServidorChat(int puerto,
                        RepositorioContacto repoContacto,
                        RepositorioConversacion repoConversacion,
                        JsonUtil jsonUtil,
                        MainServidor mainServidor) {

        this.puerto = puerto;
        this.repoContacto = repoContacto;
        this.repoConversacion = repoConversacion;
        this.jsonUtil = jsonUtil;
        this.mainServidor = mainServidor;
    }

    public int getPuerto() {
        return puerto;
    }

    /**
     * Inicia el servidor si no está ya activo.
     */
    public boolean iniciar() {
        if (activo) {
            return false;
        }

        try {
            serverSocket = new ServerSocket(puerto);
            activo = true;

            Thread hiloAceptacion = new Thread(this::aceptarClientes, "Hilo-Aceptacion-Servidor");
            hiloAceptacion.start();

            mainServidor.escribirLog("Servidor iniciado en puerto " + puerto);
            return true;
        } catch (IOException e) {
            mainServidor.escribirLog("ERROR iniciando servidor: " + e.getMessage());
            activo = false;
            return false;
        }
    }

    /**
     * Hilo principal que acepta conexiones entrantes.
     */
    private void aceptarClientes() {
        while (activo) {
            try {
                Socket socket = serverSocket.accept();

                // Crear contacto provisional:
                String ip = socket.getInetAddress().getHostAddress();
                var contacto = repoContacto.crearContactoSiNoExiste(ip);
                ClienteConectado cliente = new ClienteConectado(contacto);

                // Sesión dedicada:
                SesionCliente sesion = new SesionCliente(
                        socket,
                        cliente,
                        repoContacto,
                        repoConversacion,
                        jsonUtil,
                        mainServidor
                );
                cliente.setSesionCliente(sesion);

                mainServidor.registrarClienteConectado(cliente);

                new Thread(sesion, "SesionCliente-" + ip).start();

            } catch (IOException e) {
                if (activo) {
                    mainServidor.escribirLog("ERROR aceptando cliente: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Detiene el servidor y todas las sesiones activas.
     */
    public void detener() {
        activo = false;

        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException ignored) {}

        mainServidor.escribirLog("Servidor detenido.");
    }
}
