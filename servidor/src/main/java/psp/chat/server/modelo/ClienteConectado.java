package psp.chat.server.modelo;

import psp.chat.general.modelo.Contacto;
import psp.chat.server.net.SesionCliente;

import java.time.LocalDateTime;

/**
 * Representa a un cliente actualmente conectado al servidor.
 *
 * Contiene:
 *   - Información persistente del contacto (idContacto, alias, ip).
 *   - Momento en el que se estableció la conexión.
 *   - La sesión de red activa asociada al socket.
 *
 * IMPORTANTE:
 *   La identidad del usuario YA NO depende de la IP.
 *   Se basa únicamente en el idContacto generado al crear el objeto Contacto.
 */
public class ClienteConectado {

    /** Contacto persistente asociado a este cliente */
    private final Contacto contacto;

    /** Momento exacto en el que se conectó al servidor */
    private final LocalDateTime conectadoDesde;

    /** Sesión de red activa para este cliente */
    private SesionCliente sesionCliente;

    /**
     * Crea la representación de un cliente conectado.
     *
     * @param contacto contacto persistente asociado al cliente
     */
    public ClienteConectado(Contacto contacto) {

        if (contacto == null) {

            throw new IllegalArgumentException("Contacto no puede ser null");

        }

        this.contacto = contacto;
        this.conectadoDesde = LocalDateTime.now();

    }

    /**
     * @return contacto persistente del cliente
     */
    public Contacto getContacto() {
        return contacto;
    }

    /**
     * @return fecha/hora exacta de conexión
     */
    public LocalDateTime getConectadoDesde() {
        return conectadoDesde;
    }

    /**
     * @return sesión activa del cliente o null si aún no está asignada
     */
    public SesionCliente getSesionCliente() {
        return sesionCliente;
    }

    /**
     * Asigna la sesión de socket asociada a este cliente.
     *
     * @param sesionCliente sesión asociada
     */
    public void setSesionCliente(SesionCliente sesionCliente) {
        this.sesionCliente = sesionCliente;
    }

    /**
     * @return descripción cómoda para UI: alias + " (" + IP + ")"
     */
    public String descripcionCorta() {
        return contacto.descripcionCorta();
    }

    @Override
    public String toString() {
        return descripcionCorta();
    }
}
