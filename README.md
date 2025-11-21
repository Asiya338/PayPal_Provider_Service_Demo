# 🟦 PayPal Provider Service — Java + Spring Boot (Microservices Project)

A production-style Spring Boot microservice that integrates with **PayPal REST APIs** to perform secure payment operations such as:

- 🔐 OAuth2 Authentication  
- 💳 Create Order  
- 👀 Show Order  
- ✔️ Capture Order  
- 🚦 Error Handling  
- 🧪 Testing with Postman  
- 📘 Swagger + OpenAPI Documentation  
- 📊 Spring Boot Actuator Monitoring
- Redis Cache for optimized accessToken storage and retreival
- Added Resilience4j retry + circuit breaker

This project is part of my **Java + Spring Boot + Microservices Training Journey** (Sprint 2).

---

## 🧩 Architecture Overview
```
### ✔️ Layered Architecture Used
Your project follows a clean and standard **3-Layer Architecture** at each microservice vlevel:

Controller
Service
Repository
```

```
### ✔️ Standard Package Used
src/main/java
└── com.hulkhiretech.payments
    ├── config                    # Configuration classes
    ├── constants                 # Constant values (PayPal URLs, keys, etc.)
    ├── controller                # REST API controllers
    ├── exception                 # Custom exceptions + global handlers
    ├── http                      # HTTP request/response wrappers
    ├── paypal                    # Parent PayPal package
    │   ├── req                   # PayPal request DTOs
    │   ├── res
    │   │   ├── create            # PayPal Create Order response DTOs
    │   │   ├── capture           # PayPal Capture Order response DTOs
    │   │   ├── show              # PayPal Show Order response DTOs
    │   │   ├── error             # Parent error package
    │   │   │   ├── create        # Error models for Create Order
    │   │   │   ├── capture       # Error models for Capture Order
    │   │   │   └── show          # Error models for Show Order
    ├── pojo                      # Generic POJOs
    ├── service                   # Service layer (Business logic)
    │   ├── helper                # Helper classes used by services
    │   ├── impl                  # Service implementations
    │   └── interfaces            # Service interfaces
    └── util                      # Utility classes
```


Client (UI / Postman)
|
v
PayPal Provider Service ---> PayPal API (Sandbox)
|
Actuator (Health, Metrics)



---

## 🛠️ Tech Stack

| Layer | Technology |
|------|------------|
| Language | Java 17 |
| Framework | Spring Boot |
| REST Client | RestTemplate / WebClient |
| Auth | PayPal OAuth 2.0 |
| Build Tool | Maven |
| Docs | Swagger + OpenAPI |
| Monitoring | Spring Boot Actuator |
| Testing | Postman |

---


## 🔐 PayPal Credentials Setup

Add in `application.properties`:

```properties
paypal.api.base-url=https://api-m.sandbox.paypal.com
paypal.client.id=${PAYPAL_CLIENT_ID}
paypal.client.secret=${PAYPAL_CLIENT_SECRET}
```

Set environment variables before running the app:
```
export PAYPAL_CLIENT_ID=your_client_id
export PAYPAL_CLIENT_SECRET=your_secret
```

🚀 Features Implemented

✔️ 1. OAuth Token Generation

Retrieves OAuth token using PayPal Client ID + Secret

Stores access token (exprires after 9 hrs) for downstream API calls to avoid unnecessary api calls and increase system performance.

✔️ 2. Create Order API

POST /orders

Creates an order

Handles success, failure, invalid data, and PayPal issues
Added Create Order Request validation and handled using custom Exception Handling (@RestControllerAdvice)

✔️ 3. Show Order API

GET /orders/{orderId}

Fetches order details using orderId

Handles success, failure, invalid data, and PayPal issues

✔️ 4. Capture Order API

POST /orders/{orderId}

Captures payment after user approval (for testing, use sandbox test account/card details)

Handles: Success, Failure, Missing/null response, Server errors
-----


📘 Swagger Documentation

Swagger UI:
```
http://localhost:8080/swagger-ui/index.html
```

OpenAPI JSON:
```
http://localhost:8080/v3/api-docs
```

🧪 Postman Testing

During development:

Tested all APIs (Oauth 2.0, createOrder, showOrder, captureOrder)

Verified failure and success cases

Imported Swagger spec into Postman

Debugged HTTP status codes and responses


------------------
📊 Spring Boot Actuator

Enabled endpoints:

/actuator/health

/actuator/metrics

/actuator/loggers

/actuator/env

/actuator/info

/actuator/beans

/actuator/mappings

These helped monitor application readiness and performance.

------------------

Add Redis caching for OAuth token

1️⃣ add dependency, enable cahcing in main class

2️⃣ add cacheable value in TokenService

3️⃣ run redis in docker, now accessToken will be stored in redis

------------------

🎯 How to Run the Application

1️⃣ Set environment variables:
```
PAYPAL_CLIENT_ID=XXXXX
PAYPAL_CLIENT_SECRET=XXXXX
```

2️⃣ Build the project:
```
mvn clean package
```

3️⃣ Run the jar:
```
java -jar target/paypal-provider-service.jar
```

-------------------
Added Resilience4j retry + circuit breaker

Implemented Circuit Breaker mechanism, with statuses CLOSED, OPEN, HALF-OPEN
when api call made to PayPal External System
Added fallbackMethod to handle no response or timeout cases


------------------

📝 Upcoming Enhancements

Add eureka service registry
Add unit tests using JUnit + Mockito
