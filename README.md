# Book Library Microservice

A Java-based REST API microservice for managing a book library using JAX-RS (Jersey), CDI (Weld), JPA (Hibernate), and PostgreSQL, deployed on Apache Tomcat.

## Technology Stack

- **Java 11** - Programming language
- **Maven 3.8+** - Build and dependency management
- **Apache Tomcat 9** - Web server and servlet container
- **Jersey 2.35** - JAX-RS implementation for REST APIs
- **Weld** - CDI (Contexts and Dependency Injection) implementation
- **Hibernate** - JPA implementation for database operations
- **PostgreSQL 15** - Database
- **Docker** - Containerization

---

## Prerequisites

- Java 11 or higher
- Maven 3.8+
- Docker and Docker Compose (for containerized deployment)
- PostgreSQL 15+ (for local development)

---

## Setup Guide

### Option 1: Docker Deployment (Recommended)

**Step 1:** Clone the repository
```bash
git clone <repository-url>
cd book-library-microservice
```

**Step 2:** Build and run with Docker
```bash
docker-compose up --build
```

This will:
- Start a PostgreSQL database container
- Build and deploy the application on Tomcat
- Initialize the database with sample data
- Expose the API on `http://localhost:8080`

### Option 2: Local Development Setup

**Step 1:** Start PostgreSQL database
```bash
# Using Docker
docker run --name postgres-booklib -e POSTGRES_PASSWORD=password -e POSTGRES_DB=booklib -p 5432:5432 -d postgres:15

# Or install PostgreSQL locally and create database
psql -U postgres -c "CREATE DATABASE booklib;"
```

**Step 2:** Initialize the database
```bash
psql -U postgres -d booklib -f docker/init.sql
```

**Step 3:** Build the application
```bash
mvn clean package
```

**Step 4:** Deploy to Tomcat
- Copy `target/book-library-microservice.war` to your Tomcat's `webapps/` directory
- Or use Docker to run just the application:
```bash
docker build -t book-library-microservice -f docker/Dockerfile .
docker run -p 8080:8080 --link postgres-booklib:postgres book-library-microservice
```

---

## API Documentation

### Base URL
`http://localhost:8080/books`

### Endpoints

| Method | Endpoint               | Description                | Example Request Body                          |
|--------|------------------------|----------------------------|-----------------------------------------------|
| POST   | `/books`               | Create a new book          | `{ "isbn": "978-123-456-7890", "title": "...", "author": "...", "publicationYear": 2023 }` |
| GET    | `/books`               | List all books             |                                               |
| GET    | `/books/{isbn}`        | Get book by ISBN           |                                               |
| PUT    | `/books`               | Update an existing book    | `{ "isbn": "existing-isbn", "title": "New Title" }` |
| DELETE | `/books/{isbn}`        | Delete a book by ISBN      |                                               |

---

## Testing with curl

**Create Book:**
```bash
curl -X POST -H "Content-Type: application/json" -d '{"isbn":"978-0-596-52068-7","title":"Clean Code","author":"Robert C. Martin","publicationYear":2008}' http://localhost:8080/books
```

**List Books:**
```bash
curl -X GET http://localhost:8080/books
```

**Update Book:**
```bash
curl -X PUT -H "Content-Type: application/json" -d '{"isbn":"978-0-596-52068-7","title":"Clean Code (Revised Edition)"}' http://localhost:8080/books
```

**Delete Book:**
```bash
curl -X DELETE http://localhost:8080/books/978-0-596-52068-7
```

---

## Configuration

### Database Configuration
Database connection settings are configured in:
- `src/main/resources/META-INF/persistence.xml` - JPA/Hibernate configuration
- Environment variables in Docker Compose:
  - `DB_HOST=postgres` (container name)
  - `DB_PORT=5432`
  - `DB_NAME=booklib`
  - `DB_USER=postgres`
  - `DB_PASSWORD=password`

### Application Architecture
- **REST Layer**: `BookResource` class with JAX-RS annotations
- **Service Layer**: `BookService` with CDI `@Inject` for dependency injection
- **Data Layer**: JPA entities with Hibernate as the implementation
- **Web Configuration**: Jersey servlet configured in `web.xml`

---

## Development

### Project Structure
```
src/main/java/com/library/book/
├── model/           # JPA entities
│   └── Book.java
├── rest/            # JAX-RS REST endpoints
│   ├── BookResource.java
│   └── JAXRSConfiguration.java
├── service/         # Business logic layer
│   └── BookService.java
└── util/            # Utility classes
    └── ValidationUtil.java
```

### Running Tests
```bash
mvn test
```

### Building for Production
```bash
mvn clean package
```

This creates a WAR file in `target/book-library-microservice.war` ready for deployment.

---

## Troubleshooting

### Common Issues

1. **Database Connection Issues**
   - Ensure PostgreSQL is running and accessible
   - Verify database credentials in environment variables
   - Check if database `booklib` exists

2. **Port Already in Use**
   - Change the port mapping in `docker-compose.yml` or stop conflicting services

3. **JAX-RS Endpoints Not Found**
   - Ensure Jersey servlet is properly configured in `web.xml`
   - Verify `JAXRSConfiguration` class registers your resource classes

### Logs
```bash
# View application logs
docker-compose logs app

# View database logs  
docker-compose logs postgres
```

---

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes
4. Run tests (`mvn test`)
5. Build and test with Docker (`docker-compose up --build`)
6. Commit your changes (`git commit -m 'Add amazing feature'`)
7. Push to the branch (`git push origin feature/amazing-feature`)
8. Open a Pull Request

---

## License

This project is licensed under the MIT License - see the LICENSE file for details.

---

**Note**: This microservice uses Apache Tomcat with Jersey (JAX-RS), Weld (CDI), and Hibernate (JPA) to provide a lightweight, standards-based REST API for book management.
