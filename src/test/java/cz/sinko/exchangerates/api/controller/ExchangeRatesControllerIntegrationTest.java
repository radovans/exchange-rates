package cz.sinko.exchangerates.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExchangeRatesControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @SuppressWarnings("PMD") // HttpHeaders implements java.util.Map and thus suggests to use Map instead
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "Password123");
        return headers;
    }

    @Test
    void testGetLatestRates() {
        HttpEntity<Void> entity = new HttpEntity<>(getHeaders());

        String url = String.format("http://localhost:%d/rates/latest?base=USD&symbols=EUR,GBP&amount=100", port);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("USD", Objects.requireNonNull(response.getBody()).get("base"));
        assertNotNull(response.getBody().get("rates"));
    }

    @Test
    void testGetRatesForDate() {
        HttpEntity<Void> entity = new HttpEntity<>(getHeaders());

        String url = String.format("http://localhost:%d/rates/historical?date=2023-01-01&base=USD&symbols=EUR," +
                "GBP&amount=100", port);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("USD", Objects.requireNonNull(response.getBody()).get("base"));
        assertNotNull(response.getBody().get("rates"));
    }

    @Test
    void testGetTimeSeriesRates() {
        HttpEntity<Void> entity = new HttpEntity<>(getHeaders());

        String url = String.format("http://localhost:%d/rates/timeseries?startDate=2023-01-01&endDate=2023-01-10&base" +
                "=USD&symbols=EUR,GBP&amount=100", port);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("USD", Objects.requireNonNull(response.getBody()).get("base"));
        assertNotNull(response.getBody().get("rates"));
    }

    @Test
    void testGetSupportedCurrencies() {
        HttpEntity<Void> entity = new HttpEntity<>(getHeaders());

        String url = String.format("http://localhost:%d/rates/currencies", port);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(Objects.requireNonNull(response.getBody()).get("currencies"));
    }
}
