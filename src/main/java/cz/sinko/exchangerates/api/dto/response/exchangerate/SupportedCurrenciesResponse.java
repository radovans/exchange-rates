package cz.sinko.exchangerates.api.dto.response.exchangerate;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * Response object for Supported Currencies.
 *
 * @author Radovan Šinko
 */
@Data
@Builder
public class SupportedCurrenciesResponse {

    private Map<String, String> currencies;
}
