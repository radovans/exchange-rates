package cz.sinko.exchangerates.integration.frankfurter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class TimeSeriesExchangeRatesResponse {

    private String base;

    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("end_date")
    private LocalDate endDate;

    private BigDecimal amount;

    private Map<LocalDate, Map<String, BigDecimal>> rates;
}
