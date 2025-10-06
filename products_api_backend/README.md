# Products API Backend

A RESTful API for managing products built with Spring Boot, PostgreSQL, and Flyway for database migrations.

## Features

- **CRUD Operations**: Create, Read, Update (full and partial), and Delete products
- **Validation**: Input validation with meaningful error messages
- **Pagination & Sorting**: Efficient data retrieval with customizable pagination and sorting
- **API Documentation**: Interactive Swagger UI for exploring and testing endpoints
- **Database Migrations**: Flyway-managed PostgreSQL schema migrations
- **Exception Handling**: Global exception handling with consistent error responses
- **Testing**: Unit tests for service layer and controller slice tests

## Tech Stack

- Java 17
- Spring Boot 3.4.8
- Spring Data JPA
- PostgreSQL
- Flyway
- SpringDoc OpenAPI (Swagger)
- Gradle

## Prerequisites

- Java 17 or higher
- PostgreSQL database (products_db)
- Gradle (wrapper included)

## Environment Variables

The application uses the following environment variables for database configuration:

| Variable | Description | Default Value |
|----------|-------------|---------------|
| `DB_URL` | PostgreSQL connection URL | `jdbc:postgresql://products_db:5432/products` |
| `DB_USERNAME` | Database username | `postgres` |
| `DB_PASSWORD` | Database password | `postgres` |

You can set these in a `.env` file or pass them directly when running the application.

## Running the Application

### Using Gradle Wrapper

```bash
# Build the application
./gradlew build

# Run the application
./gradlew bootRun

# Run with custom environment variables
DB_URL=jdbc:postgresql://localhost:5432/mydb DB_USERNAME=myuser DB_PASSWORD=mypass ./gradlew bootRun
```

### Using Java

```bash
# Build the JAR
./gradlew build

# Run the JAR
java -jar build/libs/productsapibackend-0.1.0.jar
```

The application will start on **port 3001**.

## API Endpoints

### Base URL
```
http://localhost:3001/api/products
```

### Swagger UI
Access the interactive API documentation at:
```
http://localhost:3001/swagger-ui.html
```

### OpenAPI Specification
```
http://localhost:3001/api-docs
```

## API Operations

### 1. Create Product

**POST** `/api/products`

Creates a new product.

**Request Body:**
```json
{
  "name": "Laptop",
  "price": 999.99,
  "quantity": 10
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "Laptop",
  "price": 999.99,
  "quantity": 10,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:3001/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "price": 999.99,
    "quantity": 10
  }'
```

### 2. Get All Products (with Pagination)

**GET** `/api/products?page=0&size=20&sort=name,asc`

Retrieves a paginated list of products.

**Query Parameters:**
- `page` (optional): Page number, default 0
- `size` (optional): Page size, default 20
- `sort` (optional): Sort criteria (e.g., `name,asc` or `price,desc`), default `id,asc`

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Laptop",
      "price": 999.99,
      "quantity": 10,
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalElements": 1,
  "totalPages": 1
}
```

**cURL Examples:**
```bash
# Get first page (default)
curl http://localhost:3001/api/products

# Get page 2 with 10 items, sorted by price descending
curl "http://localhost:3001/api/products?page=1&size=10&sort=price,desc"

# Sort by name ascending
curl "http://localhost:3001/api/products?sort=name,asc"
```

### 3. Get Product by ID

**GET** `/api/products/{id}`

Retrieves a single product by ID.

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Laptop",
  "price": 999.99,
  "quantity": 10,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

**cURL Example:**
```bash
curl http://localhost:3001/api/products/1
```

### 4. Update Product (Full Update)

**PUT** `/api/products/{id}`

Fully updates a product. All fields are required.

**Request Body:**
```json
{
  "name": "Gaming Laptop",
  "price": 1299.99,
  "quantity": 5
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Gaming Laptop",
  "price": 1299.99,
  "quantity": 5,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T11:45:00"
}
```

**cURL Example:**
```bash
curl -X PUT http://localhost:3001/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Gaming Laptop",
    "price": 1299.99,
    "quantity": 5
  }'
```

### 5. Partial Update (PATCH)

**PATCH** `/api/products/{id}`

Partially updates a product. Only provided fields are updated.

**Request Body (update only price):**
```json
{
  "price": 899.99
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Laptop",
  "price": 899.99,
  "quantity": 10,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T12:00:00"
}
```

**cURL Examples:**
```bash
# Update only price
curl -X PATCH http://localhost:3001/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{"price": 899.99}'

# Update only quantity
curl -X PATCH http://localhost:3001/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{"quantity": 15}'

# Update multiple fields
curl -X PATCH http://localhost:3001/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Laptop",
    "quantity": 20
  }'
```

### 6. Delete Product

**DELETE** `/api/products/{id}`

Deletes a product by ID.

**Response (204 No Content)**

**cURL Example:**
```bash
curl -X DELETE http://localhost:3001/api/products/1
```

## Validation Rules

### Product Fields

- **name**: 
  - Required
  - Maximum length: 255 characters
  
- **price**: 
  - Required
  - Must be non-negative (>= 0)
  - Precision: 19 digits, scale: 2
  
- **quantity**: 
  - Required
  - Must be non-negative (>= 0)

### Error Response Format

When validation fails or errors occur, the API returns a consistent error response:

```json
{
  "status": 400,
  "message": "Validation failed",
  "timestamp": "2024-01-15T10:30:00",
  "details": {
    "name": "Product name is required",
    "price": "Price must be non-negative"
  }
}
```

## Running Tests

```bash
# Run all tests
./gradlew test

# Run tests with coverage
./gradlew test jacocoTestReport

# Run specific test class
./gradlew test --tests ProductServiceTest
```

## Database Schema

The `products` table is created via Flyway migration with the following structure:

```sql
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(19, 2) NOT NULL CHECK (price >= 0),
    quantity INTEGER NOT NULL CHECK (quantity >= 0),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

## Project Structure

```
src/
├── main/
│   ├── java/com/example/productsapibackend/
│   │   ├── product/
│   │   │   ├── controller/       # REST controllers
│   │   │   ├── dto/              # Data Transfer Objects
│   │   │   ├── entity/           # JPA entities
│   │   │   ├── exception/        # Custom exceptions and handlers
│   │   │   ├── mapper/           # Entity-DTO mappers
│   │   │   ├── repository/       # Spring Data repositories
│   │   │   └── service/          # Business logic
│   │   └── productsapibackendApplication.java
│   └── resources/
│       ├── application.yml       # Configuration
│       └── db/migration/         # Flyway migrations
└── test/
    └── java/com/example/productsapibackend/
        └── product/
            ├── controller/       # Controller tests
            └── service/          # Service tests
```

## Additional Endpoints

### Health Check
```
GET http://localhost:3001/health
```

### Application Info
```
GET http://localhost:3001/api/info
```

### Actuator Endpoints
- Health: `http://localhost:3001/actuator/health`
- Info: `http://localhost:3001/actuator/info`
- Metrics: `http://localhost:3001/actuator/metrics`

## License

This project is part of a product management system.
