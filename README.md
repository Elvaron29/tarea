100684238
Descripción General

El Sakila Manager es un sistema de gestión para la base de datos Sakila (una base de datos de ejemplo para tiendas de alquiler de DVD) que permite realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre diversas entidades como actores, películas, ciudades, países, entre otros.

Estructura del Proyecto
El sistema está organizado en los siguientes paquetes principales:

com.sakila.controllers - Contiene los controladores para manejar la interacción con el usuario
com.sakila.data - Maneja el acceso a la base de datos
com.sakila.models - Contiene las clases modelo que representan las entidades de la base de datos

Componentes Principales
1. Controladores
ActorController
Maneja las operaciones relacionadas con actores:

Listar todos los actores

Buscar actor por ID
Agregar nuevo actor
Actualizar actor existente
Eliminar actor

2. Gestores de Datos
ActorContext
Extiende DataContext e implementa operaciones específicas para la entidad Actor.

CityManager, CountryManager, FilmManager, LanguageManager
Implementan la interfaz IDataManager y proporcionan funcionalidad CRUD para sus respectivas entidades.

3. Modelos
Actor, City, Country, Film, Language
Clases que representan las entidades de la base de datos, con sus atributos y métodos correspondientes.
Entity
Clase abstracta que sirve como base para todas las entidades, conteniendo campos comunes como lastUpdate y active.

4. Conexión a Base de Datos
DatabaseConnection
Maneja la conexión a la base de datos MySQL usando el patrón Singleton.

Interfaces Importantes
IDataManager<T>
Define las operaciones básicas para el manejo de datos:

post(T item) - Crear un nuevo registro

get(int id) - Obtener un registro por ID

get() - Obtener todos los registros

get(String searchTerm) - Buscar registros

put(T item) - Actualizar un registro

delete(int id) - Eliminar un registro

iDatapost<T>
Similar a IDataManager pero con nombres de métodos diferentes:

create(T item)

findById(int id)

update(T item)

remove(int id)

findAll()

Clase Principal
SakilaManager
Punto de entrada del sistema que muestra un menú principal con las siguientes opciones:

Ver actores

Ver películas

Ver ciudades

Ver países

Ver direcciones

Ver películas rentadas (con año)

Ver ganancias totales por renta de películas

Ver suma total de todas las ganancias

Salir

La conexión se configura en DatabaseConnection con los siguientes parámetros:

URL: jdbc:mysql://localhost:3306/sakila?useSSL=false&serverTimezone=UTC
Usuario: root
Contraseña: pepe

Consideraciones de Implementación

El sistema utiliza JDBC para interactuar con MySQL
Se implementa el patrón DAO (Data Access Object) a través de las clases *Manager
Se manejan transacciones básicas a nivel de sentencias SQL
Se utiliza programación orientada a objetos con herencia y polimorfismo
La interfaz de usuario es por consola
