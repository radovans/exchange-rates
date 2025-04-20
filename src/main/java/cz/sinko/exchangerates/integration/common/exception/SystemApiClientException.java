package cz.sinko.exchangerates.integration.common.exception;

import cz.sinko.exchangerates.integration.common.ErrorCode;

/**
 * Specific exception raised when call at SystemApiClient failed on 4xx.
 *
 * @author Radovan Šinko
 */
public class SystemApiClientException extends ExchangeRatesIntegrationException {

    /**
     * SystemApiClientException.
     *
     * @param error   error code
     * @param message error message
     */
    public SystemApiClientException(final ErrorCode error, final String message) {
        super(error, message);
    }

    /**
     * SystemApiClientException.
     *
     * @param error     error code
     * @param message   error message
     * @param throwable exception
     */
    public SystemApiClientException(final ErrorCode error, final String message, final Throwable throwable) {
        super(error, message, throwable);
    }
}
