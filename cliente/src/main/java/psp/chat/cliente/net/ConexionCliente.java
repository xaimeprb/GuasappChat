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

/**
 * Clase responsable de gestionar la conexión de red del cliente
 * contra el servidor de chat.
 *
 * Se encarga de:
 * - Abrir el socket y los flujos de E/S.
 * - Enviar comandos y datos al servidor.
 * - Lanzar el hilo {@link HandlerProtocoloCliente} para recibir
 *   mensajes de manera asíncrona.
 */
public class ConexionCliente {

    private final String host;
    private final int puerto;
    private final UsuarioLocal usuario;
    private final MainControladorCliente controlador;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private final JsonUtil jsonUtil;
    private HandlerProtocoloCliente handler;

    /**
     * Crea una nueva conexión de cliente.
     *
     * @param host        dirección del servidor.
     * @param puerto      puerto de escucha del servidor.
     * @param usuario     usuario local.
     * @param controlador controlador principal para callbacks.
     */
    public ConexionCliente(String host, int puerto,
                           UsuarioLocal usuario,
                           MainControladorCliente controlador) {
        this.host = host;
        this.puerto = puerto;
        this.usuario = usuario;
        this.controlador = controlador;
        this.jsonUtil = new JsonUtil();
    }

    /**
     * Abre la conexión con el servidor y arranca el hilo de lectura.
     *
     * Envía además un comando de LOGIN al servidor con el alias del usuario.
     */
    public void conectar() {
        try {
            socket = new Socket(host, puerto);

            out = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream(), "UTF-8"),
                    true);

            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), "UTF-8"));

            handler = new HandlerProtocoloCliente(in, controlador, jsonUtil);
            Thread hilo = new Thread(handler, "HandlerProtocoloCliente");
            hilo.setDaemon(true);
            hilo.start();

            // Enviar comando de LOGIN
            String payloadLogin = jsonUtil.toJson(usuario.getAlias());
            EmpaquetadoDatos login = new EmpaquetadoDatos(TipoComando.LOGIN, payloadLogin);
            enviarEmpaquetado(login);

        } catch (IOException e) {
            // Más adelante: aquí se integrará la reconexión.
            e.printStackTrace();
        }
    }

    /**
     * Solicita al servidor la lista de conversaciones asociadas al usuario.
     */
    public void solicitarResumenConversaciones() {
        String payload = jsonUtil.toJson(usuario.getAlias());
        EmpaquetadoDatos paquete =
                new EmpaquetadoDatos(TipoComando.LISTA_CONVERSACIONES, payload);
        enviarEmpaquetado(paquete);
    }

    /**
     * Solicita el historial completo de una conversación concreta.
     *
     * @param idConversacion identificador de la conversación.
     */
    public void solicitarHistorialConversacion(String idConversacion) {
        String payload = jsonUtil.toJson(idConversacion);
        EmpaquetadoDatos paquete =
                new EmpaquetadoDatos(TipoComando.HISTORIAL_CONVERSACION, payload);
        enviarEmpaquetado(paquete);
    }

    /**
     * Envía al servidor un mensaje de texto perteneciente a una conversación.
     *
     * @param conversacion conversación local a la que pertenece.
     * @param texto        contenido textual a enviar.
     */
    public void enviarMensajeTexto(ConversacionLocal conversacion, String texto) {
        Mensaje mensaje = new Mensaje(
                conversacion.getIdConversacion(),
                usuario.getAlias(),
                conversacion.getIpRemota(),
                TipoMensaje.TEXTO,
                texto
        );

        String payloadMensaje = jsonUtil.toJson(mensaje);
        EmpaquetadoDatos paquete =
                new EmpaquetadoDatos(TipoComando.NUEVO_MENSAJE, payloadMensaje);
        enviarEmpaquetado(paquete);

        // Reflejamos el envío inmediatamente en la UI como mensaje propio.
        conversacion.anadirMensaje(mensaje);
        controlador.onMensajeEntrante(mensaje);
    }

    /**
     * Serializa un {@link EmpaquetadoDatos} a JSON y lo envía por el socket.
     *
     * @param paquete datos de protocolo a enviar.
     */
    private void enviarEmpaquetado(EmpaquetadoDatos paquete) {
        if (out == null) {
            return;
        }
        String jsonLinea = jsonUtil.toJson(paquete);
        out.println(jsonLinea);
    }

    /**
     * Cierra la conexión con el servidor y detiene el hilo de lectura.
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
            e.printStackTrace();
        }
    }
}
