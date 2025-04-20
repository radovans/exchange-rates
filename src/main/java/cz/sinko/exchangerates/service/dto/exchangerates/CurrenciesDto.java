package cz.sinko.exchangerates.service.dto.exchangerates;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * DTO for supported currencies from Frankfurter API.
 *
 * @author Radovan Šinko
 */
@Data
@Builder
public class CurrenciesDto {

    private Map<String, String> currencies;
}
