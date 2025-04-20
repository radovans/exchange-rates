package cz.sinko.exchangerates.api.dto.response.exchangerate;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * Response object for Time Series Exchange Rates.
 *
 * @author Radovan Šinko
 */
@Data
@Builder
public class TimeSeriesExchangeRatesResponse {

    private String base;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal amount;

    private Map<LocalDate, Map<String, BigDecimal>> rates;
}
