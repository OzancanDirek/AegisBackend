# Aegis Backend

> **Post-Disaster Neighborhood Solidarity and Resource Management System** — Java Spring Boot + MySQL REST API

Frontend repository: [AegisFrontend](https://github.com/OzancanDirek/AegisFrontend)

---

## Tech Stack

| | |
|---|---|
| Language | Java 17+ |
| Framework | Spring Boot |
| Database | MySQL 8 |
| ORM | Spring Data JPA / Hibernate |
| Build | Maven |
| Architecture | Layered (Controller → Service → Repository) |

---

## Getting Started

### Prerequisites
- Java 17+
- Maven
- MySQL 8+

### Steps

```bash
git clone https://github.com/OzancanDirek/AegisBackend.git
cd AegisBackend
```

Create `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mainAegis_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

Create the database:

```sql
CREATE DATABASE IF NOT EXISTS mainAegis_db;
```

Run the application:

```bash
mvn spring-boot:run
```

API will be available at `http://localhost:8080`.

---

## Project Structure

```
src/main/java/org/example/
├── Controller/
│   ├── AdminController.java
│   ├── AdressController.java
│   ├── LoginController.java
│   ├── ResidentController.java
│   ├── SkillController.java
│   ├── SpecialNeedsController.java
│   ├── TestController.java
│   ├── UserRoleController.java
│   └── VolunteerController.java
│
├── Service/
│   ├── IAdminService.java
│   ├── IAdressService.java
│   ├── IResidentService.java
│   ├── ISkillService.java
│   ├── ISpecialNeedService.java
│   ├── IUserRoleService.java
│   ├── IVolunteerService.java
│   └── Impl/
│       ├── AdminServiceImpl.java
│       ├── AdressServiceImpl.java
│       ├── ResidentServiceImpl.java
│       ├── SkillServiceImpl.java
│       ├── SpecialNeedServiceImpl.java
│       ├── UserRoleServiceImpl.java
│       ├── UserServiceImpl.java
│       └── VolunteerServiceImpl.java
│
├── Repository/
│   ├── AddressRepository.java
│   ├── ResidentRepository.java
│   ├── RoleRepository.java
│   ├── SkillRepository.java
│   ├── SpecialNeedRepository.java
│   ├── UserRepository.java
│   └── VolunteerRepository.java
│
├── Model/
│   ├── Adresses.java
│   ├── HouseHold.java
│   ├── Resident.java
│   ├── Roles.java
│   ├── Skills.java
│   ├── SpecialNeeds.java
│   ├── UserRoles.java
│   ├── Users.java
│   └── Volunteers.java
│
├── Dtos/
│   ├── AdressDto/
│   │   ├── CreateAdressDto.java
│   │   ├── ResultAdressDto.java
│   │   └── UpdateAdressDto.java
│   ├── ResidentDto/
│   │   └── ResidentResponseDto.java
│   ├── SkillDtos/
│   ├── SpecialNeedsDto/
│   │   ├── CreateSpecialNeeds.java
│   │   └── SpecialNeedsResponseDto.java
│   ├── VolunteerDtos/
│   ├── LoginDto.java
│   └── RegisterDto.java
│
└── Main.java
```

---

## API Endpoints

### Address
```
GET    /api/addresses/allAdresses       List all addresses
POST   /api/addresses                   Create new address
PUT    /api/addresses/{id}              Update address
DELETE /api/addresses/{id}              Delete address
```

### User & Role
```
GET    /api/UserRole/users              List all users
GET    /api/UserRole/all                List all roles
GET    /api/UserRole/user/{userId}      Get roles of a user
POST   /api/UserRole/assign             Assign role to user
```

### Resident
```
GET    /api/residents/all                       List all residents
POST   /api/residents/{id}/special-needs        Assign special needs to resident
```

### Special Needs
```
GET    /api/special-needs/all           List all special need definitions
POST   /api/special-needs               Create new special need
```

### Volunteer
```
GET    /api/volunteers/all              List all volunteers
POST   /api/volunteers                  Create volunteer
```

### Skill
```
GET    /api/skills/all                  List all skills
POST   /api/skills                      Create new skill
```

---

## Database Schema

20 tables — key relationships:

```
users ──< user_roles >── roles                           many-to-many
households ──< residents                                 one-to-many
residents ──< resident_special_needs >── special_needs   many-to-many
volunteers ──< volunteer_skills >── skills               many-to-many
households ──< aid_requests                              one-to-many
aid_requests ──< aid_assignments                         one-to-many
warehouses ──< inventory_items                           one-to-many
addresses ──── buildings                                 one-to-one
```

---

## User Roles

| Role | Description |
|---|---|
| `Admin` | System administrator with full access |
| `Gonullu` | Field volunteer |
| `Calisan` | Staff member |
| `Depremzede` | Aid recipient / disaster victim |

---

## Technical Notes

- User IDs use `CHAR(36)` UUID format
- Lombok annotations used throughout: `@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`
- DTO layer prevents JSON serialization issues caused by lazy-loaded JPA relationships
- `@Transactional` applied where lazy field access is required
- CORS configured for `http://localhost:5173` on all controllers

---

*Aegis Disaster Management System · v1.0.0 · 2026*
