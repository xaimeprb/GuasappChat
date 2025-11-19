package psp.chat.general.net;

/**
 * Tipos de contenido que puede transportar un mensaje.
 *
 * Por ahora solo se usa TEXTO, pero la enumeración se deja
 * preparada para ampliar a otros tipos.
 */
public enum TipoMensaje {

    /**
     * Mensaje con contenido de texto plano.
     */
    TEXTO,

    /**
     * Mensaje que referencia una imagen.
     */
    IMAGEN,

    /**
     * Mensaje que referencia un archivo genérico.
     */
    ARCHIVO
}
