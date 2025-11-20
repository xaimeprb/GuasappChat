package psp.chat.server.net;

import psp.chat.general.modelo.Conversacion;
import psp.chat.general.modelo.Mensaje;
import psp.chat.general.modelo.ResumenConversacion;
import psp.chat.general.net.EmpaquetadoDatos;
import psp.chat.general.net.TipoComando;
import psp.chat.general.util.JsonUtil;
import psp.chat.server.modelo.ClienteConectado;
import psp.chat.server.persistencia.RepositorioContacto;
import psp.chat.server.persistencia.RepositorioConversacion;

import java.io.*;
import java.net.Socket;
import java.util.List;

/**
 * Gestiona la comunicación con UN solo cliente.
 *
 * Un hilo por sesión:
 *   - Lee JSON línea a línea
 *   - Decodifica comandos
 *   - Interactúa con repositorios
 *   - Envía respuestas JSON empaquetadas
 *
 * Esta clase NUNCA toca la capa de UI (JavaFX).
 */
public class SesionCliente implements Runnable {

    private final Socket socket;
    private final ClienteConectado cliente;
    private final RepositorioContacto repoContacto;
    private final RepositorioConversacion repoConversacion;
    private final JsonUtil json;
    private final MainServidor mainServidor;

    private BufferedReader entrada;
    private PrintWriter salida;
    private boolean activa;

    public SesionCliente(
            Socket socket,
            ClienteConectado cliente,
            RepositorioContacto repoContacto,
            RepositorioConversacion repoConversacion,
            JsonUtil jsonUtil,
            MainServidor mainServidor) {

        this.socket = socket;
        this.cliente = cliente;
        this.repoContacto = repoContacto;
        this.repoConversacion = repoConversacion;
        this.json = jsonUtil;
        this.mainServidor = mainServidor;
    }

    @Override
    public void run() {

        try {

            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            activa = true;

            mainServidor.escribirLog("Nueva sesión creada: " + cliente.descripcionCorta());

            while (activa) {

                String linea = entrada.readLine();

                // Cliente cerró socket
                if (linea == null) {
                    detener();
                    break;
                }

                procesarLinea(linea);
            }

        } catch (IOException e) {

            if (activa) {
                mainServidor.escribirLog("ERROR en sesión cliente: " + e.getMessage());
            }

        } finally {
            detener();
        }
    }

    private void procesarLinea(String linea) {

        if (linea == null) {
            return;
        }

        String texto = linea.trim();
        if (texto.isEmpty()) {
            return;
        }

        EmpaquetadoDatos paquete = json.fromJson(texto, EmpaquetadoDatos.class);

        if (paquete == null) {
            mainServidor.escribirLog("JSON inválido recibido de cliente.");
            return;
        }

        TipoComando comando = paquete.getComando();

        if (comando == null) {
            return;
        }

        switch (comando) {

            case LOGIN:
                procesarLogin(paquete.getPayloadJson());
                break;

            case LISTA_CONVERSACIONES:
                procesarListarConversaciones();
                break;

            case HISTORIAL_CONVERSACION:
                procesarHistorialConversacion(paquete.getPayloadJson());
                break;

            case NUEVO_MENSAJE:
                procesarNuevoMensaje(paquete.getPayloadJson());
                break;

            default:
                mainServidor.escribirLog("Comando NO soportado en servidor: " + comando);
                break;
        }
    }


    /* ==========================================================
     *                  LÓGICA DE COMANDOS
     * ========================================================== */

    /**
     * LOGIN → se recibe alias, se guarda en Contacto y se notifica a TODOS.
     */
    private void procesarLogin(String payloadJson) {

        String alias = json.fromJson(payloadJson, String.class);

        if (alias == null || alias.isBlank()) {
            alias = "";
        }

        cliente.getContacto().setAliasVisible(alias);

        repoContacto.guardar(cliente.getContacto());

        mainServidor.escribirLog("LOGIN → " + cliente.getContacto().descripcionCorta());

        // NECESARIO → reenvía alias actualizados a todos
        mainServidor.enviarListaConectadosATodos();
    }


    private void procesarHistorialConversacion(String payloadJson) {

        String idConversacion = json.fromJson(payloadJson, String.class);

        if (idConversacion == null || idConversacion.isBlank()) {
            mainServidor.escribirLog("ID de conversación inválido en HISTORIAL_CONVERSACION");
            return;
        }

        Conversacion conversacion = repoConversacion.obtenerConversacion(idConversacion);

        EmpaquetadoDatos respuesta = new EmpaquetadoDatos(
                TipoComando.HISTORIAL_CONVERSACION,
                json.toJson(conversacion)
        );

        enviar(respuesta);
    }


    private void procesarListarConversaciones() {

        List<ResumenConversacion> res = repoConversacion.obtenerResumenes(cliente);

        EmpaquetadoDatos respuesta = new EmpaquetadoDatos(
                TipoComando.LISTA_CONVERSACIONES,
                json.toJson(res)
        );

        enviar(respuesta);
    }


    private void procesarNuevoMensaje(String payloadJson) {

        Mensaje m = json.fromJson(payloadJson, Mensaje.class);

        if (m == null) {
            mainServidor.escribirLog("Mensaje inválido recibido de cliente.");
            return;
        }

        repoConversacion.guardarMensaje(m);

        EmpaquetadoDatos ack = new EmpaquetadoDatos(
                TipoComando.ACK,
                "\"ok\""
        );

        enviar(ack);
    }


    /* ==========================================================
     *                       ENVÍO
     * ========================================================== */

    public void enviar(EmpaquetadoDatos paquete) {

        if (paquete == null) {
            return;
        }

        salida.println(json.toJson(paquete));
    }

    public void enviarPaquete(EmpaquetadoDatos paquete) {
        enviar(paquete);
    }


    /* ==========================================================
     *                  CIERRE DE SESIÓN
     * ========================================================== */

    public void detener() {

        if (!activa) return;

        activa = false;

        try {
            socket.close();
        } catch (IOException ignored) {}

        mainServidor.registrarClienteDesconectado(cliente);
    }
}
