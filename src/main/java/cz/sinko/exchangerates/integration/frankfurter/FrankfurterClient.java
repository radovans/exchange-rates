package cz.sinko.exchangerates.integration.frankfurter;

import cz.sinko.exchangerates.integration.frankfurter.dto.ExchangeRatesResponse;
import cz.sinko.exchangerates.integration.frankfurter.dto.TimeSeriesExchangeRatesResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * Frankfurter client.
 *
 * @author Radovan Šinko
 */
public interface FrankfurterClient {

    /**
     * Get latest exchange rates.
     *
     * @param base    Base currency code (optional).
     * @param symbols Comma-separated list of currency codes to get exchange rates for (optional).
     * @param amount  Amount to convert (optional).
     * @return Latest exchange rates.
     */
    ExchangeRatesResponse getLatestRates(String base, String symbols, BigDecimal amount);

    /**
     * Get historical exchange rates.
     *
     * @param date    Date to get exchange rates for.
     * @param base    Base currency code (optional).
     * @param symbols Comma-separated list of currency codes to get exchange rates for (optional).
     * @param amount  Amount to convert (optional).
     * @return Historical exchange rates.
     */
    ExchangeRatesResponse getRatesForDate(LocalDate date, String base, String symbols, BigDecimal amount);

    /**
     * Get exchange rates for a time period.
     *
     * @param startDate Start date.
     * @param endDate   End date.
     * @param base      Base currency code (optional).
     * @param symbols   Comma-separated list of currency codes to get exchange rates for (optional).
     * @param amount    Amount to convert (optional).
     * @return Exchange rates for a time period.
     */
    TimeSeriesExchangeRatesResponse getTimeSeriesRates(LocalDate startDate,
                                                       LocalDate endDate,
                                                       String base,
                                                       String symbols,
                                                       BigDecimal amount);

    /**
     * Get supported currencies.
     *
     * @return Supported currencies.
     */
    Map<String, String> getSupportedCurrencies();
}
