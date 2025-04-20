package cz.sinko.exchangerates.facade.impl;

import cz.sinko.exchangerates.facade.ExchangeFacade;
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

    private final ExchangeService exchangeService;

    @Override
    public ExchangeRatesDto getLatestRates(final String base, final String symbols, final BigDecimal amount) {
        log.info("Getting latest rates for base '{}', symbols '{}', amount '{}'", base, symbols, amount);
        return exchangeService.getLatestRates(base, symbols, amount);
    }

    @Override
    public ExchangeRatesDto getRatesForDate(final LocalDate date,
                                            final String base,
                                            final String symbols,
                                            final BigDecimal amount) {

        log.info("Getting rates for date '{}', base '{}', symbols '{}', amount '{}'", date, base, symbols, amount);
        return exchangeService.getRatesForDate(date, base, symbols, amount);
    }

    @Override
    public TimeSeriesExchangeRateDto getTimeSeriesRates(final LocalDate startDate,
                                                        final LocalDate endDate,
                                                        final String base,
                                                        final String symbols,
                                                        final BigDecimal amount) {

        log.info("Getting time series rates for start date '{}', end date '{}', base '{}', symbols '{}', amount '{}'",
                startDate, endDate, base, symbols, amount);
        return exchangeService.getTimeSeriesRates(startDate, endDate, base, symbols, amount);
    }

    @Override
    public CurrenciesDto getSupportedCurrencies() {
        log.info("Getting supported currencies");
        return exchangeService.getSupportedCurrencies();
    }
}
