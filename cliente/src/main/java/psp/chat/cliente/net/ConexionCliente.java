package psp.chat.cliente.net;

import psp.chat.cliente.controlador.MainControladorCliente;
import psp.chat.cliente.modelo.ConversacionLocal;
import psp.chat.cliente.modelo.UsuarioLocal;
import psp.chat.general.modelo.Mensaje;
import psp.chat.general.net.EmpaquetadoDatos;
import psp.chat.general.net.TipoComando;
import psp.chat.general.net.TipoMensaje;
import psp.chat.general.util.JsonUtil;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gestiona la conexión de red del cliente.
 *
 * Abre socket, inicializa E/S, envía comandos y lanza el hilo
 * HandlerProtocoloCliente para lectura asíncrona.
 */
public class ConexionCliente {

    private static final Logger LOG = Logger.getLogger(ConexionCliente.class.getName());

    private final String host;
    private final int puerto;
    private final UsuarioLocal usuario;
    private final MainControladorCliente controlador;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private final JsonUtil jsonUtil;
    private HandlerProtocoloCliente handler;

    public ConexionCliente(String host, int puerto, UsuarioLocal usuario, MainControladorCliente controlador) {

        if (host == null) {
            throw new IllegalArgumentException("host no puede ser null");
        }

        if (usuario == null) {
            throw new IllegalArgumentException("usuario no puede ser null");
        }

        if (controlador == null) {
            throw new IllegalArgumentException("controlador no puede ser null");
        }

        this.host = host;
        this.puerto = puerto;
        this.usuario = usuario;
        this.controlador = controlador;
        this.jsonUtil = new JsonUtil();
    }

    /**
     * Establece conexión con el servidor y envía LOGIN.
     */
    public void conectar() {

        try {
            socket = new Socket(host, puerto);

            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

            controlador.mostrarMensajeSistema("Conectado al servidor " + host + ":" + puerto);

            handler = new HandlerProtocoloCliente(in, controlador, jsonUtil);

            Thread hilo = new Thread(handler, "HandlerProtocoloCliente");
            hilo.setDaemon(true);
            hilo.start();

            // LOGIN automático
            String alias = usuario.getAlias();
            if (alias == null) alias = "";

            EmpaquetadoDatos login = new EmpaquetadoDatos(
                    TipoComando.LOGIN,
                    jsonUtil.toJson(alias)
            );

            enviarEmpaquetado(login);

        } catch (IOException e) {

            LOG.log(Level.SEVERE, "Error al conectar con el servidor: " + e.getMessage(), e);
            controlador.mostrarError("No se pudo conectar con " + host + ":" + puerto);

        }
    }

    /**
     * Solicita al servidor el resumen de conversaciones.
     */
    public void solicitarResumenConversaciones() {

        String alias = usuario.getAlias();
        if (alias == null) alias = "";

        EmpaquetadoDatos paquete = new EmpaquetadoDatos(
                TipoComando.LISTA_CONVERSACIONES,
                jsonUtil.toJson(alias)
        );

        enviarEmpaquetado(paquete);
    }

    /**
     * Solicita al servidor la lista de contactos conectados.
     */
    public void solicitarListaContactosConectados() {

        EmpaquetadoDatos paquete = new EmpaquetadoDatos(
                TipoComando.LISTA_CONTACTOS_CONECTADOS,
                "\"ok\""
        );

        enviarEmpaquetado(paquete);
    }

    /**
     * Solicita historial completo de una conversación.
     */
    public void solicitarHistorialConversacion(String idConversacion) {

        if (idConversacion == null) {
            return;
        }

        EmpaquetadoDatos paquete = new EmpaquetadoDatos(
                TipoComando.HISTORIAL_CONVERSACION,
                jsonUtil.toJson(idConversacion)
        );

        enviarEmpaquetado(paquete);
    }

    /**
     * Envía un mensaje TEXTO a una conversación concreta.
     */
    public void enviarMensajeTexto(ConversacionLocal conversacion, String texto) {

        if (conversacion == null) return;
        if (texto == null) texto = "";

        String id = conversacion.getIdConversacion();
        String remitente = usuario.getAlias();
        if (remitente == null) remitente = "";

        String destino = conversacion.getIpRemota();

        Mensaje mensaje = new Mensaje(
                id,
                remitente,
                destino,
                TipoMensaje.TEXTO,
                texto
        );

        EmpaquetadoDatos paquete = new EmpaquetadoDatos(
                TipoComando.NUEVO_MENSAJE,
                jsonUtil.toJson(mensaje)
        );

        enviarEmpaquetado(paquete);

        // Mostrar de inmediato en UI
        conversacion.anadirMensaje(mensaje);
        controlador.onMensajeEntrante(mensaje);
    }

    /**
     * Envía un paquete JSON al servidor.
     */
    private void enviarEmpaquetado(EmpaquetadoDatos paquete) {

        if (paquete == null) return;
        if (out == null) return;

        out.println(jsonUtil.toJson(paquete));
    }

    /**
     * Cierra conexión y frena el handler.
     */
    public void cerrar() {

        try {
            if (handler != null) {
                handler.detener();
            }

            if (socket != null && !socket.isClosed()) {
                socket.close();
            }

        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error cerrando conexión: " + e.getMessage(), e);
        }
    }
}
