package cz.sinko.exchangerates.configuration;

import cz.sinko.exchangerates.repository.entity.User;
import io.getunleash.UnleashContext;
import io.getunleash.UnleashContextProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * UnleashConfiguration
 *
 * @author Radovan Šinko
 */
@Configuration
public class UnleashConfiguration {

    public static final String APP_NAME = "exchange-rates";
    public static final String ENVIRONMENT = "development";

    /**
     * UnleashContextProvider
     *
     * @return UnleashContextProvider
     */
    @Bean
    public UnleashContextProvider unleashContextProvider() {
        return () -> {
            final UnleashContext.Builder builder = UnleashContext.builder();
            final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof User user) {
                builder.userId(user.getUsername());
            }

            return builder
                    .appName(APP_NAME)
                    .environment(ENVIRONMENT)
                    .build();
        };
    }
}
