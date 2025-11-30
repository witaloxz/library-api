# 📚 Library API

Uma API RESTful completa para gerenciamento de biblioteca com autenticação OAuth2, desenvolvida em Spring Boot.

## 🚀 Tecnologias Utilizadas

- **Java 17+** - Linguagem de programação
- **Spring Boot 3.x** - Framework principal
- **Spring Security** - Autenticação e autorização
- **Spring Security OAuth2** - Servidor de autorização
- **Spring Data JPA** - Persistência de dados
- **PostgreSQL** - Banco de dados
- **JWT** - Tokens de acesso
- **Swagger/OpenAPI 3** - Documentação da API
- **Maven** - Gerenciamento de dependências
- **Lombok** - Redução de código boilerplate

## 📋 Funcionalidades

### 🔐 Sistema de Autenticação
- **OAuth2 Authorization Server** com suporte a múltiplos grant types
- **JWT Tokens** com claims personalizadas
- **Login Social** via OAuth2 (Google, GitHub, etc.)
- **RBAC (Role-Based Access Control)** com roles USER e ADMIN
- **BCrypt** para hash de senhas

### 📖 Gerenciamento de Conteúdo
- **Autores** - CRUD completo com validações
- **Livros** - Gerenciamento com ISBN e gêneros
- **Clientes OAuth2** - Registro de aplicações clientes
- **Usuários** - Sistema de cadastro e perfis

## 🏗️ Estrutura do Projeto

```
src/main/java/com/witalo/libraryapi/
├── config/
│   ├── AuthorizationServerConfiguration.java
│   ├── OpenApiConfiguration.java
│   ├── SecurityConfiguration.java
│   └── WebConfiguration.java
│
├── controllers/
│   ├── AuthorController.java
│   ├── BookController.java
│   ├── ClientController.java
│   ├── UserController.java
│   ├── LoginViewController.java
│   └── exceptions/
│       ├── ControllerExceptionHandler.java
│       ├── FieldMessage.java
│       ├── StandardError.java
│       └── ValidationError.java
│
├── dto/
│   ├── AuthorDTO.java
│   ├── BookDTO.java
│   ├── ClientDTO.java
│   └── UserDTO.java
│
├── model/
│   ├── Author.java
│   ├── Book.java
│   ├── Client.java
│   ├── User.java
│   └── enums/
│       └── BookGenres.java
│
├── repository/
│   ├── AuthorRepository.java
│   ├── BookRepository.java
│   ├── ClientRepository.java
│   └── UserRepository.java
│
├── security/
│   ├── CustomAuthentication.java
│   ├── CustomAuthenticationProvider.java
│   ├── CustomRegisteredClientRepository.java
│   ├── JwtCustomAuthenticationFilter.java
│   ├── LoginSocialSuccessHandler.java
│   └── SecurityService.java
│
└── services/
    ├── AuthorService.java
    ├── BookService.java
    ├── ClientService.java
    ├── UserService.java
    └── exceptions/
        ├── DataBaseException.java
        ├── InvalidBookGenreException.java
        ├── ResourceAlreadyExistsException.java
        └── ResourceNotFoundException.java
```

## 🔐 Endpoints da API

### 📚 Autores (`/authors`)
| Método | Endpoint | Permissão | Descrição |
|--------|----------|-----------|-----------|
| GET | `/authors` | USER, ADMIN | Lista paginada de autores |
| GET | `/authors/{id}` | USER, ADMIN | Busca autor por ID |
| POST | `/authors` | ADMIN | Cria novo autor |
| PUT | `/authors/{id}` | ADMIN | Atualiza autor |
| DELETE | `/authors/{id}` | ADMIN | Remove autor |

### 📖 Livros (`/books`)
| Método | Endpoint | Permissão | Descrição |
|--------|----------|-----------|-----------|
| GET | `/books` | USER, ADMIN | Lista paginada de livros |
| GET | `/books/{id}` | USER, ADMIN | Busca livro por ID |
| POST | `/books` | ADMIN | Cria novo livro |
| PUT | `/books/{id}` | ADMIN | Atualiza livro |
| DELETE | `/books/{id}` | ADMIN | Remove livro |
### 👥 Usuários (`/users`)
| Método | Endpoint | Permissão | Descrição |
|--------|----------|-----------|-----------|
| GET | `/users` | ADMIN | Lista todos usuários |
| GET | `/users/{id}` | ADMIN ou próprio usuário | Busca usuário por ID |
| POST | `/users` | PÚBLICO | Registra novo usuário |
| PUT | `/users/{id}` | ADMIN ou próprio usuário | Atualiza usuário |
| DELETE | `/users/{id}` | ADMIN | Remove usuário |

### 🔧 Clientes OAuth2 (`/clients`)
| Método | Endpoint | Permissão | Descrição |
|--------|----------|-----------|-----------|
| GET | `/clients` | USER, ADMIN | Lista clientes OAuth2 |
| GET | `/clients/{id}` | USER, ADMIN | Busca cliente por ID |
| POST | `/clients` | ADMIN | Registra novo cliente |
| PUT | `/clients/{id}` | ADMIN | Atualiza cliente |
| DELETE | `/clients/{id}` | ADMIN | Remove cliente |

## 🔑 Autenticação OAuth2

### Endpoints do Authorization Server
- `POST /oauth2/token` - Obter token de acesso
- `POST /oauth2/introspect` - Validar token
- `POST /oauth2/revoke` - Revogar token
- `GET /oauth2/authorize` - Fluxo de autorização
- `GET /oauth2/userinfo` - Informações do usuário
- `GET /oauth2/jwks` - Chaves públicas JWT
- `GET /oauth2/logout` - Logout

### Tipos de Grant Suportados
- **Authorization Code**
- **Client Credentials** 
- **Refresh Token**

## ⚙️ Configuração

### application.properties
```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/librarydb
spring.datasource.username=username
spring.datasource.password=password

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# OAuth2
spring.security.oauth2.authorizationserver.issuer=http://localhost:8080

# Server
server.port=8080
``` 

# 🚀 Como Executar
# Pré-requisitos
- **Java 17+**
- **Maven 3.6+**
- **PostgreSQL**

## Passos

1. 📥 Clone este repositório:
   ```bash
   git clone <URL_DO_REPOSITORIO>
   cd library-api
   ```
2. 🗃️ Configuração do Banco do Dados
   ```sql
   CREATE DATABASE librarydb;
   ```
3. ⚙️ Várieveis de Ambiente
   ```bash
   export DB_URL=jdbc:postgresql://localhost:5432/librarydb
   export DB_USERNAME=seu_usuario
   export DB_PASSWORD=sua_senha
   ```
4. 🚀 Executar a Aplicação
   ```bash
   mvn spring-boot:run
   ```
5. 📚 Acessar Documentação
   ```bash
   Swagger UI: http://localhost:8080/swagger-ui.html
   API Docs: http://localhost:8080/v3/api-docs
   🔗 Abrir automaticamente (Linux/Mac)
   xdg-open http://localhost:8080/swagger-ui.html
   open http://localhost:8080/swagger-ui.html
   ```
## 📊 Modelo de Dados

### Entidades Principais

#### User
- `UUID id`
- `String login`
- `String email`
- `String password`
- `List<String> roles`

#### Author  
- `UUID id`
- `String name`
- `LocalDate date`
- `String nationality`
- `User user`
- `List<Book> books`

#### Book
- `UUID id`
- `String isbn`
- `String title`
- `LocalDate datePublication`
- `BookGenres bookGenres`
- `BigDecimal price`
- `Author author`

#### Client (OAuth2)
- `UUID id`
- `String clientId`
- `String clientSecret`
- `String redirectURI`
- `String scope`

## 🛡️ Regras de Negócio

### Autores
- ✅ Nome, data de nascimento e nacionalidade obrigatórios
- ✅ Data de nascimento não pode ser futura
- ✅ Não pode haver autores duplicados (nome + data)
- ✅ Usuário só pode modificar autores que criou
- ❌ Não pode excluir autor com livros associados

### Livros
- ✅ ISBN, título e data de publicação obrigatórios
- ✅ ISBN deve ser válido e único
- ✅ Data de publicação não pode ser futura
- ✅ Preço obrigatório para livros a partir de 2020
- ✅ Gênero deve ser válido (enum BookGenres)

### Usuários
- ✅ Login único (3-50 caracteres)
- ✅ Email válido e único
- ✅ Senha mínima de 6 caracteres
- ✅ Usuário comum só acessa/próprios dados
- ✅ Admin acessa/modifica todos os usuários

## 🔒 Roles e Permissões

### ROLE_USER
- ✅ Listar e visualizar autores e livros
- ✅ Acessar próprios dados de usuário
- ✅ Atualizar próprio perfil

### ROLE_ADMIN  
- ✅ Todas permissões de USER
- ✅ CRUD completo de autores, livros e clientes
- ✅ Gerenciar todos os usuários
- ✅ Acessar todos os endpoints administrativos

## 📝 Exemplos de Uso

### 1. Registrar Usuário
```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{
    "login": "joao.silva",
    "email": "joao@email.com",
    "password": "123456",
    "roles": ["USER"]
  }'
``` 
### 2. Obter Token OAuth2
```bash
curl -X POST http://localhost:8080/oauth2/token \
  -H "Authorization: Basic base64(clientId:clientSecret)" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials&scope=read write"
```
### 3. Listar Livros
```bash
curl -X GET http://localhost:8080/books \
  -H "Authorization: Bearer <access_token>"
```
## 🐛 Tratamento de Erros
### A API retorna respostas padronizadas para erros:
```bash
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 404,
  "error": "Resource not found",
  "message": "Author not found with id: 123e4567-e89b-12d3-a456-426614174000",
  "path": "/authors/123e4567-e89b-12d3-a456-426614174000"
}
```
### Códigos de Status Comuns

- `200 - Sucesso`
- `201 - Recurso criado`
- `400 - Dados inválidos`
- `401 - Não autenticado`
- `403 - Acesso negado`
- `404 - Recurso não encontrado`
- `409 - Conflito (recurso já existe)`
- `422 - Erro de validação`
  
## 👨‍💻 Desenvolvedor - Witalo Dias
### Contatos
📧 [Email](mailto:witalodias1@gmail.com)<br>
💼 [LinkedIn](https://www.linkedin.com/in/witalo-dias-775a59289/)

## 📄 Licença
Este projeto está sob a licença MIT.
### ⭐ Se este projeto foi útil, deixe uma estrela no repositório!











