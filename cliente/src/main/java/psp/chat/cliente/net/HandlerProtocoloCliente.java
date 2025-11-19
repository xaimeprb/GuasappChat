package psp.chat.cliente.net;

import psp.chat.cliente.controlador.MainControladorCliente;
import psp.chat.general.modelo.Conversacion;
import psp.chat.general.modelo.Mensaje;
import psp.chat.general.modelo.ResumenConversacion;
import psp.chat.general.net.EmpaquetadoDatos;
import psp.chat.general.net.TipoComando;
import psp.chat.general.util.JsonUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/**
 * Hilo encargado de leer continuamente del socket del servidor
 * y traducir los mensajes del protocolo en callbacks al controlador.
 *
 * Se basa en:
 * - Una línea de texto por paquete JSON.
 * - Cada línea se deserializa a {@link EmpaquetadoDatos}.
 * - Según el {@link TipoComando}, se deserializa el payload a
 *   {@link Conversacion}, {@link Mensaje}, etc.
 */
public class HandlerProtocoloCliente implements Runnable {

    private final BufferedReader in;
    private final MainControladorCliente controlador;
    private final JsonUtil jsonUtil;

    private volatile boolean seguirLeyendo;

    /**
     * Crea un nuevo manejador de protocolo.
     *
     * @param in          flujo de entrada del socket.
     * @param controlador controlador principal del cliente.
     * @param jsonUtil    utilitario para serialización/deserialización JSON.
     */
    public HandlerProtocoloCliente(BufferedReader in,
                                   MainControladorCliente controlador,
                                   JsonUtil jsonUtil) {
        this.in = in;
        this.controlador = controlador;
        this.jsonUtil = jsonUtil;
        this.seguirLeyendo = true;
    }

    /**
     * Bucle principal de lectura de mensajes desde el servidor.
     *
     * Lee línea a línea, deserializa a {@link EmpaquetadoDatos} y
     * actúa según el comando recibido.
     */
    @Override
    public void run() {
        try {
            String linea;
            while (seguirLeyendo && (linea = in.readLine()) != null) {
                EmpaquetadoDatos paquete =
                        jsonUtil.fromJson(linea, EmpaquetadoDatos.class);

                TipoComando comando = paquete.getComando();
                String payloadJson = paquete.getPayloadJson();

                switch (comando) {
                    case LISTA_CONVERSACIONES -> {
                        List<ResumenConversacion> resumenes =
                                jsonUtil.fromJsonLista(payloadJson, ResumenConversacion.class);
                        controlador.onResumenConversacionesRecibido(resumenes);
                    }
                    case HISTORIAL_CONVERSACION -> {
                        Conversacion conversacion =
                                jsonUtil.fromJson(payloadJson, Conversacion.class);
                        controlador.onHistorialConversacionRecibido(conversacion);
                    }
                    case NUEVO_MENSAJE -> {
                        Mensaje mensaje =
                                jsonUtil.fromJson(payloadJson, Mensaje.class);
                        controlador.onMensajeEntrante(mensaje);
                    }
                    default -> {
                        // Otros comandos se podrán gestionar aquí.
                    }
                }
            }
        } catch (IOException e) {
            // Aquí se puede informar al controlador y/o iniciar reconexión.
            e.printStackTrace();
        }
    }

    /**
     * Marca el hilo para detener la lectura.
     */
    public void detener() {
        this.seguirLeyendo = false;
    }
}
