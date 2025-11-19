package psp.chat.cliente.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controlador de una burbuja individual de mensaje.
 *
 * Esta clase se reutiliza tanto para los mensajes enviados como para
 * los recibidos, variando solo el FXML que la envuelve.
 */
public class ControladorMsjBurbuja {

    @FXML
    private Label lblRemitente;

    @FXML
    private Label lblTexto;

    @FXML
    private Label lblHora;

    /**
     * Rellena el contenido visual de la burbuja de mensaje.
     *
     * @param remitente nombre/alias del emisor.
     * @param texto     contenido textual del mensaje.
     * @param hora      hora formateada (HH:mm).
     */
    public void configurar(String remitente, String texto, String hora) {
        lblRemitente.setText(remitente);
        lblTexto.setText(texto);
        lblHora.setText(hora);
    }
}
