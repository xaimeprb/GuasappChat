package psp.chat.general.net;

/**
 * Comandos de alto nivel que el cliente y el servidor
 * intercambian a través del socket.
 *
 * Cada mensaje en la red lleva un {@link TipoComando} y
 * un payload en formato JSON.
 */
public enum TipoComando {

    /**
     * Autenticación / identificación del cliente ante el servidor.
     */
    LOGIN,

    /**
     * Petición del cliente para obtener la lista de conversaciones.
     */
    LISTA_CONVERSACIONES,

    /**
     * Respuesta del servidor con la lista de conversaciones.
     */
    LISTA_CONVERSACIONES_RESPUESTA,

    /**
     * Petición de historial completo de una conversación concreta.
     */
    HISTORIAL_CONVERSACION,

    /**
     * Respuesta del servidor con el historial completo de una conversación.
     */
    HISTORIAL_CONVERSACION_RESPUESTA,

    /**
     * Envío de un nuevo mensaje en una conversación.
     */
    NUEVO_MENSAJE,

    /**
     * Confirmación genérica de operación correcta.
     */
    ACK,

    /**
     * Notificación de error (mensaje de texto en el payload).
     */
    ERROR
}
