# Network Inventory Backend (Spring Boot + MySQL)

## Tech Stack
- Java + Spring Boot
- Spring Data JPA (Hibernate)
- MySQL
- Swagger OpenAPI
- Audit Logging (actor + requestId)

## How to Run (Local)
1. Create MySQL DB: `network_inventory_db`
2. Create `src/main/resources/application-local.properties` with your DB credentials
3. Run with profile:
    - IntelliJ: set active profile = `local`
    - or: `mvn spring-boot:run -Dspring-boot.run.profiles=local`

## Swagger
- http://localhost:8989/swagger-ui/index.html
- http://localhost:8989/v3/api-docs

## Key Features
- Manage Headend → FDH → Splitters → Customers
- Splitter port assignment with validation
- Deployment Task workflow (SCHEDULED → IN_PROGRESS → COMPLETED)
- Technician management
- Asset creation + assignment to customer
- Audit logs for CREATE/ASSIGN/STATUS_CHANGE actions

## Audit Logs
Endpoint:
- `GET /api/audit/recent`

Header used:
- `X-Actor: Admin`
