package psp.chat.cliente.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controlador de la burbuja de mensaje para la vista del chat.
 * Se utiliza para mensajes enviados y recibidos, dependiendo del FXML contenedor.
 */
public class ControladorMsjBurbuja {

    @FXML
    private Label lblRemitente;

    @FXML
    private Label lblTexto;

    @FXML
    private Label lblHora;

    /**
     * Configura la burbuja con los datos del mensaje.
     * Este método debe ser seguro frente a valores null porque los mensajes
     * pueden llegar incompletos o mal formateados desde la red.
     *
     * @param remitente alias del emisor. Puede ser null.
     * @param texto contenido del mensaje. Puede ser null.
     * @param hora hora del envío, formateada. Puede ser null.
     */
    public void configurar(String remitente, String texto, String hora) {

        lblRemitente.setText("");
        lblTexto.setText("");
        lblHora.setText("");

        // Alias del remitente
        if (remitente != null) {

            String limpio = remitente.trim();

            if (!limpio.isEmpty()) {

                lblRemitente.setText(limpio);

            }

        }

        // Contenido del mensaje
        if (texto != null) {

            String limpio = texto.trim();

            if (!limpio.isEmpty()) {

                lblTexto.setText(limpio);

            } else {

                lblTexto.setText("[Mensaje vacío]");

            }

        } else {

            lblTexto.setText("[Mensaje no disponible]");

        }

        // Hora del mensaje
        if (hora != null) {

            String limpio = hora.trim();

            if (!limpio.isEmpty()) {

                lblHora.setText(limpio);

            }

        }

    }

}