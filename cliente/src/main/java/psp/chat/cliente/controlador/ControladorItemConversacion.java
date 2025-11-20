package psp.chat.cliente.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import psp.chat.cliente.modelo.ConversacionLocal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controlador de la fila de conversación en la lista lateral.
 * Actúa como celda personalizada en la {@link javafx.scene.control.ListView}
 * de conversaciones.
 */
public class ControladorItemConversacion {

    @FXML
    private Label lblAlias;

    @FXML
    private Label lblUltimoMensaje;

    @FXML
    private Label lblHoraUltimoMensaje;

    /**
     * Formato estándar de hora para la vista
     */
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Configura los datos visuales del item de conversación.
     *
     * @param conversacion datos locales de la conversación.
     */
    public void configurar(ConversacionLocal conversacion) {

        lblAlias.setText("");
        lblUltimoMensaje.setText("");
        lblHoraUltimoMensaje.setText("");

        if (conversacion == null) {

            return; // No hay información que mostrar

        }

        String alias = conversacion.getAliasVisible();

        if (alias != null) {

            lblAlias.setText(alias);

        }

        String ultimoMensaje = conversacion.getUltimoMensajeTexto();

        if(ultimoMensaje != null) {

            lblUltimoMensaje.setText(ultimoMensaje);

        }

        LocalDateTime fecha = conversacion.getFechaUltimoMensaje();

        if(fecha != null) {

            lblHoraUltimoMensaje.setText(fecha.format(formatter));

        }

    }

}
