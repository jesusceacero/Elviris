# Proyecto Elviris

Proyecto sobre la gestión de datos reservas de eventos en un Pub.

Los Usuarios podran registrarse y loguearse en nuestra aplicación.
Una vez logueados podran ver el listado de eventos que el pub les ofrece, reservar un evento si es que quedan plazas y no ha pasado la fecha, ver su listado reservas y su perfil (Pudiendo cambiar su foto de usuario).
Los acministradores podran hacer lo mismos que los usuarios mas las funciones de administrador (ver detalle de los eventos con un listado de los usuarios reservados, editar eventos, añadir eventos y eliminar eventos).


***

#### Usuarios de prueba:
* [Rol : USER ](username: user@user.com, password: 123456)
* [Rol : ADMIN ](username: admin@admin.com, password: 123456)

***

#### Tecnologías usadas:
* [Spring Tool Suite](https://spring.io/)
* [Maven](https://maven.apache.org/)
* [Kotlin](https://kotlinlang.org/)
* [Android](https://www.android.com/)
* [Postgresql](https://www.postgresql.org/)
* IDE: [Idea](https://www.jetbrains.com/es-es/idea/) Necesario para arrancar la api rest (se debe arrancar desde el IDE)
* IDE: [Android Studio](https://developer.android.com/studio) Utilizado para la programación de la App

***


#### Variables de entorno necesarias en un archivo .env para el Api Rest:
* Descritas en application.properties

***


#### Usar la Api Rest:
* La aplicación esta subida api esta subida a heroku para que se puedra provar la app de android directamente.
* Para la prueva de dicha api y app en local, la aplicación esta subida al repositorio utilizando la base de datos h2 de Spring. 
* Debemos cambiar la URL BASE de la App de android a http://localhost:9000 y añadir un ClientID de ingur en el application.properties.
* La Api arrancara en el puerto 9000.

* En la clase EventoRepository hay datos de ejemplo comentados, descomentarlos para que se auto inserten la primera vez que arrancamos la aplicación.

***


## Endpoints

* Todos los las peticiones están en la colección de Postman adjuntada en en repositorio.


```