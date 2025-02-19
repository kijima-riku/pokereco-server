# Spring Boot Server-side Project

## 🚀 Overview
This is a server-side project built using **Spring Boot**. The project is containerized with **Docker Compose**, which sets up a PostgreSQL database and **pgAdmin** for database management.

## 📦 Setup & Running the Project

### 1️⃣ Start Database Containers
The project includes a `docker-compose.yml` file at the top level. To start the required services (**PostgreSQL & pgAdmin**), run:

```sh
docker-compose up --build -d
```

### 2️⃣ Verify Containers are Running
Check if the containers are running using:

```sh
docker ps
```

### 3️⃣ Access pgAdmin
Once the containers are running, you can access **pgAdmin** at:

🔗 **[http://localhost:8081](http://localhost:8081)**

The login credentials are specified in `docker-compose.yml`.

---

## 🛠️ Running the Spring Boot Application

### 1️⃣ Build & Run the Application
This project uses **Gradle** as the build tool. To start the Spring Boot application, navigate to the project root and run:

```sh
./gradlew bootRun
```

### 2️⃣ Apply Database Migrations
The project uses **Flyway** for database migrations. To apply the migrations, run:

```sh
./gradlew flywayMigrate
```