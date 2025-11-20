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
     * Crea un nuevo usuario local
     *
     * @param alias alias que se mostrará al resto de clientes
     */
    public UsuarioLocal(String alias) {

        if (alias != null) {

            this.alias = alias;

        } else {

            this.alias = "";

        }

    }

    /**
     * @return alias del usuario local
     */
    public String getAlias() {
        return alias;
    }

    @Override
    public String toString() {

        return "UsuarioLocal {alias='" + alias + "'}";

    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {

            return true;

        }

        if (!(obj instanceof UsuarioLocal)) {

            return false;

        }

        UsuarioLocal otro = (UsuarioLocal) obj;
        return alias.equals(otro.alias);

    }

    @Override
    public int hashCode() {

        return alias.hashCode();

    }

}
