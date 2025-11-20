package psp.chat.server.net;

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
 * Cada conexión tiene su propio hilo SesionCliente que:
 *   - Lee comandos del cliente
 *   - Los interpreta
 *   - Interactúa con los repositorios
 *   - Responde con objetos JSON empaquetados
 *
 * Esta clase NO conoce JavaFX ni UI.
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

            mainServidor.escribirLog("Nueva sesión cliente abierta: " + cliente.descripcionCorta());

            while (activa) {

                String linea = entrada.readLine();

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

            case LISTA_CONVERSACIONES:
                procesarListarConversaciones();
                break;

            case NUEVO_MENSAJE:
                procesarNuevoMensaje(paquete.getPayloadJson());
                break;

            default:
                mainServidor.escribirLog("Comando no soportado por servidor: " + comando);
                break;
        }
    }

    /**
     * Devuelve al cliente el resumen de conversaciones.
     */
    private void procesarListarConversaciones() {

        List<ResumenConversacion> res = repoConversacion.obtenerResumenes(cliente);

        EmpaquetadoDatos respuesta = new EmpaquetadoDatos(
                TipoComando.LISTA_CONVERSACIONES,
                json.toJson(res)
        );

        enviar(respuesta);
    }

    /**
     * Registra un nuevo mensaje y persiste en el servidor.
     */
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

    /**
     * Envía un paquete JSON al cliente.
     */
    private void enviar(EmpaquetadoDatos paquete) {
        if (paquete == null) {
            return;
        }
        salida.println(json.toJson(paquete));
    }

    /**
     * Cierra la sesión y notifica al servidor principal.
     */
    public void detener() {
        activa = false;

        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ignored) {}

        mainServidor.registrarClienteDesconectado(cliente);
    }
}
