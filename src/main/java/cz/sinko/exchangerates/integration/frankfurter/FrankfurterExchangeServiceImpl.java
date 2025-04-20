package cz.sinko.exchangerates.integration.frankfurter;

import cz.sinko.exchangerates.integration.frankfurter.dto.ExchangeRatesResponse;
import cz.sinko.exchangerates.integration.frankfurter.dto.TimeSeriesExchangeRatesResponse;
import cz.sinko.exchangerates.service.ExchangeService;
import cz.sinko.exchangerates.service.dto.exchangerates.CurrenciesDto;
import cz.sinko.exchangerates.service.dto.exchangerates.ExchangeRatesDto;
import cz.sinko.exchangerates.service.dto.exchangerates.TimeSeriesExchangeRateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * Implementation of {@link ExchangeService}
 *
 * @author Radovan Šinko
 */
@Service
@RequiredArgsConstructor
public class FrankfurterExchangeServiceImpl implements ExchangeService {

    private final FrankfurterClient frankfurterClient;

    @Override
    public ExchangeRatesDto getLatestRates(final String base, final String symbols, final BigDecimal amount) {
        final ExchangeRatesResponse latestRates = frankfurterClient.getLatestRates(base, symbols, amount);
        return ExchangeRatesDto.builder()
                .base(latestRates.getBase())
                .date(latestRates.getDate())
                .amount(latestRates.getAmount())
                .rates(latestRates.getRates())
                .build();
    }

    @Override
    public ExchangeRatesDto getRatesForDate(final LocalDate date,
                                            final String base,
                                            final String symbols,
                                            final BigDecimal amount) {

        final ExchangeRatesResponse historicalRates = frankfurterClient.getRatesForDate(date, base, symbols, amount);
        return ExchangeRatesDto.builder()
                .base(historicalRates.getBase())
                .date(historicalRates.getDate())
                .amount(historicalRates.getAmount())
                .rates(historicalRates.getRates())
                .build();
    }

    @Override
    public TimeSeriesExchangeRateDto getTimeSeriesRates(final LocalDate startDate,
                                                        final LocalDate endDate,
                                                        final String base,
                                                        final String symbols,
                                                        final BigDecimal amount) {

        final TimeSeriesExchangeRatesResponse timeSeriesRates =
                frankfurterClient.getTimeSeriesRates(startDate, endDate, base, symbols, amount);
        return TimeSeriesExchangeRateDto.builder()
                .base(timeSeriesRates.getBase())
                .startDate(timeSeriesRates.getStartDate())
                .endDate(timeSeriesRates.getEndDate())
                .amount(timeSeriesRates.getAmount())
                .rates(timeSeriesRates.getRates())
                .build();
    }

    @Override
    public CurrenciesDto getSupportedCurrencies() {
        final Map<String, String> supportedCurrencies = frankfurterClient.getSupportedCurrencies();
        return CurrenciesDto.builder()
                .currencies(supportedCurrencies)
                .build();
    }
}
