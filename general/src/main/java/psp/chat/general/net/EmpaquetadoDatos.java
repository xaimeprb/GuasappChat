package psp.chat.general.net;

/**
 * Sobre de transporte para la comunicación cliente-servidor.
 *
 * Cada línea enviada por el socket es un JSON de esta clase:
 * - Incluye un {@link TipoComando}.
 * - Incluye un payload en formato JSON (cadena).
 *
 * La interpretación concreta del payload depende del comando.
 */
public class EmpaquetadoDatos {

    private TipoComando comando;
    private String payloadJson;

    /**
     * Constructor vacío requerido por Gson para deserializar.
     */
    public EmpaquetadoDatos() {
    }

    /**
     * Crea un sobre de datos listo para ser enviado por la red.
     *
     * @param comando     comando que indica la intención del mensaje.
     * @param payloadJson contenido JSON asociado al comando.
     */
    public EmpaquetadoDatos(TipoComando comando, String payloadJson) {
        this.comando = comando;
        this.payloadJson = payloadJson;
    }

    /**
     * @return comando del paquete.
     */
    public TipoComando getComando() {
        return comando;
    }

    /**
     * Establece el comando del paquete.
     *
     * @param comando nuevo comando.
     */
    public void setComando(TipoComando comando) {
        this.comando = comando;
    }

    /**
     * @return contenido del payload en formato JSON.
     */
    public String getPayloadJson() {
        return payloadJson;
    }

    /**
     * Establece el payload JSON del paquete.
     *
     * @param payloadJson nuevo contenido JSON.
     */
    public void setPayloadJson(String payloadJson) {
        this.payloadJson = payloadJson;
    }
}
