package cz.sinko.exchangerates.api.mapper;

import cz.sinko.exchangerates.api.dto.response.exchangerate.ExchangeRateResponse;
import cz.sinko.exchangerates.api.dto.response.exchangerate.SupportedCurrenciesResponse;
import cz.sinko.exchangerates.api.dto.response.exchangerate.TimeSeriesExchangeRatesResponse;
import cz.sinko.exchangerates.service.dto.exchangerates.CurrenciesDto;
import cz.sinko.exchangerates.service.dto.exchangerates.ExchangeRatesDto;
import cz.sinko.exchangerates.service.dto.exchangerates.TimeSeriesExchangeRateDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


/**
 * Mapper between ExchangeRates and Request/Response ExchangeRatesDto.
 *
 * @author Radovan Šinko
 */
@Mapper(componentModel = "spring")
public interface ExchangeRatesApiMapper {

    /**
     * Get instance of UserMapper.
     *
     * @return instance of UserMapper
     */
    static ExchangeRatesApiMapper t() {
        return Mappers.getMapper(ExchangeRatesApiMapper.class);
    }

    /**
     * Map ExchangeRatesDto to ExchangeRateResponse.
     *
     * @param source ExchangeRatesDto
     * @return ExchangeRateResponse
     */
    ExchangeRateResponse toResponse(ExchangeRatesDto source);

    /**
     * Map TimeSeriesExchangeRateDto to TimeSeriesExchangeRatesResponse.
     *
     * @param source TimeSeriesExchangeRateDto
     * @return TimeSeriesExchangeRatesResponse
     */
    TimeSeriesExchangeRatesResponse toResponse(TimeSeriesExchangeRateDto source);

    /**
     * Map CurrenciesDto to SupportedCurrenciesResponse.
     *
     * @param source CurrenciesDto
     * @return SupportedCurrenciesResponse
     */
    SupportedCurrenciesResponse toResponse(CurrenciesDto source);
}
