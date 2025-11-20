package psp.chat.general.net;

/**
 * Paquete de transporte para la comunicación cliente-servidor
 * Cada línea enviada por el socket es un JSON de esta clase
 * Contiene un comando y un payload en formato JSON
 */
public class EmpaquetadoDatos {

    private TipoComando comando;
    private String payloadJson;

    /**
     * Constructor vacío requerido por Gson
     * Se inicializan valores por defecto para evitar nulls
     */
    public EmpaquetadoDatos() {

        this.comando = TipoComando.LOGIN; // valor por defecto seguro
        this.payloadJson = "";

    }

    /**
     * Crea un sobre de datos completo
     */
    public EmpaquetadoDatos(TipoComando comando, String payloadJson) {

        if (comando != null) {

            this.comando = comando;

        } else {

            this.comando = TipoComando.LOGIN;

        }

        if (payloadJson != null) {

            this.payloadJson = payloadJson;

        } else {

            this.payloadJson = "";

        }

    }

    public TipoComando getComando() {
        return comando;
    }

    public void setComando(TipoComando comando) {

        if (comando != null) {

            this.comando = comando;

        } else {

            this.comando = TipoComando.LOGIN;

        }

    }

    public String getPayloadJson() {
        return payloadJson;
    }

    public void setPayloadJson(String payloadJson) {

        if (payloadJson != null) {

            this.payloadJson = payloadJson;

        } else {

            this.payloadJson = "";

        }

    }

    @Override
    public String toString() {

        return "EmpaquetadoDatos{comando=" + comando + ", payload='" + payloadJson + "'}";

    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {

            return true;

        }

        if (!(obj instanceof EmpaquetadoDatos)) {

            return false;

        }

        EmpaquetadoDatos otro = (EmpaquetadoDatos) obj;

        return comando == otro.comando && payloadJson.equals(otro.payloadJson);
    }

    @Override
    public int hashCode() {
        return comando.hashCode();
    }

}
