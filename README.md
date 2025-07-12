RecetApp
Descripción

RecetApp es una aplicación móvil de recetas desarrollada para Android,
que permite a los usuarios explorar recetas, marcarlas como favoritas
y filtrarlas por categorías. El proyecto utiliza Room para la persistencia local
y notificaciones push locales para mejorar la experiencia de usuario.
Registro de usuario

Desde la versión actual, la aplicación solicita al usuario su nombre y
correo electrónico la primera vez que se inicia.
Estos datos se guardan de forma local en el dispositivo, garantizando una
experiencia personalizada sin necesidad de contraseñas
ni gestión de cuentas externas.
Si el usuario ya se ha registrado, la aplicación lo lleva directamente
a la pantalla principal en futuros inicios.
Colaboración y control de versiones

El desarrollo de esta aplicación se ha realizado colaborativamente
usando GitHub como sistema de control de versiones
para facilitar la organización y el trabajo en equipo.

Repositorio GitHub:
https://github.com/superpin6/RecetApp
Instalación y ejecución

    Abre Android Studio e importa el proyecto desde la carpeta incluida en este archivo ZIP.

    Conecta un emulador o dispositivo Android físico.

    Haz clic en “Run” para compilar y ejecutar la aplicación.

    Nota:
    Si tienes problemas con permisos de notificaciones en Android 13+,
    acepta el permiso al iniciarse la app.

Funcionalidades principales

    Registro inicial con nombre y correo electrónico (sin contraseñas).

    Visualización de recetas con imágenes y descripciones.

    Filtro de recetas por categoría.

    Añadir y eliminar recetas de favoritos.

    Notificaciones push locales
    (desaparecen automáticamente para no molestar la navegación del usuario).

    Persistencia de favoritos mediante Room Database.

    Interfaz adaptada y coherente en todas las pantallas.

¡Gracias por revisar nuestro proyecto!
