package psp.chat.cliente.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import psp.chat.cliente.modelo.ConversacionLocal;

import java.time.format.DateTimeFormatter;

/**
 * Controlador de la fila de conversación en la lista lateral.
 *
 * Se usa como celda personalizada en la {@link javafx.scene.control.ListView}
 * de conversaciones.
 */
public class ControladorItemConversacion {

    @FXML
    private Label lblAlias;

    @FXML
    private Label lblUltimoMensaje;

    @FXML
    private Label lblHoraUltimoMensaje;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Configura los datos visuales del item de conversación.
     *
     * @param conversacion datos locales de la conversación.
     */
    public void configurar(ConversacionLocal conversacion) {
        lblAlias.setText(conversacion.getAliasVisible());
        lblUltimoMensaje.setText(conversacion.getUltimoMensajeTexto());

        if (conversacion.getFechaUltimoMensaje() != null) {
            lblHoraUltimoMensaje.setText(
                    conversacion.getFechaUltimoMensaje().format(formatter)
            );
        } else {
            lblHoraUltimoMensaje.setText("");
        }
    }
}
