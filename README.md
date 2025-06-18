# 🌍 Travel Management System

## 📖 Description

A comprehensive travel management system that integrates tour booking, customer management, and online payment processing into one unified platform.

---
## mimi demo
https://github.com/user-attachments/assets/52d5526d-bd0e-4531-a3ee-3d3ccf45020f

## ✨ Key Features

* 🗜️ Tour listing, flight booking with datacom api, and management
* 🔐 User authentication and authorization via OAuth2
* 💳 Integrated online payments with Stripe
* 📧 Email notifications and confirmations (Spring Mail)
* 📱 SMS alerts using Twilio
* ⚡ Redis-based caching for performance optimization
* 📚 API documentation with Swagger / OpenAPI

---

## 🧱 Project Architecture

```
travel-management-system/
├── travelcore/              # Backend API (Spring Boot)
├── travelui/                # Frontend SPA (Angular)
└── datacom_fake_server/     # Mock server for testing
```

---

## 🛠️ Technologies Overview

| Backend – `travelcore`         | Frontend – `travelui` |
| ------------------------------ | --------------------- |
| Java 17                        | Angular 10.1.0        |
| Spring Boot 3.4.4              | Angular Material      |
| Spring Security, OAuth2, JWT   | RxJS 6.6.0            |
| Spring Data JPA                | TypeScript 4.0.2      |
| Spring Data Redis              | Stripe.js 7.3.0       |
| Spring Mail                    | FontAwesome 6.7.2     |
| Spring WebFlux                 |                       |
| Oracle Database (Free Edition) |                       |
| Redis 6.0.7                    |                       |
| Lombok, Maven                  |                       |

---
## 🗄️ Database

* **Main DB**: Oracle Database (Free Edition)
* **Caching**: Redis
* **Ports**:

  * Oracle: `1522`
  * Redis: `6379`

---

## 🧰 Development Tools

* Docker & Docker Compose
* RedisInsight 1.14.0
* MailDev (SMTP Server for testing)

---

## ⚙️ Setup Instructions

### Requirements

* Java 17
* Node.js + npm
* Docker & Docker Compose
* Maven

| Run Backend          | Run Frontend |
| -------------------- | ------------ |
| \`\`\`bash           | \`\`\`bash   |
| cd travelcore        | cd travelui  |
| mvn clean install    | npm install  |
| mvn spring-boot\:run | ng serve     |
| \`\`\`               | \`\`\`       |

---

## ▶️ Run with Docker Compose

```bash
docker-compose up -d
```

---

## 🔌 Default Ports

| Service            | Port |
| ------------------ | ---- |
| Backend API        | 8080 |
| Frontend (Angular) | 4200 |
| Oracle DB          | 1522 |
| Redis              | 6379 |
| RedisInsight       | 8001 |
| MailDev (Web UI)   | 1080 |
| MailDev (SMTP)     | 1025 |

---

## 📞 Contact

For more details or contribution inquiries, please contact me.
