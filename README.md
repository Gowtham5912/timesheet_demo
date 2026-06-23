# Timesheet Management System

A Spring Boot based Timesheet Management System that allows employees to submit timesheets and managers to review, approve, or reject them.

## Features

- User Authentication using Spring Security
- Employee Timesheet Submission
- Manager Approval Workflow
- Audit Logging
- Password Change Enforcement
- Thymeleaf-based User Interface
- PostgreSQL Database Support
- H2 Database for Testing
- Maven Build Management
- Jenkins CI Pipeline Integration

## Technology Stack

### Backend
- Java 17+
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate

### Frontend
- Thymeleaf
- HTML
- CSS
- Bootstrap

### Database
- PostgreSQL
- H2 (Testing)

### Build Tool
- Maven

### CI/CD
- Jenkins

## Project Structure

```text
src/main/java
├── controller
├── service
├── repository
├── entity
├── config

src/main/resources
├── templates
├── static
└── application.properties
```

## Build Project

```bash
mvn clean compile
```

## Run Tests

```bash
mvn test
```

## Package Application

```bash
mvn package
```

Generated artifact:

```text
target/timesheet-0.0.1-SNAPSHOT.jar
```

## Run Application

```bash
java -jar target/timesheet-0.0.1-SNAPSHOT.jar
```

Application URL:

```text
http://localhost:8080
```

## Jenkins Pipeline

The project includes a Jenkins pipeline that performs:

1. Source Code Checkout
2. Build
3. Test
4. Package
5. Artifact Archiving

Pipeline Flow:

```text
GitHub
   ↓
Jenkins
   ↓
Build
   ↓
Test
   ↓
Package
   ↓
Archive Artifact
```

## Author

Gowtham D V Gowda

