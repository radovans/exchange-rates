# Presentation

## Code quality
- Checkstyle to enforce coding standards.
- JaCoCo for test coverage.
- PMD and Spotbugs for static analysis.

## Phase 2 improvements
- Global exception handler for custom error responses.

## Phase 3 improvements
- ADMIN and USER roles for different access levels.
- CustomAuthenticationEntryPoint to handle authentication errors.
- Authenticated users can change only their own data. They cannot change their roles.
- Admin users can change any user's data.
- @UniqueUsername validation for username.
- User can update only selected fields (PATCH method).

## Phase 4 improvements
- Flyway for database migrations.
- Swagger UI documentation for the API.
- Logging with Logback in JSON format. (could be improved by rotating the logs etc.)
- Frankfurter API integration for exchange rates.

## Improvements which are not implemented
- Increase test coverage and test edge cases (e.g. user cannot change username, only admin can change role)
