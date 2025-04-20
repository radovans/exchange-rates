# About

## Project description

This project is a RESTful API for exchange rates. It allows users to manage their accounts, view exchange rates, and
perform currency conversions. The application is built using Spring Boot and most commonly used Spring modules.

## Code quality

- Checkstyle to enforce coding standards.
- JaCoCo for test coverage.
- PMD and Spotbugs for static analysis.

## Project features

- Global exception handler for custom error responses.
- ADMIN and USER roles for different access levels.
- CustomAuthenticationEntryPoint to handle authentication errors.
- Authenticated users can change only their own data. They cannot change their roles.
- Admin users can change any user's data.
- @UniqueUsername validation for username.
- User can update only selected fields (PATCH method).
- Flyway for database migrations.
- Logging into json. Separate log file for errors.
- Each log contains traceId and requestId for tracking requests.
- Integration calls are logged with request and response.
- Swagger documentation for API endpoints.

## API documentation
- Swagger UI is available at `http://localhost:8080/swagger`.
    ```bash
    open http://localhost:8080/swagger
    ```

## Build information and status page
- Build information is available at `http://localhost:8080/actuator/info`
    ```bash
    open http://localhost:8080/actuator/info
    ```
- Open status page and check if the application is running on the servers
    ```bash
    open status-page/status-page.html
    ```
- Metrics are available in raw form at `http://localhost:8080/actuator/metrics` (Prometheus)