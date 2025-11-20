package psp.chat.server.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import psp.chat.server.controlador.MainControladorServidor;

import java.io.IOException;

/**
 * Punto de entrada JavaFX para la aplicación de servidor.
 *
 * Esta clase solo se encarga de:
 *  - Cargar el FXML principal.
 *  - Obtener el controlador asociado.
 *  - Entregarle el Stage para que pueda gestionar el cierre limpio.
 *
 * Toda la lógica de negocio y red se delega en otras clases.
 */
public class ServidorApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                ServidorApp.class.getResource("/psp/chat/server/ui/MainServidorView.fxml"));

        Scene scene = new Scene(loader.load());
        MainControladorServidor controlador = loader.getController();

        controlador.inicializar(stage);

        stage.setTitle("GuasappChat - Servidor");
        stage.setScene(scene);
        stage.show();
    }
}
