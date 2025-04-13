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
