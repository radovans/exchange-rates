package cz.sinko.exchangerates.integration.common;

/**
 * Types of HTTP BAD_REQUEST (400) error response.
 *
 * @author Radovan Šinko
 */
public enum BadRequestType implements ErrorCode {

    INVALID_REQUEST;

    @Override
    public String getErrorCode() {
        return name();
    }

    @Override
    public String getErrorDesc() {
        return name();
    }
}
