# Fintech Order Management System (FOMS)

A secure, robust backend system for financial trading, asset management, and wallet transactions built with Spring Boot and PostgreSQL.

## 🚀 Features

- **Secure Authentication:** Stateless JWT-based authentication with Spring Security.
- **Role-Based Access Control:** Distinct `ROLE_USER` and `ROLE_ADMIN` access levels.
- **Wallet Management:** Secure funding and debiting for user accounts.
- **Asset Trading:** APIs to buy, sell, and view current holdings/portfolio value.
- **Audit Logging:** Comprehensive tracking of all administrative actions and wallet transactions.
- **Swagger UI:** Fully documented API endpoints via OpenAPI 3.0.
- **Containerized:** Docker and Docker Compose configurations for seamless deployment.

## 🛠️ Tech Stack

- **Backend:** Java 21, Spring Boot, Spring Security, Spring Data JPA
- **Database:** PostgreSQL
- **Tools:** Docker, Docker Compose, Maven, Swagger/OpenAPI, JWT

## ⚙️ Quick Start Setup

1. **Clone the repository:**

   ```bash
   git clone https://github.com/Kartik12082004/fintech-order-management-system.git
   cd fintech-order-management-system
   ```

2. **Run using Docker Compose (Recommended):**

   Spin up both the PostgreSQL database and the Spring Boot application simultaneously:

   ```bash
   docker-compose up -d --build
   ```

3. **Alternative: Manual Build and Run**

   If you prefer to run the database separately, you can build and run the application using Maven:

   ```bash
   ./mvnw clean package -DskipTests
   java -jar target/MiniOrderManagementSystem-0.0.1-SNAPSHOT.jar
   ```

4. **Access the API Documentation**

   Open your browser and navigate to:

   `http://localhost:8080/swagger-ui.html`
