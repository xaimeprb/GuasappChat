package psp.chat.general.modelo;

/**
 * Representa un contacto conocido por el sistema.
 * Se usa para asociar una IP u otro identificador técnico
 * con un alias legible para el usuario.
 */
public class Contacto {

    private String ipRemota;
    private String aliasVisible;

    /**
     * Constructor vacío necesario para librerías de serialización como Gson.
     */
    public Contacto() {
    }

    /**
     * Crea un contacto completamente inicializado.
     *
     * @param ipRemota    IP o identificador de la otra parte.
     * @param aliasVisible alias amigable que se mostrará en la UI.
     */
    public Contacto(String ipRemota, String aliasVisible) {
        this.ipRemota = ipRemota;
        this.aliasVisible = aliasVisible;
    }

    /**
     * @return IP o identificador remoto del contacto.
     */
    public String getIpRemota() {
        return ipRemota;
    }

    /**
     * Establece la IP o identificador remoto del contacto.
     *
     * @param ipRemota nueva IP o identificador.
     */
    public void setIpRemota(String ipRemota) {
        this.ipRemota = ipRemota;
    }

    /**
     * @return alias visible asociado al contacto.
     */
    public String getAliasVisible() {
        return aliasVisible;
    }

    /**
     * Cambia el alias visible.
     *
     * @param aliasVisible nuevo alias a mostrar.
     */
    public void setAliasVisible(String aliasVisible) {
        this.aliasVisible = aliasVisible;
    }
}
