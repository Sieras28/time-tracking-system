# ⏱️ Time Tracking System

A simple **Spring Boot 3 + Thymeleaf** web application that lets _Workers_ log their hours, _Admins_ approve them and manage data, and _Accountants_ generate salary & invoice summaries – all backed by an in-memory **H2** database.

---

## Table of Contents
1. [Features](#features)
2. [Tech Stack](#tech-stack)
3. [Quick Start](#quick-start)
4. [Running](#running)
5. [Sample Accounts](#sample-accounts)
6. [Project Structure](#project-structure)
7. [Database](#database)
8. [How It Works](#how-it-works)
9. [Commit History Plan](#commit-history-plan)
10. [Roadmap](#roadmap)
11. [Contributing](#contributing)
12. [License](#license)

---

## Features
| Role        | What they can do                                                                      |
|-------------|---------------------------------------------------------------------------------------|
| **Admin**   | • Login / Logout<br>• CRUD Workers & Projects<br>• Approve / Reject work logs<br>• Summaries by worker / project<br>• Export reports (CSV/Excel placeholder) |
| **Worker**  | • Login / Logout<br>• Submit work log (date, hours, project, comment, optional photo)<br>• View their own entries |
| **Accountant** | • Login / Logout<br>• View all work logs<br>• **Generate salary summaries** based on each worker’s hourly rate<br>• **Generate invoice summaries** per client / project |

---

## Tech Stack
| Layer              | Choice / Version |
|--------------------|------------------|
| Language           | Java 17 |
| Framework          | Spring Boot 3.1 (Spring MVC + Data JPA + Security) |
| Template Engine    | Thymeleaf 3 |
| Database           | H2 in-memory (file mode optional) |
| Build Tool         | Maven |
| Styling            | [Pico.css](https://picocss.com/) (cdn) |
| Testing            | _**Not included by project brief**_ |
| Containerisation   | _None (no Docker by brief)_ |

---

## Quick Start

```bash
# clone
git clone https://github.com/your-org/time-tracking.git
cd time-tracking

# run in dev mode
./mvnw spring-boot:run
# or build a fat jar
./mvnw clean package
java -jar target/time-tracking-*.jar
