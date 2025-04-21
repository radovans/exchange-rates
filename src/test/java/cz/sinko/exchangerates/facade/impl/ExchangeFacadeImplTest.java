package cz.sinko.exchangerates.facade.impl;

import cz.sinko.exchangerates.facade.ExchangeFacade;
import cz.sinko.exchangerates.service.CacheService;
import cz.sinko.exchangerates.service.ExchangeService;
import cz.sinko.exchangerates.service.dto.exchangerates.CurrenciesDto;
import cz.sinko.exchangerates.service.dto.exchangerates.ExchangeRatesDto;
import cz.sinko.exchangerates.service.dto.exchangerates.TimeSeriesExchangeRateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Implementation of {@link ExchangeFacade}
 *
 * @author Radovan Šinko
 */
class ExchangeFacadeImplTest {

    @Mock
    private ExchangeService exchangeService;

    @Mock
    private CacheService cacheService;

    @InjectMocks
    private ExchangeFacadeImpl exchangeFacade;

    @BeforeEach
    void setUp() {
        exchangeService = mock(ExchangeService.class);
        cacheService = mock(CacheService.class);
        exchangeFacade = new ExchangeFacadeImpl(exchangeService, cacheService);
    }

    @Test
    void testGetLatestRates() {
        final ExchangeRatesDto expected = ExchangeRatesDto.builder().build();
        when(cacheService.getCachedDataOrSave(
                anyString(),
                any(),
                eq(ExchangeRatesDto.class)
        )).thenReturn(expected);

        final ExchangeRatesDto result = exchangeFacade.getLatestRates("USD", "EUR", BigDecimal.TEN);

        assertEquals(expected, result);
        verify(cacheService).getCachedDataOrSave(anyString(), any(), eq(ExchangeRatesDto.class));
    }

    @Test
    void testGetLatestRatesMissCache() {
        final ExchangeRatesDto expected = ExchangeRatesDto.builder().build();

        when(exchangeService.getLatestRates("USD", "EUR", BigDecimal.TEN)).thenReturn(expected);

        when(cacheService.getCachedDataOrSave(
                anyString(),
                any(),
                eq(ExchangeRatesDto.class)
        )).thenAnswer(invocation -> {
            final Supplier<?> supplier = invocation.getArgument(1);
            return supplier.get();
        });

        final ExchangeRatesDto result = exchangeFacade.getLatestRates("USD", "EUR", BigDecimal.TEN);

        assertEquals(expected, result);
        verify(exchangeService).getLatestRates("USD", "EUR", BigDecimal.TEN);
    }

    @Test
    void testGetRatesForDate() {
        final ExchangeRatesDto expected = ExchangeRatesDto.builder().build();
        when(cacheService.getCachedDataOrSave(
                anyString(),
                any(),
                eq(ExchangeRatesDto.class)
        )).thenReturn(expected);

        final LocalDate date = LocalDate.of(2024, 1, 1);
        final ExchangeRatesDto result = exchangeFacade.getRatesForDate(date, "USD", "EUR", BigDecimal.ONE);

        assertEquals(expected, result);
        verify(cacheService).getCachedDataOrSave(anyString(), any(), eq(ExchangeRatesDto.class));
    }

    @Test
    void testGetRatesForDateMissCache() {
        final ExchangeRatesDto expected = ExchangeRatesDto.builder().build();
        final LocalDate date = LocalDate.of(2024, 1, 1);

        when(exchangeService.getRatesForDate(date, "USD", "EUR", BigDecimal.ONE)).thenReturn(expected);

        when(cacheService.getCachedDataOrSave(
                anyString(),
                any(),
                eq(ExchangeRatesDto.class)
        )).thenAnswer(invocation -> {
            final Supplier<?> supplier = invocation.getArgument(1);
            return supplier.get();
        });

        final ExchangeRatesDto result = exchangeFacade.getRatesForDate(date, "USD", "EUR", BigDecimal.ONE);

        assertEquals(expected, result);
        verify(exchangeService).getRatesForDate(date, "USD", "EUR", BigDecimal.ONE);
    }

    @Test
    void testGetTimeSeriesRates() {
        final TimeSeriesExchangeRateDto expected = TimeSeriesExchangeRateDto.builder().build();
        when(cacheService.getCachedDataOrSave(
                anyString(),
                any(),
                eq(TimeSeriesExchangeRateDto.class)
        )).thenReturn(expected);

        final TimeSeriesExchangeRateDto result = exchangeFacade.getTimeSeriesRates(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 10),
                "USD", "EUR", BigDecimal.valueOf(100)
        );

        assertEquals(expected, result);
        verify(cacheService).getCachedDataOrSave(anyString(), any(), eq(TimeSeriesExchangeRateDto.class));
    }


    @Test
    void testGetTimeSeriesRatesMissCache() {
        final TimeSeriesExchangeRateDto expected = TimeSeriesExchangeRateDto.builder().build();
        final LocalDate startDate = LocalDate.of(2024, 1, 1);
        final LocalDate endDate = LocalDate.of(2024, 1, 10);

        when(exchangeService.getTimeSeriesRates(startDate, endDate, "USD", "EUR", BigDecimal.valueOf(100)))
                .thenReturn(expected);

        when(cacheService.getCachedDataOrSave(anyString(), any(), eq(TimeSeriesExchangeRateDto.class)))
                .thenAnswer(invocation -> {
                    final Supplier<?> supplier = invocation.getArgument(1);
                    return supplier.get();
                });

        final TimeSeriesExchangeRateDto result = exchangeFacade.getTimeSeriesRates(
                startDate, endDate, "USD", "EUR", BigDecimal.valueOf(100)
        );

        assertEquals(expected, result);
        verify(exchangeService).getTimeSeriesRates(startDate, endDate, "USD", "EUR", BigDecimal.valueOf(100));
    }

    @Test
    void testGetSupportedCurrencies() {
        final CurrenciesDto expected = CurrenciesDto.builder().build();
        when(cacheService.getCachedDataOrSaveWithTtl(
                anyString(),
                any(),
                eq(CurrenciesDto.class),
                eq(600L)
        )).thenReturn(expected);

        final CurrenciesDto result = exchangeFacade.getSupportedCurrencies();

        assertEquals(expected, result);
        verify(cacheService).getCachedDataOrSaveWithTtl(anyString(), any(), eq(CurrenciesDto.class), eq(600L));
    }

    @Test
    void testGetSupportedCurrenciesMissCache() {
        final CurrenciesDto expected = CurrenciesDto.builder().build();
        when(exchangeService.getSupportedCurrencies()).thenReturn(expected);

        when(cacheService.getCachedDataOrSaveWithTtl(anyString(), any(), eq(CurrenciesDto.class), eq(600L)))
                .thenAnswer(invocation -> {
                    final Supplier<?> supplier = invocation.getArgument(1);
                    return supplier.get();
                });

        final CurrenciesDto result = exchangeFacade.getSupportedCurrencies();

        assertEquals(expected, result);
        verify(exchangeService).getSupportedCurrencies();
    }
}