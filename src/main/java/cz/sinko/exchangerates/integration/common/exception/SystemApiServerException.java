package cz.sinko.exchangerates.integration.common.exception;

import cz.sinko.exchangerates.integration.common.ErrorCode;

/**
 * Specific exception raised when call at SystemApiClient failed on 5xx.
 *
 * @author Radovan Šinko
 */
public class SystemApiServerException extends ExchangeRatesIntegrationException {

    /**
     * SystemApiServerException.
     *
     * @param error   error code
     * @param message error message
     */
    public SystemApiServerException(final ErrorCode error, final String message) {
        super(error, message);
    }
}
