package psp.chat.cliente.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import psp.chat.cliente.controlador.ControladorLogin;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Punto de entrada principal de la aplicación de cliente.
 * Se encarga de inicializar JavaFX, cargar la vista de login
 * y delegar la lógica en {@link ControladorLogin}.
 */
public class ClienteApp extends Application {

    private static final Logger LOG = Logger.getLogger(ClienteApp.class.getName());
    private static final String LOGIN_FXML = "/psp/chat/cliente/ui/LoginView.fxml";

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Inicializa la pantalla de Login.
     *
     * @param primaryStage ventana principal creada por JavaFX.
     * @throws Exception si se produce un error cargando el FXML.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/psp/chat/cliente/ui/LoginView.fxml"));

        Scene scene;

        try {

            scene = new Scene(loader.load());

        } catch (IOException ex) {

            LOG.log(Level.SEVERE, "No se pudo cargar el FXML de login: " + LOGIN_FXML, ex);

            return;

        }

        ControladorLogin controlador = loader.getController();

        if(controlador == null) {

            LOG.severe("El controlador del FXML es null. Revisa fx:controller en LoginView.fxml");

            return;

        }

        configurarStage(primaryStage, scene);

        controlador.inicializar(primaryStage);

        primaryStage.show();

    }

    /**
     * Configura el escenario principal con opciones estándar de la aplicación.
     */
    private void configurarStage(Stage stage, Scene scene) {

        stage.setTitle("GuasappChat - Login");
        stage.setResizable(false);
        stage.setScene(scene);

        // TODO: Añadir Icono + CSS

        // stage.getIcons().add(new Image("/icons/app.png"));
        // scene.getStylesheets().add("/style/global.css");

    }

}
