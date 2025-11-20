package psp.chat.server.modelo;

import psp.chat.general.modelo.Contacto;
import psp.chat.server.net.SesionCliente;

import java.time.LocalDateTime;

/**
 * Representa a un cliente actualmente conectado al servidor.
 *
 * Contiene:
 *  - Información de contacto (IP y alias).
 *  - Momento de conexión.
 *  - Referencia a la sesión de red asociada.
 */
public class ClienteConectado {

    private final Contacto contacto;
    private final LocalDateTime conectadoDesde;
    private SesionCliente sesionCliente;

    public ClienteConectado(Contacto contacto) {
        this.contacto = contacto;
        this.conectadoDesde = LocalDateTime.now();
    }

    public Contacto getContacto() {
        return contacto;
    }

    public LocalDateTime getConectadoDesde() {
        return conectadoDesde;
    }

    public SesionCliente getSesionCliente() {
        return sesionCliente;
    }

    public void setSesionCliente(SesionCliente sesionCliente) {
        this.sesionCliente = sesionCliente;
    }

    /**
     * Devuelve una descripción breve para mostrar en la UI.
     *
     * @return texto del tipo "alias (ip)".
     */
    public String descripcionCorta() {
        String alias = contacto.getAliasVisible();
        String ip = contacto.getIpRemota();

        String textoAlias;
        if (alias == null || alias.isEmpty()) {
            textoAlias = "(sin alias)";
        } else {
            textoAlias = alias;
        }

        String textoIp;
        if (ip == null || ip.isEmpty()) {
            textoIp = "(sin IP)";
        } else {
            textoIp = ip;
        }

        return textoAlias + " - " + textoIp;
    }

    @Override
    public String toString() {
        return descripcionCorta();
    }
}
