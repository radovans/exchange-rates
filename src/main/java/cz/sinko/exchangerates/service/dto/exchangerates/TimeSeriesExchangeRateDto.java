package cz.sinko.exchangerates.service.dto.exchangerates;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * DTO for time series data from Frankfurter API.
 *
 * @author Radovan Šinko
 */
@Data
@Builder
public class TimeSeriesExchangeRateDto {

    private String base;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal amount;

    private Map<LocalDate, Map<String, BigDecimal>> rates;
}
