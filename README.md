## Proyecto para la materia Orientación a Objetos 2

Solución elegida: Sistema de Tickets genérico.
- - -
- Versión de Java 21 o superior.
- Maven 3 o superior.
- Lombok instalado en el IDE.
- MySQL como Base de Datos.
- Crear una Base de Datos con la siguiente consulta: create database if not exists gestion-tickets;
- Abrir el proyecto y revisar que estén las dependencias descargadas, sino abrir una terminal en la raíz del proyecto y ejecutar esta instrucción: mvn clean install
- Configurar las variables de entorno para que el archivo application.yml las reconozca antes de iniciar la aplicación, haciendo clic derecho en el proyecto -> Run as  -> Run Configurations -> Environment:
	+ DB_URL -> colocar la url de la base de datos.
	+ DB_USERNAME -> colocar su usuario de la base de datos.
	+ DB_PASSWORD -> colocar tu contraseña de la base de datos.
- Ejecutar el proyecto.
- Si todo esta correcto aparece que la aplicación inicio en menos de 10 segundos en el puerto 8080.
- Abrir el navegador e ir a la siguiente url: http//localhost:8080
- Para una demo de cada rol:
  - Para ver como Cliente:
    - Usuario: cliente1
    - Contraseña: cliente123
  - Para ver como Empleado:
    - Usuario: empleado1
    - Contraseña: empleado123
  - Para ver como Administrador:
    - Usuario: admin
    - Contraseña: admin123


