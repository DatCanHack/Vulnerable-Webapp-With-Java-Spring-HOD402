# Vulnerable Webapp With Java Spring - HOD402

## Description
This is a sample web application built with Spring Boot for educational purposes in the HOD402 course. The application includes features such as:
- User authentication and authorization (USER, SELLER, ADMIN roles)
- Product management (add, edit, delete, view)
- User management (ADMIN only)
- Several intentionally vulnerable features for security testing practice (SQL Injection, Insecure Deserialization, Path Traversal...)

## Technologies Used
- Java 17+
- Spring Boot
- Spring Security
- Thymeleaf
- JPA (Hibernate)
- MariaDB/MySQL
- Maven

## Installation & Running

### 1. Clone the repository
```bash
git clone https://github.com/L42yH4d3s/Vulnerable-Webapp-With-Java-Spring-HOD402.git
cd Vulnerable-Webapp-With-Java-Spring-HOD402
```

### 2. Configure the database
- Edit the database connection settings in `src/main/resources/application.properties` or `application-dev.properties`/`application-prod.properties` to match your environment.

### 3. Build & run the application
Using the provided Maven Wrapper:
```bash
./mvnw spring-boot:run
```
Or build the jar file:
```bash
./mvnw clean package
java -jar target/*.jar
```

### 4. Access the application
- Default URL: [http://localhost:8080](http://localhost:8080)
- Login with sample accounts (see `data-dev.sql` or `data-prod.sql` if available)

## Main Features
- `/` or `/home`: Home page
- `/products`: View product list
- `/products/manage`: Manage products (SELLER, ADMIN)
- `/users`: Manage users (ADMIN)
- `/login`, `/register`: Login, register

## Security Notice
This application contains known security vulnerabilities for educational and testing purposes. **Do NOT use in production!**

## Contribution
All contributions, bug reports, or improvement ideas are welcome!

## License
[Add license information here if needed] 