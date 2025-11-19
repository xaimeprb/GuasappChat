package psp.chat.cliente.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import psp.chat.cliente.modelo.UsuarioLocal;
import psp.chat.cliente.persistencia.AjustesRepositorioLocal;

import java.io.IOException;

/**
 * Controlador de la pantalla de login del cliente.
 *
 * Gestiona la introducción del alias del usuario y la dirección
 * del servidor, así como el salto a la ventana principal del chat.
 */
public class ControladorLogin {

    @FXML
    private TextField txtServidor;

    @FXML
    private TextField txtAlias;

    @FXML
    private Label lblError;

    private Stage stage;
    private AjustesRepositorioLocal ajustesRepositorio;

    /**
     * Inicializa el controlador con la referencia a la ventana principal.
     *
     * @param stage ventana sobre la que se mostrará la UI.
     */
    public void inicializar(Stage stage) {
        this.stage = stage;
        this.ajustesRepositorio = new AjustesRepositorioLocal();

        // Rellenar campos con los últimos datos guardados, si existen.
        txtServidor.setText(ajustesRepositorio.cargarUltimoServidor());
        txtAlias.setText(ajustesRepositorio.cargarUltimoAlias());
    }

    /**
     * Maneja el clic sobre el botón "Conectar".
     *
     * - Valida alias.
     * - Guarda ajustes básicos.
     * - Crea el {@link UsuarioLocal}.
     * - Carga la vista principal del cliente.
     *
     * @param event evento de acción de JavaFX (no utilizado).
     */
    @FXML
    private void manejarConectar(ActionEvent event) {
        lblError.setText(""); // limpiar error previo

        String servidor = txtServidor.getText().trim();
        String alias = txtAlias.getText().trim();

        if (servidor.isEmpty()) {
            servidor = "localhost";
        }

        if (alias.isEmpty()) {
            lblError.setText("Introduce un alias.");
            return;
        }

        ajustesRepositorio.guardarUltimoServidorYAalias(servidor, alias);

        UsuarioLocal usuario = new UsuarioLocal(alias);

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/psp/chat/cliente/ui/MainClienteView.fxml"));
            Scene scene = new Scene(loader.load());

            MainControladorCliente controlador = loader.getController();
            controlador.inicializar(usuario, servidor);

            stage.setTitle("GuasappChat - " + alias);
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();

        } catch (IOException e) {
            lblError.setText("No se pudo cargar la vista principal.");
            e.printStackTrace();
        }
    }
}
