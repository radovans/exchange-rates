package cz.sinko.exchangerates.api.dto.response.exchangerate;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * Response object for Exchange Rates.
 *
 * @author Radovan Šinko
 */
@Data
@Builder
public class ExchangeRateResponse {

    private String base;

    private LocalDate date;

    private BigDecimal amount;

    private Map<String, BigDecimal> rates;
}
