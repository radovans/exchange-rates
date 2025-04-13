package cz.sinko.exchangerates.configuration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception when resource is not found.
 *
 * @author Radovan Šinko
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends Exception {

    private final String resourceName;

    private final String message;

    /**
     * Create ResourceNotFoundException with resourceName and message.
     *
     * @param resourceName name of the resource
     * @param message      message
     */
    public ResourceNotFoundException(final String resourceName, final String message) {
        this.resourceName = resourceName;
        this.message = message;
    }

    /**
     * Create ResourceNotFoundException with resourceName and message.
     *
     * @param resourceName name of the resource
     * @param message      message
     * @return ResourceNotFoundException
     */
    public static ResourceNotFoundException createWith(final String resourceName, final String message) {
        return new ResourceNotFoundException(resourceName, message);
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
