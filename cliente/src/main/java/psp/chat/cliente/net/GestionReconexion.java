package psp.chat.cliente.net;

import java.util.logging.Logger;

/**
 * Clase preparada para gestionar lógica de reconexión automática
 * al servidor en caso de caída de la conexión
 */
public class GestionReconexion {

    private static final Logger LOG = Logger.getLogger(GestionReconexion.class.getName());

    private final ConexionCliente conexionCliente;

    /**
     * Crea una nueva gestión de reconexión asociada a una conexión concreta.
     *
     * @param conexionCliente conexión de cliente a supervisar.
     */
    public GestionReconexion(ConexionCliente conexionCliente) {

        if (conexionCliente == null) {

            throw new IllegalArgumentException("conexionCliente no puede ser null");

        }

        this.conexionCliente = conexionCliente;

    }

    /**
     * Inicia un intento de reconexión
     */
    public void intentarReconectar() {

        LOG.info("Intentando reconectar con el servidor (estrategia pendiente de implementar).");
        // TODO: implementar lógica de reconexión

    }

    /**
     * @return conexión de cliente
     */
    public ConexionCliente getConexionCliente() {
        return conexionCliente;
    }
}

