package psp.chat.cliente.controlador;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import psp.chat.cliente.modelo.ConversacionLocal;
import psp.chat.cliente.modelo.UsuarioLocal;
import psp.chat.cliente.net.ConexionCliente;
import psp.chat.cliente.persistencia.AjustesRepositorioLocal;
import psp.chat.general.modelo.Conversacion;
import psp.chat.general.modelo.Mensaje;
import psp.chat.general.modelo.ResumenConversacion;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Controlador principal de la vista de cliente.
 *
 * Gestiona:
 * - Lista de conversaciones (panel izquierdo).
 * - Mensajes de la conversación seleccionada (panel derecho).
 * - Interacción con la conexión de red {@link ConexionCliente}.
 */
public class MainControladorCliente {

    @FXML
    private BorderPane root;

    @FXML
    private ListView<ConversacionLocal> listViewConversaciones;

    @FXML
    private Label lblNombreContactoActual;

    @FXML
    private Label lblIpContactoActual;

    @FXML
    private ScrollPane scrollMensajes;

    @FXML
    private VBox contenedorMensajes;

    @FXML
    private TextArea txtMensaje;

    @FXML
    private Button btnEnviar;

    private UsuarioLocal usuario;
    private String hostServidor;
    private ConexionCliente conexionCliente;
    private AjustesRepositorioLocal ajustesRepositorio;

    private final ObservableList<ConversacionLocal> conversaciones =
            FXCollections.observableArrayList();

    private ConversacionLocal conversacionSeleccionada;

    private final DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Inicializa el controlador con la información del usuario local
     * y la dirección del servidor.
     *
     * @param usuario      usuario local (alias).
     * @param hostServidor host o IP del servidor.
     */
    public void inicializar(UsuarioLocal usuario, String hostServidor) {
        this.usuario = usuario;
        this.hostServidor = hostServidor;
        this.ajustesRepositorio = new AjustesRepositorioLocal();

        configurarListViewConversaciones();
        configurarBindingsUI();

        this.conexionCliente = new ConexionCliente(hostServidor, 5000, usuario, this);
        conexionCliente.conectar();

        // Tras conectar, pedimos al servidor el resumen de conversaciones.
        conexionCliente.solicitarResumenConversaciones();
    }

    /**
     * Configura el ListView de conversaciones:
     * - Asigna el modelo observable.
     * - Define una celda personalizada basada en FXML.
     * - Maneja el cambio de selección.
     */
    private void configurarListViewConversaciones() {
        listViewConversaciones.setItems(conversaciones);

        listViewConversaciones.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(ConversacionLocal conv, boolean empty) {
                super.updateItem(conv, empty);
                if (empty || conv == null) {
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(
                                getClass().getResource("/psp/chat/cliente/ui/ItemConversacion.fxml"));
                        HBox rootItem = loader.load();
                        ControladorItemConversacion ctrl = loader.getController();
                        ctrl.configurar(conv);
                        setGraphic(rootItem);
                    } catch (IOException e) {
                        e.printStackTrace();
                        setText(conv.toString());
                    }
                }
            }
        });

        listViewConversaciones.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        seleccionarConversacion(newVal);
                    }
                });
    }

    /**
     * Configura los bindings de la interfaz:
     * - Deshabilita el botón enviar si no hay conversación seleccionada
     *   o si el campo de mensaje está vacío.
     */
    private void configurarBindingsUI() {
        btnEnviar.disableProperty().bind(
                listViewConversaciones.getSelectionModel().selectedItemProperty().isNull()
                        .or(txtMensaje.textProperty().isEmpty()));
    }

    /**
     * Cambia la conversación activa y actualiza la cabecera y los mensajes.
     *
     * @param conversacion conversación local seleccionada.
     */
    private void seleccionarConversacion(ConversacionLocal conversacion) {
        this.conversacionSeleccionada = conversacion;
        lblNombreContactoActual.setText(conversacion.getAliasVisible());
        lblIpContactoActual.setText(conversacion.getIpRemota());

        contenedorMensajes.getChildren().clear();

        if (conversacion.getMensajes().isEmpty()) {
            // Pedimos el historial al servidor si aún no lo tenemos.
            conexionCliente.solicitarHistorialConversacion(conversacion.getIdConversacion());
        } else {
            conversacion.getMensajes().forEach(this::pintarMensaje);
        }
    }

    /**
     * Maneja el clic del botón "Enviar" o acción asociada.
     *
     * Envía el mensaje de texto al servidor usando {@link ConexionCliente}.
     */
    @FXML
    private void manejarEnviarMensaje() {
        if (conversacionSeleccionada == null) {
            return;
        }

        String texto = txtMensaje.getText().trim();
        if (texto.isEmpty()) {
            return;
        }

        conexionCliente.enviarMensajeTexto(conversacionSeleccionada, texto);
        txtMensaje.clear();
    }

    // ============================
    //  Callbacks desde la conexión
    // ============================

    /**
     * Callback llamado cuando el servidor envía la lista de conversaciones
     * (solo resumen).
     *
     * @param resumenes lista de resúmenes de conversación.
     */
    public void onResumenConversacionesRecibido(List<ResumenConversacion> resumenes) {
        Platform.runLater(() -> {
            conversaciones.clear();
            for (ResumenConversacion r : resumenes) {
                ConversacionLocal local = new ConversacionLocal(
                        r.getIdConversacion(),
                        r.getIpRemota(),
                        r.getAliasVisible()
                );
                local.actualizarResumen(r);
                conversaciones.add(local);
            }
        });
    }

    /**
     * Callback llamado cuando el servidor envía el historial completo de una conversación.
     *
     * @param conversacion conversación con todos sus mensajes.
     */
    public void onHistorialConversacionRecibido(Conversacion conversacion) {
        Platform.runLater(() -> {
            Optional<ConversacionLocal> localOpt = conversaciones.stream()
                    .filter(c -> c.getIdConversacion().equals(conversacion.getIdConversacion()))
                    .findFirst();

            if (localOpt.isEmpty()) {
                return;
            }

            ConversacionLocal local = localOpt.get();
            local.setMensajes(conversacion.getMensajes());

            if (local == conversacionSeleccionada) {
                contenedorMensajes.getChildren().clear();
                local.getMensajes().forEach(this::pintarMensaje);
            }
        });
    }

    /**
     * Callback llamado cuando llega un mensaje nuevo (entrante o enviado por nosotros,
     * según cómo lo usemos desde {@link ConexionCliente}).
     *
     * @param mensaje mensaje recibido.
     */
    public void onMensajeEntrante(Mensaje mensaje) {
        Platform.runLater(() -> {
            Optional<ConversacionLocal> localOpt = conversaciones.stream()
                    .filter(c -> c.getIdConversacion().equals(mensaje.getIdConversacion()))
                    .findFirst();

            ConversacionLocal local = localOpt.orElseGet(() -> {
                // Conversación nueva que no teníamos en la lista.
                ConversacionLocal nueva = new ConversacionLocal(
                        mensaje.getIdConversacion(),
                        mensaje.getRemitente(),
                        mensaje.getRemitente()
                );
                conversaciones.add(0, nueva);
                return nueva;
            });

            local.anadirMensaje(mensaje);

            if (local == conversacionSeleccionada) {
                pintarMensaje(mensaje);
            }

            listViewConversaciones.refresh();
        });
    }

    /**
     * Pinta una burbuja de mensaje en la conversación actual,
     * eligiendo la plantilla adecuada (emisor o receptor).
     *
     * @param mensaje mensaje a representar.
     */
    private void pintarMensaje(Mensaje mensaje) {
        try {
            boolean esPropio = mensaje.getRemitente()
                    .equalsIgnoreCase(usuario.getAlias());

            String recursoFXML = esPropio
                    ? "/psp/chat/cliente/ui/BurbujaMsjEmisor.fxml"
                    : "/psp/chat/cliente/ui/BurbujaMsjReceptor.fxml";

            FXMLLoader loader = new FXMLLoader(getClass().getResource(recursoFXML));
            HBox burbuja = loader.load();

            ControladorMsjBurbuja ctrl = loader.getController();
            String hora = mensaje.getFechaHora().format(formatterHora);
            ctrl.configurar(mensaje.getRemitente(), mensaje.getContenido(), hora);

            contenedorMensajes.getChildren().add(burbuja);

            // Forzamos a que el scroll baje al último mensaje.
            scrollMensajes.layout();
            scrollMensajes.setVvalue(1.0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cierra la conexión de red de forma ordenada.
     *
     * Pensado para llamarse al cerrar la ventana o desde un menú.
     */
    public void cerrarConexion() {
        if (conexionCliente != null) {
            conexionCliente.cerrar();
        }
    }
}
