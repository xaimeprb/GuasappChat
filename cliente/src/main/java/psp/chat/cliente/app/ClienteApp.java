package psp.chat.cliente.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import psp.chat.cliente.controlador.ControladorLogin;

/**
 * Punto de entrada principal de la aplicación de cliente.
 *
 * Se encarga de inicializar JavaFX, cargar la vista de login
 * y delegar la lógica en el {@link ControladorLogin}.
 */
public class ClienteApp extends Application {

    /**
     * Método invocado por la JVM para lanzar JavaFX.
     *
     * @param args argumentos de línea de comandos (no se usan).
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Inicializa la primera escena (pantalla de login).
     *
     * @param primaryStage ventana principal creada por JavaFX.
     * @throws Exception si se produce un error cargando el FXML.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/psp/chat/cliente/ui/LoginView.fxml"));

        Scene scene = new Scene(loader.load());

        ControladorLogin controlador = loader.getController();
        controlador.inicializar(primaryStage);

        primaryStage.setTitle("GuasappChat - Login");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
