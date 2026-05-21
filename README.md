# Aegis Backend

Spring Boot-based disaster management system backend. Developed for post-earthquake relief coordination.

## Tech Stack

- Java 21
- Spring Boot 3.4.1
- Spring Security + JWT
- MySQL 9.4
- Hibernate / JPA
- Lombok
- Maven

## Prerequisites

- Java 21+
- MySQL 8+
- Maven 3.8+

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/OzancanDirek/AegisBackend.git
cd AegisBackend
```

### 2. Create the database

Run the following command in MySQL:

```sql
CREATE DATABASE mainAegis_db;
```

### 3. Configure `application.properties`

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mainAegis_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:your_password}

spring.jpa.hibernate.ddl-auto=update

jwt.secret=${JWT_SECRET:aegis-super-secret-key-must-be-32chars!}
```

### 4. Run the application

```bash
mvn spring-boot:run
```

The application will start at `http://localhost:8080`.

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DB_USERNAME` | MySQL username | `root` |
| `DB_PASSWORD` | MySQL password | — |
| `JWT_SECRET` | JWT signing key (min. 32 characters) | — |

## API Endpoints

### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/login` | Login |
| POST | `/api/auth/register` | Register |

### Users
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/api/users/profile` | Get profile | Authenticated |
| PUT | `/api/users/profile` | Update profile | Authenticated |

### Aid Requests
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/api/aid-requests` | Get all requests | Authenticated |
| POST | `/api/aid-requests` | Create request | Authenticated |
| PUT | `/api/aid-requests` | Update request | Authenticated |
| DELETE | `/api/aid-requests/{id}` | Delete request | Authenticated |

### Assignments
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/api/assignments` | Get all assignments | Admin, Calisan |
| POST | `/api/assignments` | Create assignment | Admin, Calisan |
| PUT | `/api/assignments` | Update assignment | Admin, Calisan |
| DELETE | `/api/assignments/{id}` | Delete assignment | Admin, Calisan |

### Warehouses
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/api/warehouse/getAllWarehouse` | Get all warehouses | Authenticated |
| POST | `/api/warehouse` | Create warehouse | Authenticated |
| PUT | `/api/warehouse/{id}` | Update warehouse | Authenticated |
| DELETE | `/api/warehouse/{id}` | Delete warehouse | Authenticated |

### Inventory
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/api/inventory/warehouse/{id}` | Get warehouse inventory | Authenticated |
| GET | `/api/inventory/critical` | Get critical items | Authenticated |
| POST | `/api/inventory` | Add item | Authenticated |
| PUT | `/api/inventory/{id}` | Update item | Authenticated |
| DELETE | `/api/inventory/{id}` | Delete item | Authenticated |

### Audit Log
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/api/audit` | Get all logs | Admin |
| GET | `/api/audit/user/{email}` | Get logs by user | Admin |
| GET | `/api/audit/action/{action}` | Get logs by action | Admin |

## Roles

| Role | Description |
|------|-------------|
| `Admin` | Full access |
| `Calisan` | Operational access |
| `Gonullu` | Volunteer — view assignments |
| `Depremzede` | Earthquake victim — submit aid requests |
| `WAREHOUSE_MANAGER` | Warehouse management |
| `User` | Basic access |

## Security

- JWT-based authentication (Access Token: 1 hour, Refresh Token: 7 days)
- Role-based access control (RBAC)
- BCrypt password hashing
- Audit logging — all critical operations are recorded

## Project Structure

src/main/java/org/example/
├── Controller/       # REST controllers
├── Service/          # Business logic
│   └── Impl/         # Service implementations
├── Repository/       # JPA repositories
├── Model/            # Entity classes
├── Dtos/             # Data Transfer Objects
├── Security/         # JWT and Spring Security configuration
└── Scheduled/        # Scheduled jobs
