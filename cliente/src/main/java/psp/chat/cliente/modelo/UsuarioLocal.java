package psp.chat.cliente.modelo;

/**
 * Representa al usuario local que está usando este cliente.
 *
 * De momento solo identifica al usuario por su alias, pero
 * se podría ampliar con más atributos (avatar, estado, etc.).
 */
public class UsuarioLocal {

    private final String alias;

    /**
     * Crea un nuevo usuario local.
     *
     * @param alias alias que se mostrará al resto de clientes.
     */
    public UsuarioLocal(String alias) {
        this.alias = alias;
    }

    /**
     * @return alias del usuario local.
     */
    public String getAlias() {
        return alias;
    }
}
