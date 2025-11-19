# GuasappChat — Cliente/Servidor de mensajería en JavaFX + Maven

Proyecto modular para PSP (Programación de Servicios y Procesos).

---

## Descripción del proyecto

GuasappChat es una aplicación completa de mensajería cliente-servidor desarrollada en Java 21, JavaFX 21 y Maven multi-módulo, con arquitectura limpia y modular.

El proyecto incluye:

- Cliente JavaFX con interfaz inspirada en WhatsApp Web.
- Servidor (UI separada) que gestiona múltiples clientes conectados.
- Módulo general con toda la lógica compartida del protocolo y modelo de datos.
- Persistencia local del cliente: conversaciones guardadas en ficheros JSON.
- Comunicación en tiempo real mediante sockets y un protocolo propio JSON.
- Recuperación automática de conversaciones al reconectar.
- Sistema de alias configurables por IP y almacenamiento local.

---

## Arquitectura del Proyecto

El proyecto está organizado como Maven multi-módulo:

GuasappChat/
│
├── pom.xml                ← Proyecto padre
│
├── general/               ← Lógica común (modelo + protocolo + utilidades)
│   ├── pom.xml
│   └── src/main/java/psp/chat/general/
│
├── servidor/              ← Aplicación del servidor (UI JavaFX)
│   ├── pom.xml
│   └── src/main/java/psp/chat/server/
│
└── cliente/               ← Aplicación del cliente (UI JavaFX)
    ├── pom.xml
    └── src/main/java/psp/chat/cliente/

---

## Módulo: general

Contiene el modelo de datos, protocolo de red, serialización JSON y estructuras reutilizables por cliente y servidor.

Incluye clases como:

- Mensaje
- Conversacion
- ResumenConversacion
- TipoComando
- EmpaquetadoDatos
- JsonUtil

---

## Módulo: cliente

Aplicación JavaFX que gestiona:

- Login
- Lista de conversaciones
- Vista estilo WhatsApp
- Envío/recepción de mensajes en tiempo real
- Reconexión automática
- Persistencia local en ficheros JSON

Pantallas principales (FXML):

- LoginView.fxml
- MainClienteView.fxml
- ItemConversacion.fxml
- BurbujaMsjEmisor.fxml
- BurbujaMsjReceptor.fxml

---

## Módulo: servidor

Aplicación JavaFX independiente donde se mostrarán:

- Clientes conectados
- Logs de tráfico
- Estado del servidor

(Interfaz en desarrollo)

---

## Protocolo de Comunicación

La comunicación cliente-servidor es vía sockets TCP, usando paquetes JSON.

### Formato estándar:

{
  "comando": "NUEVO_MENSAJE",
  "payloadJson": "{...}"
}

### Comandos principales:

- LOGIN
- LISTA_CONVERSACIONES
- HISTORIAL_CONVERSACION
- NUEVO_MENSAJE
- ACK
- ERROR

El servidor procesa cada comando y responde con paquetes empaquetados.

---

## Persistencia Local (Cliente)

Cada conversación del cliente se guarda en un fichero JSON.

Al iniciar sesión o reconectar, el cliente:

1. Recupera la lista de contactos y conversaciones.
2. Carga desde disco los mensajes previos.
3. Reconstruye el historial visual automáticamente.

Esto permite continuidad incluso si:

- El servidor se reinicia
- El cliente pierde conexión
- Se cierra la aplicación

---

## Cómo ejecutar el proyecto

### Requisitos

- JDK 21 (Temurin recomendado)
- Maven 3.9+ (IntelliJ ya lo incluye)
- IntelliJ IDEA

---

### Ejecutar el Cliente

Desde IntelliJ (ventana Maven):

cliente → Plugins → javafx → run

O por consola:

cd cliente  
mvn javafx:run

---

### Ejecutar el Servidor

(Se activará en cuanto esté implementado)

cd servidor  
mvn javafx:run

---

## Compilación Completa

En la raíz del proyecto:

mvn clean install

Esto compila:

- general  
- servidor  
- cliente

---

## Tecnologías utilizadas

| Tecnología        | Uso                                         |
|-------------------|---------------------------------------------|
| Java 21           | Lógica principal                            |
| JavaFX 21         | Interfaz gráfica                            |
| Maven multi-módulo| Gestión y estructura del proyecto           |
| Sockets TCP       | Comunicación en tiempo real                 |
| Gson 2.11         | Serialización JSON                          |
| FXML + CSS        | Diseño de UI                                |
| Modularización    | Separación entre cliente, servidor y general|

---

## Características implementadas hasta ahora

- Base del proyecto multi-módulo consolidada  
- Eliminación de clases dummy e inconsistencias  
- Protocolo JSON unificado  
- Modelo completo del sistema  
- JavaFX bien configurado con Maven  
- Cliente JavaFX operativo  
- Vistas FXML estilo WhatsApp  
- Reconexión automática  
- Persistencia local JSON  
- .gitignore profesional  
- Repositorio limpio y preparado para trabajo en equipo  

---

## Próximos pasos del roadmap

- Construcción de la UI del servidor  
- Sincronización cliente ↔ servidor en tiempo real  
- Notificaciones de nuevos mensajes  
- Gestión avanzada de reconexiones  
- Exportación de conversaciones  
- Cifrado básico de mensajes  
- Deploy del servidor en una máquina externa  

---

## Equipo

🔗 [Jaime Pérez Roget Blanco](https://github.com/xaimeprb)
🔗 [Sofía Abid Hajjar](https://github.com/sofiacfgsdam)   

---

## Licencia

Proyecto educativo de DAM / PSP.  
Uso libre no comercial.
