# Exchange rates application

## Prerequisites
- Java 21
- Docker
- 
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