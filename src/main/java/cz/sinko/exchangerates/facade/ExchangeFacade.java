package cz.sinko.exchangerates.facade;

import cz.sinko.exchangerates.service.dto.exchangerates.CurrenciesDto;
import cz.sinko.exchangerates.service.dto.exchangerates.ExchangeRatesDto;
import cz.sinko.exchangerates.service.dto.exchangerates.TimeSeriesExchangeRateDto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Service for getting exchange rates.
 *
 * @author Radovan Šinko
 */
public interface ExchangeFacade {

    /**
     * Get latest exchange rates.
     *
     * @param base    Base currency code (optional).
     * @param symbols Comma-separated list of currency codes to get exchange rates for (optional).
     * @param amount  Amount to convert (optional).
     * @return Latest exchange rates.
     */
    ExchangeRatesDto getLatestRates(String base, String symbols, BigDecimal amount);

    /**
     * Get exchange rates for a specific date.
     *
     * @param date    Date to get exchange rates for.
     * @param base    Base currency code (optional).
     * @param symbols Comma-separated list of currency codes to get exchange rates for (optional).
     * @param amount  Amount to convert (optional).
     * @return Exchange rates for the specified date.
     */
    ExchangeRatesDto getRatesForDate(LocalDate date, String base, String symbols, BigDecimal amount);

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
    TimeSeriesExchangeRateDto getTimeSeriesRates(LocalDate startDate,
                                                 LocalDate endDate,
                                                 String base,
                                                 String symbols,
                                                 BigDecimal amount);

    /**
     * Get supported currencies.
     *
     * @return Supported currencies.
     */
    CurrenciesDto getSupportedCurrencies();
}
