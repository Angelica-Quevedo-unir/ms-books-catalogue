# **ms-books-catalogue**

## **Descripción General**
El microservicio **ms-books-catalogue** es el núcleo de la gestión del catálogo de libros y el inventario de nuestra aplicación **Relatos de Papel**. Este microservicio expone una API RESTful que permite:

- **Gestión de Libros**: Crear, modificar (total o parcialmente), eliminar y consultar libros.
- **Gestión de Inventario**: Controlar el stock, la disponibilidad y el estado de los libros en el inventario.
- **Búsquedas Avanzadas**: Realizar consultas combinadas utilizando múltiples filtros, como título, autor, género, editorial y rango de fechas de publicación.
- **Health Check**: Monitorear el estado del microservicio mediante un endpoint dedicado.

Este microservicio está diseñado para ser escalable y eficiente, garantizando la integridad y disponibilidad de los datos en todo momento. Además, está estructurado en dos controladores principales: **Books** e **inventory**, lo que permite una clara separación de responsabilidades y facilita el mantenimiento y la expansión del sistema.
el proyecto esta diseñado bajos  los principios **REST** y utiliza **HATEOAS** (Hypermedia as the Engine of Application State) para proporcionar respuestas enriquecidas con enlaces que permiten navegar fácilmente entre los recursos

---

## **Estructura del Proyecto**
El proyecto sigue una arquitectura basada en capas para garantizar la separación de responsabilidades. A continuación, se detalla la estructura del proyecto:

ms-books-catalogue
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── es.unir.relatosdepapel.books
│   │   │       ├── controller       
│   │   │       ├── model           
│   │   │       ├── repository       
│   │   │       ├── service         
│   │   │       ├── hateoas         
│   │   │       └── dto              
│   │   └── resources
│   │       ├── application.properties          
│   └── test
│       └── java


---

## **Controladores Principales**
### **1. Controlador de Libros**
Este controlador gestiona toda la información relacionada con los libros, incluyendo títulos, autores, editoriales, géneros y fechas de publicación. Los endpoints principales son:

- **POST /api/v1/books**: Crea un nuevo libro en el catálogo.
- **GET /api/v1/books**: Obtiene una lista de todos los libros disponibles con paginación.
- **GET /api/v1/books/{id}**: Obtiene los detalles de un libro específico por su ID.
- **PUT /api/v1/books/{id}**: Actualiza todos los campos de un libro existente.
- **PATCH /api/v1/books/{id}**: Actualiza parcialmente los campos de un libro existente.
- **DELETE /api/v1/books/{id}**: Elimina un libro del catálogo.
- **GET /api/v1/books/isbn/{isbn}**: Obtiene un libro por su ISBN.
- **GET /api/v1/books/search**: Realiza búsquedas avanzadas utilizando filtros combinados.

### **2. Controlador de Inventario**
Este controlador se encarga de gestionar el stock y la disponibilidad de los libros. Los endpoints principales son:

- **GET /api/v1/inventory/{isbn}/availability**: Verifica la disponibilidad de un libro en el inventario.
- **PATCH /api/v1/inventory/{isbn}/stock/reduce**: Reduce el stock de un libro después de una compra confirmada.
- **PATCH /api/v1/inventory/{isbn}/stock/restore**: Restaura el stock de un libro después de una cancelación de compra.

---

## **Servicios**
### **1. Servicio de Libros (`IBookService`)**
Este servicio contiene la lógica de negocio relacionada con la gestión de libros. Algunos de los métodos principales son:

- **createBook(Book book)**: Crea un nuevo libro.
- **getAllBooks(Pageable pageable)**: Obtiene todos los libros con paginación.
- **getBookById(Long id)**: Obtiene un libro por su ID.
- **updateBook(Long id, Book book)**: Actualiza un libro existente.
- **deleteBook(Long id)**: Elimina un libro.
- **getBookByIsbn(String isbn)**: Obtiene un libro por su ISBN.
- **searchBooks(String title, String author, String isbn, String category, int page, int size, String sortField, String sortDirection)**: Realiza búsquedas avanzadas.
- **partialUpdateBook(Long bookId, Book partialUpdate)**: Actualiza parcialmente un libro.

### **2. Servicio de Inventario (`IBookInventoryService`)**
Este servicio contiene la lógica de negocio relacionada con la gestión del inventario. Algunos de los métodos principales son:

- **checkBookAvailability(String isbn, Long quantity)**: Verifica la disponibilidad de un libro.
- **reduceBookStock(String isbn, Long quantity)**: Reduce el stock de un libro.
- **restoreBookStock(String isbn, Long quantity)**: Restaura el stock de un libro.

---

## **HATEOAS (Hypermedia as the Engine of Application State)**
El microservicio utiliza **HATEOAS** para enriquecer las respuestas de la API con enlaces que permiten a los clientes navegar fácilmente entre los recursos. Por ejemplo, al obtener un libro, la respuesta incluye enlaces para actualizarlo, eliminarlo o consultar su disponibilidad en el inventario. Esto hace que la API sea más intuitiva y siga los principios RESTful.

Ejemplo de respuesta con HATEOAS:
```json
{
  "id": 1,
  "title": "El Señor de los Anillos",
  "author": "J.R.R. Tolkien",
  "isbn": "978-0544003415",
  "_links": {
    "self": { "href": "/api/v1/books/1" },
    "update": { "href": "/api/v1/books/1" },
    "delete": { "href": "/api/v1/books/1" },
    "inventory": { "href": "/api/v1/inventory/978-0544003415/availability" }
  }
}
```
---

## **Swagger UI**
El microservicio utiliza **Swagger UI** para documentar y probar la API de manera interactiva. Puedes acceder a la interfaz de Swagger en la siguiente URL una vez que el microservicio esté en ejecución:

```
http://localhost:8080/swagger-ui.html
```

### **Imagen de Swagger UI**
A continuación, se muestra una captura de pantalla de la interfaz de Swagger UI:

![Swagger UI](src/main/resources/static/swaggerone.png)
![Swagger UI](src/main/resources/static/swagger2.png)
*(Nota: Reemplaza la URL de la imagen con la captura de pantalla real de tu Swagger UI.)*
---

## **Health Check**
El microservicio expone su estado mediante el siguiente endpoint:
- **GET /admin/health**: Proporciona información sobre el estado del microservicio, indicando si está operativo y saludable.

---

## **Configuración del Proyecto**
### **Requisitos Previos**
- Java 17 o superior
- MySQL 8.x
- Maven 3.x
- Docker (opcional, para despliegue en contenedores)

### **Instalación y Ejecución**
1. Clona el repositorio del proyecto.
2. Configura las variables de entorno en `application.properties` para la conexión a la base de datos.
3. Ejecuta el siguiente comando para construir el proyecto:
   ```bash
   mvn clean install
   ```
4. Inicia el microservicio con:
   ```bash
   mvn spring-boot:run
   ```
5. Accede a la documentación de la API en `http://localhost:${PORT}/swagger-ui.html`.

### **Despliegue con Docker**
1. Construye la imagen Docker:
   ```bash
   docker build -t ms-books-catalogue .
   ```
2. Ejecuta el contenedor:
   ```bash
   docker run -p 8080:8080 ms-books-catalogue
   ```

---

