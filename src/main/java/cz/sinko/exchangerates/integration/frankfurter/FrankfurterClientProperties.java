package cz.sinko.exchangerates.integration.frankfurter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Frankfurter client configuration properties.
 *
 * @author Radovan Šinko
 */
@Data
@ConfigurationProperties(FrankfurterClientProperties.PREFIX)
public class FrankfurterClientProperties {

    static final String PREFIX = "integration.frankfurter-client";

    private String url;
}
