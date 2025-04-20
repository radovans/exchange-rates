package cz.sinko.exchangerates.service.dto.exchangerates;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * DTO for latest rate from Frankfurter API.
 *
 * @author Radovan Šinko
 */
@Data
@Builder
public class ExchangeRatesDto {

    private String base;

    private LocalDate date;

    private BigDecimal amount;

    private Map<String, BigDecimal> rates;
}
