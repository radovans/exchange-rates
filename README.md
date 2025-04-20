# Exchange rates application

## Prerequisites

- Java 21
- Docker

## How to run

1. Clone the repository
2. Start PostgreSQL database in the background
    ```bash
   docker run -d -p 5401:5432 --name exchange_rates --restart always \
   -e POSTGRES_USER=user \
   -e POSTGRES_PASSWORD=password \
   -e POSTGRES_DB=exchange_rates \
   -v exchange_rates_postgres_data:/var/lib/postgresql/data \
   postgres
    ```
3. Run `mvn clean flyway:migrate` to apply database migrations
4. Run `mvn spring-boot:run` to start the application
5. Access the application at `http://localhost:8080`

## Additional Information

- Run tests with `mvn clean test`
- Run checkstyle with `mvn checkstyle:checkstyle` - report will be generated in `/target/reports/checkstyle.html`
    ```bash
    open target/reports/checkstyle.html
    ```
- Run Jacoco with `mvn jacoco:report` - report will be generated in `/target/site/jacoco/index.html`
    ```bash
    open target/site/jacoco/index.html
    ```
- Run Spotbugs with `mvn spotbugs:spotbugs` - report will be generated in `/target/site/spotbugs.html`
    ```bash
    open target/site/spotbugs.html
    ```
- Run PMD with `mvn pmd:pmd` - report will be generated in `/target/site/pmd.html`
    ```bash
    open target/reports/pmd.html
    ```

- Project contains devtools for hot reload
    - Changes will be automatically reloaded after saving the file and `Ctrl + F9` or `Cmd + F9` in IntelliJ

## Postman collection

- Postman collection is available in the `api-test` directory.
    - Import environment and collections into Postman.