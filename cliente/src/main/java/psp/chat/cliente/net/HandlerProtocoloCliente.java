package psp.chat.cliente.net;

import psp.chat.cliente.controlador.MainControladorCliente;
import psp.chat.general.modelo.Conversacion;
import psp.chat.general.modelo.Mensaje;
import psp.chat.general.modelo.ResumenConversacion;
import psp.chat.general.modelo.Contacto;
import psp.chat.general.net.EmpaquetadoDatos;
import psp.chat.general.net.TipoComando;
import psp.chat.general.util.JsonUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hilo encargado de leer continuamente del socket del servidor
 * y traducir los mensajes del protocolo en callbacks al controlador.
 */
public class HandlerProtocoloCliente implements Runnable {

    private static final Logger LOG = Logger.getLogger(HandlerProtocoloCliente.class.getName());

    private final BufferedReader in;
    private final MainControladorCliente controlador;
    private final JsonUtil jsonUtil;

    private volatile boolean seguirLeyendo;

    /**
     * Crea un nuevo manejador de protocolo
     *
     * @param in flujo de entrada del socket
     * @param controlador controlador principal del cliente
     * @param jsonUtil util para serialización/deserialización JSON
     */
    public HandlerProtocoloCliente(BufferedReader in, MainControladorCliente controlador, JsonUtil jsonUtil) {

        if (in == null) {
            throw new IllegalArgumentException("BufferedReader no puede ser null");
        }

        if (controlador == null) {
            throw new IllegalArgumentException("controlador no puede ser null");
        }

        if (jsonUtil == null) {
            throw new IllegalArgumentException("jsonUtil no puede ser null");
        }

        this.in = in;
        this.controlador = controlador;
        this.jsonUtil = jsonUtil;

        this.seguirLeyendo = true;
    }

    /**
     * Bucle principal de lectura de mensajes desde el servidor
     * Lee línea a línea, deserializa a {@link EmpaquetadoDatos} y
     * actúa según el comando recibido
     */
    @Override
    public void run() {
        try {

            String linea = in.readLine();

            while (seguirLeyendo && linea != null) {

                EmpaquetadoDatos paquete = jsonUtil.fromJson(linea, EmpaquetadoDatos.class);

                TipoComando comando = paquete.getComando();
                String payloadJson = paquete.getPayloadJson();

                switch (comando) {

                    case LISTA_CONVERSACIONES:

                        List<ResumenConversacion> resumenes =
                                jsonUtil.fromJsonLista(payloadJson, ResumenConversacion.class);

                        controlador.onResumenConversacionesRecibido(resumenes);
                        break;

                    case HISTORIAL_CONVERSACION:

                        Conversacion conversacion =
                                jsonUtil.fromJson(payloadJson, Conversacion.class);

                        controlador.onHistorialConversacionRecibido(conversacion);
                        break;

                    case NUEVO_MENSAJE:

                        Mensaje mensaje = jsonUtil.fromJson(payloadJson, Mensaje.class);
                        controlador.onMensajeEntrante(mensaje);
                        break;

                    case LISTA_CONTACTOS_CONECTADOS:

                        procesarListaContactosConectados(payloadJson);
                        break;

                    default:

                        LOG.warning("Comando no reconocido recibido del servidor: " + comando);
                        break;

                }

                linea = in.readLine();
            }

        } catch (IOException e) {

            LOG.log(Level.SEVERE,
                    "Error leyendo desde el servidor: " + e.getMessage(), e);

            // TODO: GestionReconexion
        }
    }

    /**
     * Procesa la lista completa de contactos conectados enviada por el servidor.
     *
     * @param payloadJson lista JSON de contactos conectados
     */
    private void procesarListaContactosConectados(String payloadJson) {

        if (payloadJson == null) {
            LOG.warning("Payload LISTA_CONTACTOS_CONECTADOS nulo");
            return;
        }

        // Lista de objetos Contacto
        List<Contacto> lista = jsonUtil.fromJsonLista(payloadJson, Contacto.class);

        if (lista == null) {
            LOG.warning("No se pudo parsear lista de contactos conectados");
            return;
        }

        // Notificar al controlador UI
        controlador.onListaContactosConectados(lista);
    }

    /**
     * Marca el hilo para detener la lectura
     */
    public void detener() {
        this.seguirLeyendo = false;
    }
}
