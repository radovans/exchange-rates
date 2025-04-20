package cz.sinko.exchangerates.integration.common;

/**
 * Types of HTTP INTERNAL SERVER ERROR (500) error response.
 *
 * @author Radovan Šinko
 */
public enum ServerErrorRequestType implements ErrorCode {

    INVALID_REQUEST,
    SERVICE_UNAVAILABLE;

    @Override
    public String getErrorCode() {
        return name();
    }

    @Override
    public String getErrorDesc() {
        return name();
    }
}
