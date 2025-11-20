package psp.chat.server.net;

import psp.chat.general.util.JsonUtil;
import psp.chat.server.modelo.ClienteConectado;
import psp.chat.server.persistencia.RepositorioContacto;
import psp.chat.server.persistencia.RepositorioConversacion;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Lista de sesiones activas.
     * Permite cerrar limpiamente TODAS las conexiones cuando el servidor se detiene.
     */
    private final List<SesionCliente> sesionesActivas;

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

        this.sesionesActivas = new ArrayList<>();
    }

    public int getPuerto() {
        return puerto;
    }

    /**
     * Inicia el servidor si no está ya activo.
     */
    public boolean iniciar() {

        if (activo) {

            mainServidor.escribirLog("Servidor YA estaba activo.");

            return false;

        }

        try {

            mainServidor.escribirLog("Intentando arrancar servidor en puerto " + puerto + "...");

            serverSocket = new ServerSocket(puerto);
            activo = true;

            Thread hiloAceptacion = new Thread(this::aceptarClientes, "Hilo-Aceptacion-Servidor");
            hiloAceptacion.setDaemon(true);
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

                // Crear contacto provisional según la IP:
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

                // Guardamos sesión activa
                synchronized (sesionesActivas) {
                    sesionesActivas.add(sesion);
                }

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

        // 1) Cerrar todas las sesiones activas
        synchronized (sesionesActivas) {

            for (SesionCliente sesion : sesionesActivas) {

                try {
                    sesion.detener();
                } catch (Exception ignored) {}

            }

            sesionesActivas.clear();
        }

        // 2) Cerrar el servidor
        try {

            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }

        } catch (IOException ignored) {}

        mainServidor.escribirLog("Servidor detenido.");
    }

}
