# Ride Booking System

## Microservices Architecture

### Services
- **Service Registry** (Port 8761) - Eureka Server
- **API Gateway** (Port 8080) - Spring Cloud Gateway
- **Auth Service** (Port 8081) - Authentication & JWT
- **User Service** (Port 8082) - User Management
- **Driver Service** (Port 8083) - Driver Management & Location
- **Ride Service** (Port 8084) - Ride Orchestration
- **Payment Service** (Port 8086) - Payment Processing
- **Notification Service** (Port 8087) - Notifications

## Team Members
- Member 1: [Name] - Infrastructure & Ride Service
- Member 2: [Name] - User & Driver Management
- Member 3: [Name] - Location & Real-time Tracking
- Member 4: [Name] - Payment & Notification

## Tech Stack
- Java 21
- Spring Boot 3.2
- Spring Cloud (Eureka, Gateway, Feign)
- PostgreSQL
- Redis
- Kafka
- Docker

## Getting Started

### Prerequisites
- Java 21+
- Maven 3.8+
- Docker Desktop
- PostgreSQL 15

### Running the application
```bash
# Start infrastructure
docker-compose up -d postgres redis kafka

# Start services in order
./start-services.sh
