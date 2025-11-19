package psp.chat.cliente.net;

/**
 * Clase preparada para gestionar lógica de reconexión automática
 * al servidor en caso de caída de la conexión.
 *
 * Por ahora es un esqueleto que se integrará más adelante.
 */
public class GestionReconexion {

    private final ConexionCliente conexionCliente;

    /**
     * Crea una nueva gestión de reconexión asociada a una conexión concreta.
     *
     * @param conexionCliente conexión de cliente a supervisar.
     */
    public GestionReconexion(ConexionCliente conexionCliente) {
        this.conexionCliente = conexionCliente;
    }

    /**
     * Inicia un intento de reconexión.
     *
     * La estrategia concreta (intentos, backoff, etc.) se implementará
     * más adelante. De momento solo deja el punto de extensión claro.
     */
    public void intentarReconectar() {
        // TODO: implementar estrategia de reconexión (reintentos, delay, etc.).
    }

    /**
     * @return conexión de cliente asociada a esta gestión.
     */
    public ConexionCliente getConexionCliente() {
        return conexionCliente;
    }
}

