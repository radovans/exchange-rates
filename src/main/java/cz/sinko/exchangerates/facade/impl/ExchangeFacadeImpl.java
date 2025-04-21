package cz.sinko.exchangerates.facade.impl;

import cz.sinko.exchangerates.facade.ExchangeFacade;
import cz.sinko.exchangerates.service.CacheService;
import cz.sinko.exchangerates.service.ExchangeService;
import cz.sinko.exchangerates.service.dto.exchangerates.CurrenciesDto;
import cz.sinko.exchangerates.service.dto.exchangerates.ExchangeRatesDto;
import cz.sinko.exchangerates.service.dto.exchangerates.TimeSeriesExchangeRateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Implementation of {@link ExchangeFacade}
 *
 * @author Radovan Šinko
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeFacadeImpl implements ExchangeFacade {

    private static final String REDIS_KEY_PREFIX = "er-";

    private final ExchangeService exchangeService;

    private final CacheService cacheService;

    @Override
    public ExchangeRatesDto getLatestRates(final String base, final String symbols, final BigDecimal amount) {
        return cacheService.getCachedDataOrSave(
                REDIS_KEY_PREFIX + "latestRates-" + base + "-" + symbols + "-" + amount,
                () -> exchangeService.getLatestRates(base, symbols, amount),
                ExchangeRatesDto.class
        );
    }

    @Override
    public ExchangeRatesDto getRatesForDate(final LocalDate date,
                                            final String base,
                                            final String symbols,
                                            final BigDecimal amount) {

        return cacheService.getCachedDataOrSave(
                REDIS_KEY_PREFIX + "ratesForDate-" + date + "-" + base + "-" + symbols + "-" + amount,
                () -> exchangeService.getRatesForDate(date, base, symbols, amount),
                ExchangeRatesDto.class
        );
    }

    @Override
    public TimeSeriesExchangeRateDto getTimeSeriesRates(final LocalDate startDate,
                                                        final LocalDate endDate,
                                                        final String base,
                                                        final String symbols,
                                                        final BigDecimal amount) {

        return cacheService.getCachedDataOrSave(
                REDIS_KEY_PREFIX + "timeSeriesRates-"
                        + startDate + "-" + endDate + "-" + base + "-" + symbols + "-" + amount,
                () -> exchangeService.getTimeSeriesRates(startDate, endDate, base, symbols, amount),
                TimeSeriesExchangeRateDto.class
        );
    }

    @Override
    public CurrenciesDto getSupportedCurrencies() {
        return cacheService.getCachedDataOrSaveWithTtl(
                REDIS_KEY_PREFIX + "supportedCurrencies",
                exchangeService::getSupportedCurrencies,
                CurrenciesDto.class,
                600L
        );
    }
}
