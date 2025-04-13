package cz.sinko.exchangerates;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for Exchange Rates application.
 *
 * @author Radovan Šinko
 */
@SuppressWarnings("PMD")
@SpringBootApplication
public class ExchangeRatesApplication {

    /**
     * Main method to run the Exchange Rates application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(ExchangeRatesApplication.class, args);
    }
}
