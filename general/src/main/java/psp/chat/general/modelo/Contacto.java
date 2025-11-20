package psp.chat.general.modelo;

/**
 * Representa un contacto conocido por el sistema.
 * Asocia una IP o identificador técnico con un alias visible.
 */
public class Contacto {

    private String ipRemota;
    private String aliasVisible;

    /**
     * Constructor vacío requerido por librerías como Gson.
     */
    public Contacto() {

        this.ipRemota = "";
        this.aliasVisible = "";

    }

    /**
     * Crea un contacto con datos iniciales
     *
     * @param ipRemota IP o identificador remoto
     * @param aliasVisible alias visible
     */
    public Contacto(String ipRemota, String aliasVisible) {

        if (ipRemota != null) {

            this.ipRemota = ipRemota;

        } else {

            this.ipRemota = "";

        }

        if (aliasVisible != null) {

            this.aliasVisible = aliasVisible;

        } else {

            this.aliasVisible = "";

        }

    }

    /**
     * @return IP o identificador remoto
     */
    public String getIpRemota() {
        return ipRemota;
    }

    /**
     * Establece la IP remota del contacto
     */
    public void setIpRemota(String ipRemota) {

        if (ipRemota != null) {

            this.ipRemota = ipRemota;

        } else {

            this.ipRemota = "";

        }

    }

    /**
     * @return alias visible del contacto
     */
    public String getAliasVisible() {
        return aliasVisible;
    }

    /**
     * Cambia el alias visible del contacto.
     */
    public void setAliasVisible(String aliasVisible) {

        if (aliasVisible != null) {

            this.aliasVisible = aliasVisible;

        } else {

            this.aliasVisible = "";

        }

    }

    @Override
    public String toString() {
        return "Contacto{ip='" + ipRemota + "', alias='" + aliasVisible + "'}";
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {

            return true;

        }

        if (!(obj instanceof Contacto)) {

            return false;

        }

        Contacto otro = (Contacto) obj;
        return ipRemota.equals(otro.ipRemota);

    }

    @Override
    public int hashCode() {
        return ipRemota.hashCode();
    }
}
