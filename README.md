# TazoDrops

Plugin de Paper 1.21.x: al matar una araña, hay 5% de probabilidad de que el
jugador reciba un "Tazo" — un ítem coleccionable con la imagen del Pokémon
que tú definas (usa una "cabeza de jugador" custom para mostrar la imagen,
así que **no necesita resource pack ni que el jugador instale nada**).

## 1. Cómo compilarlo

Necesitas Java 17+ y Maven instalados.

```bash
cd TazoDrops
mvn clean package
```

Esto genera `target/TazoDrops.jar`. Ese archivo va a la carpeta `plugins/`
de tu servidor Paper.

Si usas IntelliJ IDEA: abre la carpeta como proyecto Maven, y en el panel de
Maven (lado derecho) corre `package`.

## 2. Cómo subir tus imágenes de tazos

El plugin muestra la imagen usando un link directo. Pasos:

1. Sube tu imagen (como la que me mandaste, el tazo de Dewgong) a un host
   de imágenes que dé **links directos**, por ejemplo:
   - imgur.com (sube la imagen, click derecho sobre ella > "copiar dirección
     de la imagen" — debe terminar en `.png` o `.jpg`)
2. Pega ese link en `config.yml`, en el campo `image-url` del tazo
   correspondiente.
3. Reinicia el servidor o ejecuta `/tazodrops reload` en el juego.

**Importante:** la imagen debe ser cuadrada (idealmente 64x64 o 128x128 px)
para que se vea bien en el modelo de cabeza de jugador — puedes recortar la
imagen del tazo que me mandaste a un cuadrado centrado en el Pokémon.

## 3. Configuración (`config.yml`)

```yaml
drop-chance: 5.0          # % de probabilidad al matar una araña
include-cave-spider: false
give-directly: true        # true = va directo al inventario, false = se dropea en el piso
win-message: "&6¡Obtuviste un Tazo #%id% - %name%!"

tazos:
  - id: 87
    name: "Dewgong"
    type: "AGUA"
    image-url: "https://i.imgur.com/TU_LINK.png"
```

Puedes agregar tantos tazos como quieras a la lista — cuando cae un tazo,
el plugin elige uno al azar entre todos los que estén configurados.

## 4. Comandos

- `/tazodrops reload` — recarga el config.yml sin reiniciar el servidor
  (requiere ser op).

## 5. Estructura del proyecto

```
TazoDrops/
├── pom.xml
└── src/main/
    ├── java/com/tazos/plugin/
    │   ├── TazosPlugin.java          (clase principal)
    │   ├── items/
    │   │   ├── Tazo.java             (modelo de datos)
    │   │   └── TazoManager.java      (crea los ítems y maneja la lista)
    │   └── listeners/
    │       └── SpiderDeathListener.java  (lógica del drop al matar araña)
    └── resources/
        ├── plugin.yml
        └── config.yml
```
