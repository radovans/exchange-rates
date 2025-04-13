package cz.sinko.exchangerates.configuration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception when field is not unique.
 *
 * @author Radovan Šinko
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyExistsException extends Exception {

    private final String resourceName;

    private final String message;

    /**
     * Create AlreadyExistsException with resourceName and message.
     *
     * @param resourceName name of the resource
     * @param message      message
     */
    public AlreadyExistsException(final String resourceName, final String message) {
        this.resourceName = resourceName;
        this.message = message;
    }

    /**
     * Create AlreadyExistsException with resourceName and message.
     *
     * @param resourceName name of the resource
     * @param message      message
     * @return ResourceNotFoundException
     */
    public static AlreadyExistsException createWith(final String resourceName, final String message) {
        return new AlreadyExistsException(resourceName, message);
    }

    /**
     * Get exception message.
     *
     * @return message
     */
    @Override
    public String getMessage() {
        return String.format("%s %s", resourceName, message);
    }
}
