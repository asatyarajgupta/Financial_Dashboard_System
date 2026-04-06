# 💰 Financial Dashboard System

A Spring Boot REST API for managing personal financial records — track income, expenses, and get insights like net balance, monthly trends, and category breakdowns. Secured with JWT authentication and role-based access control.

---

## 🚀 Tech Stack

- **Java 17** + **Spring Boot 3**
- **Spring Security** with JWT
- **Spring Data JPA** + **MySQL**
- **Lombok**
- **Maven**
- **Swagger / OpenAPI 3** (via SpringDoc)

---

## ⚙️ Setup & Installation

### 1. Prerequisites
- Java 17+
- MySQL running locally
- Maven (or use `.\mvnw`)

### 2. Clone the repository
```bash
git clone https://github.com/yourusername/financial-dashboard-system.git
cd financial-dashboard-system
```

### 3. Create the MySQL database
```sql
CREATE DATABASE financial_dashboard;
```

### 4. Configure environment variables
Create a `.env` file in the project root (copy from `.env.example`):
```env
DB_URL=jdbc:mysql://localhost:3306/financial_dashboard
DB_USERNAME=root
DB_PASSWORD=yourpassword
JWT_SECRET=yourjwtsecretkey
```

### 5. Run the application
```bash
.\mvnw spring-boot:run
```

The API will be available at: `http://localhost:8080`

Swagger UI will be available at: `http://localhost:8080/swagger-ui/index.html`

---

## 🔐 Authentication

This API uses **JWT Bearer tokens**.

1. Register via `POST /api/public/create`
2. Login via `POST /api/public/login` → returns a JWT token
3. Add the token to all subsequent requests:
```
Authorization: Bearer <your_token>
```

---

## 👥 Roles

| Role | Permissions |
|------|-------------|
| `VIEWER` | View dashboard stats (income, expenses, balance, recent activity) |
| `ANALYST` | VIEWER + view all records, insights, and filter by category |
| `ADMIN` | ANALYST + create, update, delete records and users |

---

## 📡 API Endpoints

### Public (no auth required)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/public/create` | Register a new user |
| POST | `/api/public/login` | Login and get JWT token |
| PUT | `/api/public/update` | Update own profile |
| DELETE | `/api/public/delete` | Delete own account |

---

### Admin — User Management
> Requires `ADMIN` role

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/admin` | Get all users |
| GET | `/api/admin/{id}` | Get user by ID |
| POST | `/api/admin/create` | Create a user with any role |
| PUT | `/api/admin/update/{id}` | Update any user |
| DELETE | `/api/admin/delete` | Delete own account |
| DELETE | `/api/admin/delete/{id}` | Delete any user by ID |

---

### Records — Dashboard
> Requires `VIEWER`, `ANALYST`, or `ADMIN` role

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/records/dashboard/income` | Total income |
| GET | `/api/records/dashboard/expenses` | Total expenses |
| GET | `/api/records/dashboard/balance` | Net balance |
| GET | `/api/records/dashboard/recent` | Last 10 transactions |

---

### Records — Data & Insights
> Requires `ANALYST` or `ADMIN` role

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/records` | All records (paginated) |
| GET | `/api/records/{id}` | Get record by ID |
| GET | `/api/records/category/{category}` | Filter by category |
| GET | `/api/records/insights/category` | Category-wise totals |
| GET | `/api/records/insights/trends` | Monthly trends |

**Pagination params for `GET /api/records`:**
| Param | Default | Description |
|-------|---------|-------------|
| `page` | `0` | Page number |
| `size` | `5` | Records per page |
| `sortBy` | `id` | Field to sort by |
| `ascending` | `true` | Sort direction |

---

### Records — CRUD
> Requires `ADMIN` role

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/records/create` | Create a record |
| PUT | `/api/records/update/{id}` | Update a record |
| DELETE | `/api/records/delete/{id}` | Delete a record |

---

## 📦 Request Body Examples

### Register / Login
```json
{
  "username": "aditya",
  "email": "aditya@example.com",
  "password": "secret123"
}
```

### Create Record
```json
{
  "amount": 5000.00,
  "category": "INCOME",
  "description": "Monthly salary"
}
```

### Create User (Admin)
```json
{
  "username": "analyst1",
  "email": "analyst@example.com",
  "password": "pass123",
  "role": "ANALYST"
}
```

---

## 🧪 Running Tests

```bash
.\mvnw test
```

- **Unit tests** — `userServiceTest`, `recordServiceTest` (39 tests, Mockito)
- **Integration tests** — `userRepoIntegrationTest`, `recordRepoIntegrationTest` (H2 in-memory DB)

---

## 📁 Project Structure

```
src/
├── main/java/com/aditya/financial_dashboard_system/
│   ├── controllers/       # REST controllers
│   ├── entities/          # JPA entities
│   ├── repos/             # Spring Data JPA repositories
│   ├── security/          # JWT filter, Spring Security config
│   ├── services/          # Business logic
│   ├── utils/             # Enums (Role, Category)
│   └── exceptions/        # Custom exceptions & global handler
└── test/java/
    ├── services/          # Unit tests
    └── repos/             # Integration tests
```

---

## 🌱 Environment Variables Reference

Create a `.env.example` file with these keys (no real values):

```env
DB_URL=
DB_USERNAME=
DB_PASSWORD=
JWT_SECRET=
```

---

## 📝 License

MIT License — free to use and modify.
