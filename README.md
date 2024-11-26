# API GraphQL con Spring Boot 3.4 y Base de Datos H2

Este proyecto es una implementación sencilla de una API GraphQL utilizando Spring Boot 3.4 y una base de datos en memoria H2.

## Requisitos Previos

- **Java 23**: Asegúrate de tener instalado JDK 23 o superior.
- **Maven 4.0+**: Para la gestión de dependencias y construcción del proyecto.

## Configuración del Proyecto

1. **Clonar el Repositorio**

   ```bash
   git clone https://github.com/sorodriguezz/graphql-simple-springboot.git
   cd graphql-simple-springboot
   ```

2. **Agregar Dependencias**

   En el archivo `pom.xml`, asegúrate de incluir las siguientes dependencias:

   ```xml
   <dependencies>
       <!-- Dependencia para Spring Web -->
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-web</artifactId>
       </dependency>
       <!-- Dependencia para Spring Data JPA -->
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-data-jpa</artifactId>
       </dependency>
       <!-- Dependencia para H2 Database -->
       <dependency>
           <groupId>com.h2database</groupId>
           <artifactId>h2</artifactId>
           <scope>runtime</scope>
       </dependency>
       <!-- Dependencia para Spring for GraphQL -->
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-graphql</artifactId>
       </dependency>
   </dependencies>
   ```

3. **Configurar la Base de Datos H2**

   En el archivo `src/main/resources/application.properties`, agrega la siguiente configuración:

   ```properties
   # Habilitar la consola H2
   spring.h2.console.enabled=true
   # Ruta de la consola H2
   spring.h2.console.path=/h2-console
   # URL de la base de datos
   spring.datasource.url=jdbc:h2:mem:testdb
   # Usuario y contraseña
   spring.datasource.username=sa
   spring.datasource.password=
   # Mostrar las consultas SQL en la consola
   spring.jpa.show-sql=true
   # Crear y actualizar las tablas automáticamente
   spring.jpa.hibernate.ddl-auto=update
   ```

## Definición del Esquema GraphQL

Crea un archivo de esquema GraphQL en `src/main/resources/graphql/schema.graphqls` con el siguiente contenido:

```graphql
type Query {
    allBooks: [Book]
    bookById(id: ID!): Book
}

type Mutation {
    createBook(title: String!, author: String!): Book
}

type Book {
    id: ID!
    title: String!
    author: String!
}
```

## Implementación de la Lógica de Negocio

1. **Entidad JPA**

   Crea la clase `Book` en el paquete `com.example.demo.model`:

   ```java
   package com.example.demo.model;

   import javax.persistence.Entity;
   import javax.persistence.GeneratedValue;
   import javax.persistence.GenerationType;
   import javax.persistence.Id;

   @Entity
   public class Book {
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       private Long id;
       private String title;
       private String author;

       // Getters y setters
   }
   ```

2. **Repositorio**

   Crea la interfaz `BookRepository` en el paquete `com.example.demo.repository`:

   ```java
   package com.example.demo.repository;

   import com.example.demo.model.Book;
   import org.springframework.data.jpa.repository.JpaRepository;

   public interface BookRepository extends JpaRepository<Book, Long> {
   }
   ```

3. **Resolvers de GraphQL**

   Crea la clase `BookResolver` en el paquete `com.example.demo.resolver`:

   ```java
   package com.example.demo.resolver;

   import com.example.demo.model.Book;
   import com.example.demo.repository.BookRepository;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.graphql.data.method.annotation.Argument;
   import org.springframework.graphql.data.method.annotation.MutationMapping;
   import org.springframework.graphql.data.method.annotation.QueryMapping;
   import org.springframework.stereotype.Controller;

   import java.util.List;
   import java.util.Optional;

   @Controller
   public class BookResolver {

       @Autowired
       private BookRepository bookRepository;

       @QueryMapping
       public List<Book> allBooks() {
           return bookRepository.findAll();
       }

       @QueryMapping
       public Optional<Book> bookById(@Argument Long id) {
           return bookRepository.findById(id);
       }

       @MutationMapping
       public Book createBook(@Argument String title, @Argument String author) {
           Book book = new Book();
           book.setTitle(title);
           book.setAuthor(author);
           return bookRepository.save(book);
       }
   }
   ```

## Ejecución de la Aplicación

Para ejecutar la aplicación, utiliza el siguiente comando de Maven:

```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080/`.

## Acceso a la Consola H2

Puedes acceder a la consola H2 en `http://localhost:8080/h2-console`. Utiliza las siguientes credenciales:

- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Usuario**: `sa`
- **Contraseña**: *(dejar en blanco)*

## Interacción con la API GraphQL

Para interactuar con la API GraphQL, puedes utilizar herramientas como [GraphiQL](http://localhost:8080/graphiql?path=/graphql) o [Postman](https://www.postman.com/).

**Ejemplos de Consultas y Mutaciones:**

- **Crear un nuevo libro:**

   ```graphql
   mutation {
     createBook(title: "1984", author: "George Orwell") {
       id
       title
       author
     }
   }
   ``` 
- **Obtener todos los libros:**

   ```graphql
    query {
      allBooks {
      id
      title
      author
      }
    }

   ``` 