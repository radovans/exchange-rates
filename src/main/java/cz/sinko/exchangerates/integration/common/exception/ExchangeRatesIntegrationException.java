package cz.sinko.exchangerates.integration.common.exception;

import cz.sinko.exchangerates.integration.common.ErrorCode;
import lombok.Getter;
import org.springframework.util.Assert;

/**
 * Integration parent exception. Use this exception when you want to define specific exceptional state.
 *
 * @author Radovan Šinko
 */
@Getter
public abstract class ExchangeRatesIntegrationException extends RuntimeException {

    private final ErrorCode error;

    /**
     * One-arg constructor.
     *
     * @param error   the error code
     * @param message exception message
     */
    protected ExchangeRatesIntegrationException(final ErrorCode error, final String message) {
        super(message);

        Assert.notNull(error, "error must not be null");
        this.error = error;
    }

    /**
     * Two-arg constructor.
     *
     * @param error     the error code
     * @param message   exception message
     * @param throwable exception itself
     */
    protected ExchangeRatesIntegrationException(final ErrorCode error,
                                                final String message,
                                                final Throwable throwable) {

        super(message, throwable);

        Assert.notNull(error, "error must not be null");
        this.error = error;
    }
}
