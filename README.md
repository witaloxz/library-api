# 📚 Library API

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![Spring Security](https://img.shields.io/badge/Security-Spring%20Security-success)
![OAuth2](https://img.shields.io/badge/Auth-OAuth2-blue)
![JWT](https://img.shields.io/badge/Auth-JWT-yellow)
![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-blue)
![Swagger](https://img.shields.io/badge/API-Swagger%20OpenAPI-green)
![Maven](https://img.shields.io/badge/Build-Maven-red)

A complete **RESTful API** for library management with **OAuth2 authentication**, developed using **Spring Boot**.

## 🚀 Technologies Used

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Security**
- **Spring Security OAuth2 Authorization Server**
- **Spring Data JPA**
- **PostgreSQL**
- **JWT**
- **Swagger / OpenAPI 3**
- **Maven**
- **Lombok**

## 📋 Features

### 🔐 Authentication System
- **OAuth2 Authorization Server** with multiple grant types
- **JWT tokens** with custom claims
- **Social Login** via OAuth2 (Google, GitHub, etc.)
- **RBAC (Role-Based Access Control)** with USER and ADMIN roles
- **BCrypt** password hashing

### 📖 Content Management
- **Authors** – Full CRUD with validations
- **Books** – Management with ISBN and genres
- **OAuth2 Clients** – Client application registration
- **Users** – User registration and profile management

## 🏗️ Project Structure

```
src/main/java/com/witalo/libraryapi/
├── config/
├── controllers/
│   └── exceptions/
├── dto/
├── model/
│   └── enums/
├── repository/
├── security/
└── services/
    └── exceptions/
```

## 🔐 API Endpoints

### 📚 Authors (`/authors`)
| Method | Endpoint | Permission | Description |
|------|---------|------------|------------|
| GET | `/authors` | USER, ADMIN | Paginated authors list |
| GET | `/authors/{id}` | USER, ADMIN | Find author by ID |
| POST | `/authors` | ADMIN | Create author |
| PUT | `/authors/{id}` | ADMIN | Update author |
| DELETE | `/authors/{id}` | ADMIN | Delete author |

### 📖 Books (`/books`)
| Method | Endpoint | Permission | Description |
|------|---------|------------|------------|
| GET | `/books` | USER, ADMIN | Paginated books list |
| GET | `/books/{id}` | USER, ADMIN | Find book by ID |
| POST | `/books` | ADMIN | Create book |
| PUT | `/books/{id}` | ADMIN | Update book |
| DELETE | `/books/{id}` | ADMIN | Delete book |

### 👥 Users (`/users`)
| Method | Endpoint | Permission | Description |
|------|---------|------------|------------|
| GET | `/users` | ADMIN | List users |
| GET | `/users/{id}` | ADMIN or owner | Find user |
| POST | `/users` | PUBLIC | Register user |
| PUT | `/users/{id}` | ADMIN or owner | Update user |
| DELETE | `/users/{id}` | ADMIN | Delete user |

### 🔧 OAuth2 Clients (`/clients`)
| Method | Endpoint | Permission | Description |
|------|---------|------------|------------|
| GET | `/clients` | USER, ADMIN | List OAuth2 clients |
| GET | `/clients/{id}` | USER, ADMIN | Find client |
| POST | `/clients` | ADMIN | Create client |
| PUT | `/clients/{id}` | ADMIN | Update client |
| DELETE | `/clients/{id}` | ADMIN | Delete client |

## 🔑 OAuth2 Authorization Server

**Endpoints**
- `/oauth2/token`
- `/oauth2/introspect`
- `/oauth2/revoke`
- `/oauth2/authorize`
- `/oauth2/userinfo`
- `/oauth2/jwks`
- `/oauth2/logout`

**Supported Grant Types**
- Authorization Code
- Client Credentials
- Refresh Token

## ▶️ How to Run

### Prerequisites
- Java 17+
- Maven 3.6+
- PostgreSQL

### Steps

1. Clone repository:
```bash
git clone <REPOSITORY_URL>
cd library-api
```

2. Create database:
```sql
CREATE DATABASE librarydb;
```

3. Environment variables:
```bash
export DB_URL=jdbc:postgresql://localhost:5432/librarydb
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
```

4. Run application:
```bash
mvn spring-boot:run
```

5. API Documentation:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI Docs: http://localhost:8080/v3/api-docs

## 🐛 Error Handling

Standardized error response example:
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 404,
  "error": "Resource not found",
  "message": "Author not found with id",
  "path": "/authors/{id}"
}
```

## 👨‍💻 Developer

Developed by **Witalo Dias**  
📧 [Email](mailto:witalodias1@gmail.com)  
💼 [LinkedIn](https://www.linkedin.com/in/witalo-dias-775a59289/)

## 📄 License

This project is licensed under the **MIT License**.

⭐ If you found this project useful, consider giving it a star!
