# ⏱️ Time Tracking System

A simple, role-based time tracking application for small teams or consultancies, built with **Spring Boot 3** and **Thymeleaf**. Workers can log hours, Admins approve entries and manage data, and Accountants generate salary and invoice summaries—all using a user-friendly web interface backed by an in-memory H2 database.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Quick Start](#quick-start)
- [Sample Accounts](#sample-accounts)
- [How to Use](#how-to-use)
- [Database Access](#database-access)
- [Project Structure](#project-structure)

---

## Project Overview

This Time Tracking System was built to let organizations easily record, approve, and analyze work hours per project and per worker, with clear roles for Admins, Workers, and Accountants. No external dependencies, no Docker, no Swagger—just pure Spring Boot.

---

## Features

| Role          | Permissions and Features                                                         |
|---------------|----------------------------------------------------------------------------------|
| **Admin**     | - Login / Logout <br> - CRUD workers & projects <br> - Approve work logs <br> - View summaries per worker/project <br> - Export reports (CSV/Excel placeholder) |
| **Worker**    | - Login / Logout <br> - Submit work log (date, hours, project, comment, optional photo) <br> - View own entries |
| **Accountant**| - Login / Logout <br> - View all work logs <br> - Generate salary & invoice summaries per worker/project/client |

---

## Tech Stack

- **Language:** Java 17
- **Framework:** Spring Boot 3.1 (MVC, JPA, Security)
- **Templating:** Thymeleaf 3
- **Database:** H2 (in-memory, with optional file mode)
- **Build:** Maven
- **Styling:** [Pico.css](https://picocss.com/) via CDN

> _No Docker, Swagger, Keycloak, or external dependencies. Testing not included as per requirements._

---

## Quick Start

1. **Clone the repo**

    ```bash
    git clone https://github.com/your-org/time-tracking.git
    cd time-tracking
    ```

2. **Run the application**

    - With Maven Wrapper:
      ```bash
      ./mvnw spring-boot:run
      ```
    - Or with Maven:
      ```bash
      mvn spring-boot:run
      ```
    - Or build & run jar:
      ```bash
      mvn clean package
      java -jar target/time-tracking-*.jar
      ```

3. **Access the web app:**  
   Open [http://localhost:8080](http://localhost:8080) in your browser.

---

## Sample Accounts

> Copy-paste these credentials to try the app!

| Role        | Username    | Password   |
|-------------|-------------|------------|
| Admin       | admin       | admin123   |
| Worker      | worker1     | work123    |
| Accountant  | accountant  | acc123     |

---

## How to Use

### Admin

- Login as **admin**.
- Go to **Workers** to add, edit, or deactivate workers.
- Go to **Projects** to create and manage projects.
- Approve or reject submitted work logs.
- View summaries and export reports.

### Worker

- Login as **worker1**.
- Submit daily work logs (date, hours, project, comments, photo optional).
- View and review your own entries.

### Accountant

- Login as **accountant**.
- View all work logs.
- Generate salary summaries (based on hourly rates).
- Generate invoice summaries per project or client.

---

## Database Access

- **H2 Console:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- **JDBC URL:** `jdbc:h2:mem:testdb`
- **User:** `sa` (no password)
- Note: The database resets on restart.

---

## Project Structure

- `/src/main/java/.../entity` — JPA Entities
- `/src/main/java/.../controller` — Controllers for web endpoints
- `/src/main/java/.../repository` — Spring Data JPA repositories
- `/src/main/resources/templates` — Thymeleaf templates
- `/src/main/resources/static` — CSS/images
- `/src/main/resources/application.properties` — Configuration

---


