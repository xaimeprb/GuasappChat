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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controlador de la pantalla de login del cliente.
 * Gestiona la introducción del alias del usuario, la dirección
 * del servidor y a la ventana principal del chat.
 */
public class ControladorLogin {

    private static final Logger LOG = Logger.getLogger(ControladorLogin.class.getName());
    private static final String MAIN_VIEW = "/psp/chat/cliente/ui/MainClienteView.fxml";


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

        // Rellenamos los campos con los últimos datos guardados, en caso de que existan
        String ultimoServidor = ajustesRepositorio.cargarUltimoServidor();

        if (ultimoServidor != null && !ultimoServidor.isBlank()) {

            txtServidor.setText(ultimoServidor);

        }

        String ultimoAlias = ajustesRepositorio.cargarUltimoAlias();

        if (ultimoAlias != null && !ultimoAlias.isBlank()) {

            txtAlias.setText(ultimoAlias);

        }

        lblError.setText("");  // limpiamos error

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

        limpiarError();

        String servidor = txtServidor.getText();

        if (servidor != null) {

            servidor = servidor.trim();

        }

        if (servidor == null || servidor.isEmpty()) {

            servidor = "localhost";

        }

        String alias = txtAlias.getText();

        if (alias != null) {

            alias = alias.trim();

        }

        if (alias == null || alias.isEmpty()) {

            return;

        }

        ajustesRepositorio.guardarUltimoServidorYAalias(servidor, alias);

        UsuarioLocal usuario = new UsuarioLocal(alias);

        cargarPantallaPrincipal(usuario, servidor);

    }

    /**
     * Carga la vista principal del cliente
     */
    private void cargarPantallaPrincipal(UsuarioLocal usuario, String servidor) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_VIEW));
            Scene scene = new Scene(loader.load());

            MainControladorCliente controlador = loader.getController();

            if(controlador == null) {

                LOG.severe("Error al cargar el controlador");

                return;

            }

            controlador.inicializar(usuario, servidor);

            stage.setTitle("GuasappChat - " + usuario.getAlias());
            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();

        }  catch (IOException e) {

            LOG.log(Level.SEVERE, "No se pudo cargar la vista principal: " + MAIN_VIEW, e);

        }

    }

    private void limpiarError() {

        lblError.setText("");

    }

}
