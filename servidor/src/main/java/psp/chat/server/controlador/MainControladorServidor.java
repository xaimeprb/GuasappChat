package psp.chat.server.controlador;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import psp.chat.server.modelo.ClienteConectado;
import psp.chat.server.net.MainServidor;

import java.util.List;

/**
 * Controlador principal de la UI del servidor.
 *
 * Gestiona:
 *  - Arranque y parada del servidor.
 *  - Lista de clientes conectados.
 *  - Zona de log de eventos.
 *
 * No contiene lógica de red; delega en {@link MainServidor}.
 */
public class MainControladorServidor implements MainServidor.ObservadorServidor {

    @FXML
    private Button btnArrancar;

    @FXML
    private Button btnDetener;

    @FXML
    private ListView<ClienteConectado> listaClientes;

    @FXML
    private TextArea areaLog;

    private final ObservableList<ClienteConectado> clientesObservable;
    private MainServidor mainServidor;
    private Stage stage;

    public MainControladorServidor() {
        clientesObservable = FXCollections.observableArrayList();
    }

    /**
     * Inicializa el controlador una vez cargado el FXML.
     * Este método lo llama explícitamente la clase {@link psp.chat.server.app.ServidorApp}.
     *
     * @param stage ventana principal, necesaria para enganchar el evento de cierre.
     */
    public void inicializar(Stage stage) {
        this.stage = stage;
        listaClientes.setItems(clientesObservable);

        mainServidor = new MainServidor(this);

        configurarEventosVentana();
        actualizarEstadoControles(false);
        escribirLog("Servidor preparado. Pulsa 'Arrancar' para iniciar.");
    }

    private void configurarEventosVentana() {
        stage.setOnCloseRequest(evento -> {
            if (mainServidor != null) {
                mainServidor.detenerServidor();
            }
        });
    }

    /**
     * Acción asociada al botón "Arrancar".
     */
    @FXML
    private void onArrancarServidor() {
        mainServidor.arrancarServidor();
    }

    /**
     * Acción asociada al botón "Detener".
     */
    @FXML
    private void onDetenerServidor() {
        mainServidor.detenerServidor();
    }

    private void actualizarEstadoControles(boolean servidorArrancado) {
        btnArrancar.setDisable(servidorArrancado);
        btnDetener.setDisable(!servidorArrancado);
    }

    private void escribirLog(String texto) {
        Platform.runLater(() -> {
            if (texto == null) return;
            areaLog.appendText(texto + System.lineSeparator());
        });
    }



    private void refrescarListaClientes(List<ClienteConectado> nuevosClientes) {
        clientesObservable.setAll(nuevosClientes);
    }

    /* ==========================
     *  Callbacks del observador
     * ========================== */

    @Override
    public void onServidorArrancado(int puerto) {
        Platform.runLater(() -> {
            actualizarEstadoControles(true);
            escribirLog("Servidor arrancado en el puerto " + puerto + ".");
        });
    }

    @Override
    public void onServidorDetenido() {
        Platform.runLater(() -> {
            actualizarEstadoControles(false);
            refrescarListaClientes(List.of());
            escribirLog("Servidor detenido.");
        });
    }

    @Override
    public void onClienteConectado(ClienteConectado cliente, List<ClienteConectado> clientesActuales) {
        Platform.runLater(() -> {
            refrescarListaClientes(clientesActuales);
            escribirLog("Cliente conectado: " + cliente.descripcionCorta());
        });
    }

    @Override
    public void onClienteDesconectado(ClienteConectado cliente, List<ClienteConectado> clientesActuales) {
        Platform.runLater(() -> {
            refrescarListaClientes(clientesActuales);
            escribirLog("Cliente desconectado: " + cliente.descripcionCorta());
        });
    }

    @Override
    public void onMensajeLog(String mensaje) {
        Platform.runLater(() -> escribirLog(mensaje));
    }
}
