package psp.chat.cliente.modelo;

/**
 * Representa los datos de un contacto en el cliente
 * Contiene la IP remota y el alias asignado por el usuario
 */
public class ContactoLocal {

    private final String ipRemota;
    private String alias;

    /**
     * Crea un nuevo contacto local
     *
     * @param ipRemota IP o identificador remoto
     * @param alias alias del contacto, si es null usamos ""
     */
    public ContactoLocal(String ipRemota, String alias) {

        if (ipRemota == null) {

            throw new IllegalArgumentException("La IP remota no puede ser null");

        }

        this.ipRemota = ipRemota;

        if (alias != null) {

            this.alias = alias;

        } else {

            this.alias = "";

        }

    }

    /**
     * @return IP o identificador remoto del contacto.
     */
    public String getIpRemota() {
        return ipRemota;
    }

    /**
     * @return alias que se mostrará para este contacto.
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Permite cambiar el alias asignado al contacto.
     *
     * @param alias nuevo alias.
     */
    public void setAlias(String alias) {

        if (alias != null) {

            this.alias = alias;

        } else {

            this.alias = "";

        }

    }

    @Override
    public String toString() {

        return "ContactoLocal {ip='" + ipRemota + "', alias='" + alias + "'}";

    }

    /**
     * Compara contactos por IP remota.
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {

            return true;

        }

        if (!(obj instanceof ContactoLocal)) {

            return false;

        }

        ContactoLocal otro = (ContactoLocal) obj;
        return ipRemota.equals(otro.ipRemota);
    }

    @Override
    public int hashCode() {

        return ipRemota.hashCode();

    }

}
