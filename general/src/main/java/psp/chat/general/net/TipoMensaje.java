package psp.chat.general.net;

/**
 * Tipos de contenido que puede transportar un mensaje
 */
public enum TipoMensaje {

    /**
     * Mensaje con contenido de texto plano
     */
    TEXTO,

    /**
     * Mensaje que referencia una imagen
     */
    IMAGEN,

    /**
     * Mensaje que referencia un archivo genérico
     */
    ARCHIVO
}
