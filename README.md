# **ms-books-catalogue**

## **Descripción General**
El microservicio **ms-books-catalogue** es el núcleo de la gestión del catálogo de libros y el inventario de nuestra aplicación **Relatos de Papel**. Este microservicio expone una API RESTful que permite:

- **Gestión de Libros**: Crear, modificar (total o parcialmente), eliminar y consultar libros.
- **Gestión de Inventario**: Controlar el stock, la disponibilidad y el estado de los libros en el inventario.
- **Búsquedas Avanzadas**: Realizar consultas combinadas utilizando múltiples filtros, como título, autor, género, editorial y rango de fechas de publicación.
- **Health Check**: Monitorear el estado del microservicio mediante un endpoint dedicado.

Este microservicio está diseñado para ser escalable y eficiente, garantizando la integridad y disponibilidad de los datos en todo momento. Además, está estructurado en dos controladores principales: **Books** e **inventory**, lo que permite una clara separación de responsabilidades y facilita el mantenimiento y la expansión del sistema.
el proyecto esta diseñado bajos  los principios **REST** y utiliza **HATEOAS** (Hypermedia as the Engine of Application State) para proporcionar respuestas enriquecidas con enlaces que permiten navegar fácilmente entre los recursos


El microservicio **ms-book-catalogue** forma parte de la siguiente arquitectura:

![Swagger UI](src/main/resources/static/architecture.jpg)

Este servicio expone los endpoints relacionados con el catálogo de libros a través de un [**API Gateway**](https://github.com/Angelica-Quevedo-unir/ms-springcloud-gateway). Los endpoints son registrados automáticamente en [**Eureka Server**](https://github.com/Angelica-Quevedo-unir/ms-eureka-server) , lo que permite la detección dinámica de servicios.
Además, los servicios internos encargados de la gestión de inventario son consumidos de forma interna y directa por el componente de [**ms-books-payments**](https://github.com/Angelica-Quevedo-unir/ms-books-payment), garantizando la validación del stock disponible antes de procesar un pago.


## **Estructura del Proyecto**
El proyecto sigue una arquitectura basada en capas para garantizar la separación de responsabilidades. A continuación, se detalla la estructura del proyecto:

ms-books-catalogue
```
📁 src
├── 📁 main
│   ├── 📁 java
│   │   └── 📁 es.unir.relatosdepapel.books
│   │       ├── 📁 controller       # Controladores REST o de API
│   │       ├── 📁 model            # Entidades JPA o modelos de datos
│   │       ├── 📁 repository       # Repositorios JPA o interfaces de acceso a datos
│   │       ├── 📁 service          # Lógica de negocio y servicios
│   │       ├── 📁 hateoas          # Configuración y ensambladores HATEOAS
│   │       ├── 📁 dto              # Objetos de transferencia de datos
│   │       ├── 📁 config           # Configuración de la aplicación (Beans, seguridad, etc.)
│   │       └── 📁 exception        # Manejo de excepciones personalizadas
│   └── 📁 resources
│       └── 📄 application.properties  # Configuración de la aplicación
├── 📁 test
│   ├── 📁 java                     # Clases de pruebas unitarias y de integración
│   └── 📁 resources                # Archivos de configuración de prueba
```

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
Este controlador es responsable de gestionar el stock y la disponibilidad de los libros. Los endpoints principales proporcionan operaciones internas, accesibles de forma interna por el microservicio **ms-books-payments**, permitiendo validar la lógica de inventario antes de procesar un pago. Estos endpoints **no son accesibles externamente** a través del **API Gateway**.

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
http://${HOST}/book-catalogue/swagger-ui/index.html#
```

### **Imagen de Swagger UI**
A continuación, se muestra una captura de pantalla de la interfaz de Swagger UI:

![Swagger UI](src/main/resources/static/swaggerone.png)
![Swagger UI](src/main/resources/static/swagger2.png)
---

## **Health Check**
El microservicio expone su estado mediante el siguiente endpoint:
- **GET /admin/health**: Proporciona información sobre el estado del microservicio, indicando si está operativo y saludable.
```
http://${HOST}/book-catalogue/admin/health
```
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
5. Accede a la documentación de la API en `http://${HOST}/swagger-ui.html`.
6. Valida el `http://${HOST}/book-catalogue/admin/health`

### **📄 Configuración de la Base de Datos**

Para ejecutar este proyecto, debes contar con **MySQL** instalado y una base de datos llamada **`books_catalogue_db`** en funcionamiento. Asegúrate de configurar correctamente los parámetros de conexión en el archivo **`application.properties`**. A continuación, se proporcionan las configuraciones necesarias y los scripts para la creación de tablas y la carga de datos iniciales.

---

## **🚀 Configuración de la conexión a la base de datos**
En el archivo **`application.properties`** (ubicado en `src/main/resources`), configura los siguientes valores:

```properties
# Datasource configuration
spring.datasource.url=jdbc:mysql://localhost:3306/books_catalogue_db
spring.datasource.username=user
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

---

## **🛠️ Scripts de creación de tablas**

Ejecuta los siguientes scripts SQL en la base de datos **`books_catalogue_db`**:

### **1. Creación de la tabla `books`**
```sql
CREATE TABLE books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Identificador único del libro',
    title VARCHAR(255) NOT NULL COMMENT 'Título del libro',
    author VARCHAR(255) NOT NULL COMMENT 'Autor del libro',
    publication_date DATE NOT NULL COMMENT 'Fecha de publicación del libro',
    category VARCHAR(100) NOT NULL COMMENT 'Categoría del libro',
    isbn VARCHAR(13) NOT NULL UNIQUE COMMENT 'Código ISBN único del libro',
    description TEXT COMMENT 'Descripción breve del libro',
    price DECIMAL(10, 2) NOT NULL COMMENT 'Precio del libro',
    image_url VARCHAR(2083) COMMENT 'URL de la imagen de la portada',
    is_digital BOOLEAN DEFAULT FALSE COMMENT 'Indica si el libro es digital (TRUE) o físico (FALSE)',
    rating TINYINT CHECK (rating >= 1 AND rating <= 5) COMMENT 'Valoración del libro (1 a 5)',
    visibility BOOLEAN DEFAULT TRUE COMMENT 'Indica si el libro es visible (TRUE) o no (FALSE)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación del registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha de última actualización del registro'
) COMMENT 'Tabla que almacena los libros del catálogo';


-- Índices individuales para búsquedas frecuentes
CREATE INDEX idx_books_title ON books (title);
CREATE INDEX idx_books_author ON books (author);
CREATE INDEX idx_books_category ON books (category);


-- Índice compuesto para búsquedas combinadas comunes
CREATE INDEX idx_books_title_author ON books (title, author);


-- Índice único para ISBN
CREATE UNIQUE INDEX idx_books_isbn ON books (isbn);

```

### **2. Creación de la tabla `book_inventory`**
```sql
CREATE TABLE book_inventory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Identificador del inventario',
    book_id BIGINT NOT NULL COMMENT 'Referencia al libro',
    stock INT NOT NULL DEFAULT 0 COMMENT 'Cantidad de libros físicos disponibles',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación del inventario',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha de última actualización del inventario',

    -- Llave foránea que relaciona la tabla de libros
    CONSTRAINT fk_book FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);
```

---

## **📦 Población de datos inicial**

### **Inserta libros en la tabla `books`:**
```sql
INSERT INTO books (title, author, publication_date, category, isbn, description, price, image_url, is_digital, rating, visibility)
VALUES 
('El código Da Vinci', 'Dan Brown', '2003-03-18', 'Misterio', '9780307474278', 'Un thriller que combina historia, religión y códigos secretos.', 19.99, 'https://example.com/images/davinci.jpg', FALSE, 5, TRUE),


('Cien años de soledad', 'Gabriel García Márquez', '1967-06-05', 'Realismo Mágico', '9780060883287', 'La historia épica de la familia Buendía en el pueblo ficticio de Macondo.', 14.50, 'https://example.com/images/cienanos.jpg', FALSE, 5, TRUE),


('1984', 'George Orwell', '1949-06-08', 'Distopía', '9780451524935', 'Una novela que advierte sobre los peligros del totalitarismo.', 9.99, 'https://example.com/images/1984.jpg', FALSE, 5, TRUE),


('Harry Potter y la piedra filosofal', 'J.K. Rowling', '1997-06-26', 'Fantasía', '9780747532699', 'El comienzo de la saga de Harry Potter y su vida en Hogwarts.', 12.99, 'https://example.com/images/harry1.jpg', FALSE, 4, TRUE),


('El señor de los anillos: La comunidad del anillo', 'J.R.R. Tolkien', '1954-07-29', 'Fantasía', '9780261103573', 'Primera parte de la épica aventura de la Tierra Media.', 18.50, 'https://example.com/images/lotr1.jpg', FALSE, 5, TRUE),


('El alquimista', 'Paulo Coelho', '1988-05-01', 'Ficción', '9780061122415', 'Una historia inspiradora sobre la búsqueda del destino.', 11.25, 'https://example.com/images/alquimista.jpg', FALSE, 4, TRUE),


('Los pilares de la Tierra', 'Ken Follett', '1989-09-29', 'Histórica', '9780451225245', 'Una historia épica de intriga, amor y religión en la Edad Media.', 22.95, 'https://example.com/images/pilares.jpg', FALSE, 5, TRUE),


('La sombra del viento', 'Carlos Ruiz Zafón', '2001-04-12', 'Misterio', '9780143126393', 'Una intriga literaria ambientada en la Barcelona de la posguerra.', 15.75, 'https://example.com/images/sombra.jpg', FALSE, 5, TRUE),


('Crónica de una muerte anunciada', 'Gabriel García Márquez', '1981-04-01', 'Ficción', '9781400034710', 'Una novela que relata los eventos previos a un asesinato anunciado.', 10.50, 'https://example.com/images/cronica.jpg', FALSE, 4, TRUE),


('El hombre en busca de sentido', 'Viktor Frankl', '1946-01-01', 'Autoayuda', '9780807014295', 'Una reflexión sobre la resiliencia humana basada en experiencias del Holocausto.', 13.75, 'https://example.com/images/sentido.jpg', TRUE, 5, TRUE);

```

### **Inserta datos iniciales en la tabla `book_inventory`:**
```sql
INSERT INTO book_inventory (book_id, stock, created_at, updated_at) 
VALUES 
(1, 10, NOW(), NOW()),  -- El código Da Vinci
(2, 15, NOW(), NOW()),  -- Cien años de soledad
(3, 8, NOW(), NOW()),   -- 1984
(4, 20, NOW(), NOW()),  -- Harry Potter y la piedra filosofal
(5, 12, NOW(), NOW()),  -- El señor de los anillos: La comunidad del anillo
(6, 5, NOW(), NOW()),   -- El alquimista
(7, 18, NOW(), NOW()),  -- Los pilares de la Tierra
(8, 14, NOW(), NOW()),  -- La sombra del viento
(9, 9, NOW(), NOW()),   -- Crónica de una muerte anunciada
(10, 25, NOW(), NOW()); -- El hombre en busca de sentido
```
