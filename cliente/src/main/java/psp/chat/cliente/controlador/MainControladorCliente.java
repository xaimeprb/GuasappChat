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
import psp.chat.general.modelo.Contacto;
import psp.chat.general.modelo.Conversacion;
import psp.chat.general.modelo.Mensaje;
import psp.chat.general.modelo.ResumenConversacion;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controlador principal de la vista de cliente.
 *
 * Gestiona:
 * - Lista de conversaciones (panel izquierdo)
 * - Mensajes de la conversación seleccionada (panel derecho)
 * - Interacción con la conexión de red {@link ConexionCliente}
 * - Lógica de UI
 */
public class MainControladorCliente {

    private static final Logger LOG = Logger.getLogger(MainControladorCliente.class.getName());

    @FXML
    private BorderPane root;

    @FXML
    private ListView<ConversacionLocal> listViewConversaciones;

    @FXML
    private ListView<Contacto> listViewConectados;

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

    private final ObservableList<ConversacionLocal> conversaciones = FXCollections.observableArrayList();
    private final ObservableList<Contacto> conectados = FXCollections.observableArrayList();

    private ConversacionLocal conversacionSeleccionada;

    private final DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Inicializa el controlador con la información del usuario local
     * y la dirección del servidor
     *
     * @param usuario      usuario local (alias)
     * @param hostServidor host o IP del servidor
     */
    public void inicializar(UsuarioLocal usuario, String hostServidor) {

        if (usuario == null || hostServidor == null) {

            LOG.severe("Inicialización inválida: usuario o servidor null.");

            return;

        }

        this.usuario = usuario;
        this.hostServidor = hostServidor;
        this.ajustesRepositorio = new AjustesRepositorioLocal();

        configurarListViewConversaciones();
        configurarBindingsUI();

        this.conexionCliente = new ConexionCliente(hostServidor, 5000, usuario, this);
        conexionCliente.conectar();

        // Tras conectar, pedimos al servidor el resumen de conversaciones.
        conexionCliente.solicitarResumenConversaciones();

        mostrarMensajeSistema("Cliente iniciado correctamente");

        listViewConectados.setItems(conectados);

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
            protected void updateItem(ConversacionLocal conversacionLocal, boolean vacio) {
                
                super.updateItem(conversacionLocal, vacio);

                if (vacio || conversacionLocal == null) {

                    setGraphic(null);

                } else {

                    try {

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/psp/chat/cliente/ui/ItemConversacion.fxml"));
                        HBox rootItem = loader.load();

                        ControladorItemConversacion ctrl = loader.getController();
                        ctrl.configurar(conversacionLocal);

                        setText(null);
                        setGraphic(rootItem);

                    } catch (IOException e) {

                        LOG.log(Level.SEVERE, "No se pudo cargar ItemConversacion.fxml", e);
                        setGraphic(null);
                        setText(conversacionLocal.toString());

                    }
                }
            }
        });

        listViewConversaciones.getSelectionModel().selectedItemProperty().addListener((obs, viejoValor, nuevoValor) -> {

            if (nuevoValor != null) {

                seleccionarConversacion(nuevoValor);

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

        if (conversacion == null) {

            return;

        }

        conversacionSeleccionada = conversacion;

        String alias = conversacion.getAliasVisible();

        if (alias != null) {

            lblNombreContactoActual.setText(alias);

        } else {

            lblNombreContactoActual.setText("");

        }

        String ip = conversacion.getIpRemota();

        if (ip != null) {

            lblIpContactoActual.setText(ip);

        } else {

            lblIpContactoActual.setText("");

        }

        contenedorMensajes.getChildren().clear();

        if (conversacion.getMensajes().isEmpty()) {

            conexionCliente.solicitarHistorialConversacion(conversacion.getIdConversacion());

        } else {

            for (Mensaje m : conversacion.getMensajes()) {

                pintarMensaje(m);

            }

        }

    }


    /**
     * Maneja el clic del botón "Enviar" o acción asociada
     * Envía el mensaje de texto al servidor usando {@link ConexionCliente}
     */
    @FXML
    private void manejarEnviarMensaje() {
        if (conversacionSeleccionada == null) {
            return;
        }

        String texto = txtMensaje.getText();

        if (texto == null) {

            return;

        }

        texto = texto.trim();

        if (texto.isEmpty()) {

            return;

        }

        conexionCliente.enviarMensajeTexto(conversacionSeleccionada, texto);
        txtMensaje.clear();

    }

    /**
     * Callback llamado cuando el servidor envía la lista de conversaciones
     * (solo resumen)
     *
     * @param resumenes lista de resúmenes de conversación
     */
    public void onResumenConversacionesRecibido(List<ResumenConversacion> resumenes) {

        if (resumenes == null) {

            return;

        }

        Platform.runLater(() -> {

            conversaciones.clear();

            for (ResumenConversacion resumen : resumenes) {

                ConversacionLocal local = new ConversacionLocal(
                        resumen.getIdConversacion(),
                        resumen.getIpRemota(),
                        resumen.getAliasVisible()
                );

                local.actualizarResumen(resumen);
                conversaciones.add(local);

            }
        });
    }

    /**
     * Callback llamado cuando el servidor envía el historial completo de una conversación
     * @param conversacion conversación con todos sus mensajes.
     */
    public void onHistorialConversacionRecibido(Conversacion conversacion) {

        if (conversacion == null) {

            return;

        }

        Platform.runLater(() -> {

            Optional<ConversacionLocal> localOpt = conversaciones.stream()
                    .filter(c -> c.getIdConversacion().equals(conversacion.getIdConversacion()))
                    .findFirst();

            if (localOpt.isEmpty()) {

                return;

            }

            ConversacionLocal local = localOpt.get();
            local.setMensajes(conversacion.getMensajes());

            if (local.equals(conversacionSeleccionada)) {

                contenedorMensajes.getChildren().clear();

                for (Mensaje mensaje : local.getMensajes()) {

                    pintarMensaje(mensaje);

                }

            }

        });
    }

    /**
     * Callback llamado cuando llega un mensaje nuevo (entrante o enviado por nosotros,
     * según cómo lo usemos desde {@link ConexionCliente})
     *
     * @param mensaje mensaje recibido
     */
    public void onMensajeEntrante(Mensaje mensaje) {

        if (mensaje == null) {

            return;

        }

        Platform.runLater(() -> {

            Optional<ConversacionLocal> localOpt = conversaciones.stream()
                    .filter(c -> c.getIdConversacion().equals(mensaje.getIdConversacion()))
                    .findFirst();

            ConversacionLocal local;

            if (localOpt.isPresent()) {

                local = localOpt.get();

            } else {

                local = new ConversacionLocal(
                        mensaje.getIdConversacion(),
                        mensaje.getRemitente(),
                        mensaje.getRemitente()
                );

                conversaciones.add(0, local);

            }

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

        if (mensaje == null || mensaje.getRemitente() == null) {

            LOG.warning("Mensaje inválido recibido.");

            return;

        }

        try {
            boolean esPropio = mensaje.getRemitente()
                    .equalsIgnoreCase(usuario.getAlias());

            String recursoFXML;

            if (esPropio) {

                recursoFXML = "/psp/chat/cliente/ui/BurbujaMsjEmisor.fxml";

            } else {

                recursoFXML = "/psp/chat/cliente/ui/BurbujaMsjReceptor.fxml";

            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(recursoFXML));
            HBox burbuja = loader.load();

            ControladorMsjBurbuja ctrl = loader.getController();

            String hora;

            if (mensaje.getFechaHora() != null) {

                hora = mensaje.getFechaHora().format(formatterHora);

            } else {

                hora = "";

            }


            ctrl.configurar(mensaje.getRemitente(), mensaje.getContenido(), hora);

            contenedorMensajes.getChildren().add(burbuja);

            // Forzamos a que el scroll baje al último mensaje.
            scrollMensajes.layout();
            scrollMensajes.setVvalue(1.0);

        } catch (IOException e) {

            LOG.log(Level.SEVERE, "No se pudo cargar burbuja de mensaje", e);

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

    public void mostrarError(String mensaje) {

        Platform.runLater(() -> {

            System.err.println("ERROR CLIENTE: " + mensaje);
            new Alert(Alert.AlertType.ERROR, mensaje).showAndWait();

        });
    }

    /**
     * Muestra un mensaje generado por el sistema como una burbuja gris.
     */
    public void mostrarMensajeSistema(String msg) {

        if (msg == null || msg.isBlank()) return;

        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/psp/chat/cliente/ui/BurbujaMsjReceptor.fxml"));
                HBox burbuja = loader.load();

                ControladorMsjBurbuja ctrl = loader.getController();
                ctrl.configurar("Sistema", msg, "");

                contenedorMensajes.getChildren().add(burbuja);

                scrollMensajes.layout();
                scrollMensajes.setVvalue(1.0);

            } catch (IOException e) {
                LOG.log(Level.SEVERE, "No se pudo crear burbuja de mensaje del sistema", e);
            }
        });
    }


    public void mostrarMensajeRecibido(String alias, String msg) {

        if (msg == null) return;

        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/psp/chat/cliente/ui/BurbujaMsjReceptor.fxml"));
                HBox burbuja = loader.load();

                ControladorMsjBurbuja ctrl = loader.getController();
                ctrl.configurar(alias, msg, "");

                contenedorMensajes.getChildren().add(burbuja);

                scrollMensajes.layout();
                scrollMensajes.setVvalue(1.0);

            } catch (IOException e) {
                LOG.log(Level.SEVERE, "No se pudo crear burbuja de mensaje recibido", e);
            }
        });
    }

    /**
     * Callback llamado cuando el servidor envía la lista de contactos conectados.
     *
     * @param lista lista de contactos conectados actualmente
     */
     public void onListaContactosConectados(List<Contacto> lista) {

        if (lista == null) {
            return;
        }

        Platform.runLater(() -> {
            conectados.clear();
            conectados.addAll(lista);
        });
    }


}
