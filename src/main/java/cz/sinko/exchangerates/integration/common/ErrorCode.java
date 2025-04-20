package cz.sinko.exchangerates.integration.common;

/**
 * Contract for error code definition.
 *
 * @author Radovan Šinko
 */
public interface ErrorCode {

    /**
     * Gets error code.
     *
     * @return error code
     */
    String getErrorCode();

    /**
     * Gets error description. Description is not localized.
     *
     * @return error description
     */
    String getErrorDesc();
}
