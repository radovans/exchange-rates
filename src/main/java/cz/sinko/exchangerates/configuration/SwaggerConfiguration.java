package cz.sinko.exchangerates.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger configuration class for the application.
 *
 * @author Radovan Šinko
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Exchange Rates API",
                version = "1.0",
                description = "Exchange Rates API for retrieving and converting currency rates"
        ))
@SecurityScheme(
        name = "BasicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic",
        description = "Basic authentication for API access"
)
public class SwaggerConfiguration {
}
