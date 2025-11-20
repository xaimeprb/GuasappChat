package psp.chat.general.modelo;

import java.util.Objects;
import java.util.UUID;

/**
 * Representa un contacto identificado de forma única en el sistema.
 *
 * Cada contacto tiene:
 *  - idContacto: identificador global único y persistente
 *  - ipRemota: información técnica (no define identidad)
 *  - aliasVisible: nombre mostrado al usuario
 *
 * NOTA IMPORTANTE:
 *   La identidad YA NO depende de la IP.
 *   La IP ahora es solo metadato informativo.
 */
public class Contacto {

    /** Identificador único del contacto (persistente). */
    private String idContacto;

    /** Última IP desde la que se ha conectado este contacto. */
    private String ipRemota;

    /** Alias que se muestra en la UI del cliente. */
    private String aliasVisible;


    /**
     * Constructor vacío requerido por Gson.
     * Garantiza inicialización segura.
     */
    public Contacto() {
        this.idContacto = UUID.randomUUID().toString();
        this.ipRemota = "";
        this.aliasVisible = "";
    }

    /**
     * Crea un contacto nuevo desde cero.
     *
     * @param ipRemota     IP desde la que se conecta el cliente
     * @param aliasVisible alias elegido por el usuario
     */
    public Contacto(String ipRemota, String aliasVisible) {

        this.idContacto = UUID.randomUUID().toString();

        this.ipRemota = (ipRemota != null) ? ipRemota : "";
        this.aliasVisible = (aliasVisible != null) ? aliasVisible : "";
    }


    /* ===========================
       Getters / Setters
       =========================== */

    public String getIdContacto() {
        return idContacto;
    }

    public String getIpRemota() {
        return ipRemota;
    }

    public void setIpRemota(String ipRemota) {
        this.ipRemota = (ipRemota != null) ? ipRemota : "";
    }

    public String getAliasVisible() {
        return aliasVisible;
    }

    public void setAliasVisible(String aliasVisible) {
        this.aliasVisible = (aliasVisible != null) ? aliasVisible : "";
    }


    /* ===========================
       Utilidad
       =========================== */

    /**
     * Descripción corta para logs y depuración.
     * Ejemplo: "JorgePalotes - 127.0.0.1"
     */
    public String descripcionCorta() {

        String alias = (aliasVisible != null && !aliasVisible.isBlank())
                ? aliasVisible
                : "(sin alias)";

        String ip = (ipRemota != null && !ipRemota.isBlank())
                ? ipRemota
                : "(sin IP)";

        return alias + " - " + ip;
    }


    @Override
    public String toString() {
        String textoAlias = (aliasVisible != null && !aliasVisible.isEmpty())
                ? aliasVisible
                : "(sin alias)";

        String textoIp = (ipRemota != null && !ipRemota.isEmpty())
                ? ipRemota
                : "(sin IP)";

        return textoAlias + " (" + textoIp + ")";
    }


    /* ===========================
       Identidad del contacto
       =========================== */

    /**
     * Dos contactos SON EL MISMO contacto si comparten idContacto.
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;

        if (!(obj instanceof Contacto)) return false;

        Contacto otro = (Contacto) obj;

        return Objects.equals(idContacto, otro.idContacto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idContacto);
    }
}
