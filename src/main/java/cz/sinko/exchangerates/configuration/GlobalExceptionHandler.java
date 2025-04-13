package cz.sinko.exchangerates.configuration;

import cz.sinko.exchangerates.api.ApiError;
import cz.sinko.exchangerates.configuration.exception.ResourceNotFoundException;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Global exception handler for the application.
 *
 * @author Radovan Šinko
 */
@SuppressWarnings("PMD") // HttpHeaders implements java.util.Map and thus suggests to use Map instead
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Converts the stack trace of a throwable to a string.
     *
     * @param throwable the throwable to convert
     * @return the stack trace as a string
     */
    public static String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    /**
     * Handles exceptions thrown by the application.
     *
     * @param ex      the exception to handle
     * @param request the web request
     * @return a response entity containing the error details
     */
    @ExceptionHandler({
            ResourceNotFoundException.class,
            MethodArgumentNotValidException.class
    })
    public final ResponseEntity<ApiError> handleException(final Exception ex, final WebRequest request) {
        final HttpHeaders headers = new HttpHeaders();

        if (log.isErrorEnabled()) {
            log.error("Handling {} due to {}", ex.getClass().getSimpleName(), ex.getMessage());
        }

        if (ex instanceof ResourceNotFoundException exception) {
            final HttpStatus status = HttpStatus.NOT_FOUND;
            return handleResourceNotFoundException(exception, headers, status, request);

        } else if (ex instanceof MethodArgumentNotValidException exception) {
            final HttpStatus status = HttpStatus.BAD_REQUEST;
            return handleMethodArgumentNotValidException(exception, headers, status, request);
        } else {
            if (log.isWarnEnabled()) {
                log.warn("Unknown exception type: {}", ex.getClass().getName());
            }

            final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return handleExceptionInternal(ex, null, headers, status, request);
        }
    }

    private ResponseEntity<ApiError> handleResourceNotFoundException(final ResourceNotFoundException ex,
                                                                     final HttpHeaders headers,
                                                                     final HttpStatus status,
                                                                     final WebRequest request) {
        final List<String> errors = Collections.singletonList(ex.getMessage());
        final String stackTrace = getStackTrace(ex);
        return handleExceptionInternal(ex, new ApiError(errors, stackTrace), headers, status, request);
    }

    private ResponseEntity<ApiError> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex,
                                                                           final HttpHeaders headers,
                                                                           final HttpStatus status,
                                                                           final WebRequest request) {
        final List<String> errorMessages = new ArrayList<>();
        for (ObjectError objectError : ex.getAllErrors()) {
            errorMessages.add(objectError.getDefaultMessage());
        }
        return handleExceptionInternal(ex, new ApiError(errorMessages), headers, status, request);
    }

    private ResponseEntity<ApiError> handleExceptionInternal(final Exception ex, @Nullable final ApiError body,
                                                             final HttpHeaders headers, final HttpStatus status,
                                                             final WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }

        return new ResponseEntity<>(body, headers, status);
    }
}
