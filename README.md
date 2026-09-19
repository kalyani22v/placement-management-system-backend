# Smart Campus Placement Management System

##  Live Demo

**Swagger API Documentation:**
https://campus-placement-eligibility-management.onrender.com/swagger-ui/index.html#/

> **Note:** The live demo is deployed on Render and may take a few seconds to wake up if the service is inactive.

## Overview

Smart Campus Placement Management System is a Spring Boot based web application designed to automate and simplify the campus placement process. The system manages student records, company recruitment criteria, and eligibility mapping between students and companies.

The application helps placement coordinators efficiently manage placement activities by maintaining centralized student and company data while automatically identifying eligible candidates based on predefined criteria.

---

## Features

### Student Management

* Add new students
* Update student details
* View all students
* Retrieve student eligibility information
* Email uniqueness validation

### Company Management

* Add new companies
* Update company recruitment criteria
* View all companies
* Delete companies

### Eligibility Management

* Automatic student-company mapping
* Eligibility based on:

  * Branch criteria
  * CGPA criteria
  * Backlog criteria
* View companies eligible for a specific student

### API Documentation

* Swagger/OpenAPI integration for API testing and documentation

---

## Technology Stack

### Backend

* Java 17
* Spring Boot
* Spring Data JPA
* Hibernate

### Database

* MySQL

### API Documentation

* Swagger OpenAPI

### Build Tool

* Maven

---

## Project Architecture

```text
Controller Layer
        |
        v
Service Layer
        |
        v
Repository Layer
        |
        v
MySQL Database
```

### Layers

#### Controller Layer

Handles incoming HTTP requests and returns responses.

#### Service Layer

Contains business logic such as:

* Student registration
* Company registration
* Eligibility calculation
* Student-company mapping

#### Repository Layer

Handles database operations using Spring Data JPA.

#### Database Layer

Stores:

* Students
* Companies
* Student-Company Mapping

---

## Database Design

### Student

| Field         | Type    |
| ------------- | ------- |
| id            | Integer |
| name          | String  |
| branch        | String  |
| cgpa          | Float   |
| no_of_backlog | Integer |
| email         | String  |

### Company

| Field           | Type    |
| --------------- | ------- |
| id              | Integer |
| name            | String  |
| branchcriteria  | String  |
| cgpacriteria    | Float   |
| backlogcriteria | Integer |

### Student-Company Mapping

Many-to-Many relationship between students and companies.

---

## REST APIs

### Student APIs

#### Add Student

```http
POST /campus/addstd
```

#### Get All Students

```http
GET /campus/getstd
```

#### Update Student

```http
PUT /campus/updatestd/{email}
```

#### Get Eligible Companies for Student

```http
GET /campus/std/{id}
```

### Company APIs

#### Add Company

```http
POST /campus/addcompany
```

#### Get All Companies

```http
GET /campus/getcompany
```

#### Update Company

```http
PUT /campus/updatecomp/{name}
```

#### Delete Company

```http
DELETE /campus/dltcomp/{name}
```

---

## Eligibility Criteria

A student is considered eligible for a company when:

1. Student branch matches company branch criteria.
2. Student CGPA is greater than or equal to company CGPA requirement.
3. Student backlog count satisfies company backlog criteria.

Eligible companies are automatically associated with students during registration and updates.

---

## Setup Instructions

### Clone Repository

```bash
git clone <repository-url>
```

### Navigate to Project

```bash
cd SmartCampus
```

### Configure Database

Update the database configuration in:

```properties
application.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/smartcampus
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
```

### Build Project

```bash
mvn clean install
```

### Run Application

```bash
mvn spring-boot:run
```

Application will start on:

```text
http://localhost:8080
```

---

## Swagger Documentation

Access Swagger UI locally:

```text
http://localhost:8080/swagger-ui/index.html
```

### Live Swagger

```text
https://campus-placement-eligibility-management.onrender.com/swagger-ui/index.html#/
```

---

## Project Screenshot

<img width="1366" height="728" alt="Smart Campus Placement Management System" src="https://github.com/user-attachments/assets/56b16af9-e9a4-4389-948a-4d5210a25323" />
