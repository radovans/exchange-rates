package cz.sinko.exchangerates.configuration;

import cz.sinko.exchangerates.api.ApiError;
import cz.sinko.exchangerates.configuration.exception.AlreadyExistsException;
import cz.sinko.exchangerates.configuration.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Global exception handler for the application.
 *
 * @author Radovan Šinko
 */
@SuppressWarnings("PMD") // HttpHeaders implements java.util.Map and thus suggests to use Map instead
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Map<Class<? extends Exception>, HttpStatus> EXCEPTION_STATUS_MAP = Map.of(
            AccessDeniedException.class, HttpStatus.FORBIDDEN,
            AlreadyExistsException.class, HttpStatus.CONFLICT,
            AuthorizationDeniedException.class, HttpStatus.UNAUTHORIZED,
            HttpRequestMethodNotSupportedException.class, HttpStatus.METHOD_NOT_ALLOWED,
            MethodArgumentNotValidException.class, HttpStatus.BAD_REQUEST,
            ResourceNotFoundException.class, HttpStatus.NOT_FOUND
    );

    /**
     * Handles all exceptions that are not explicitly handled by other exception handlers.
     *
     * @param ex      the exception
     * @param request the web request
     * @return the response entity with the error message
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ApiError> handleException(final Exception ex, final WebRequest request) {
        final HttpHeaders headers = new HttpHeaders();
        final HttpStatus status = EXCEPTION_STATUS_MAP.getOrDefault(ex.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);

        log.error("Handling {} due to {}", ex.getClass().getSimpleName(), ex.getMessage());

        final Function<Exception, List<String>> errorExtractor = getErrorExtractor(ex);
        final List<String> errors = errorExtractor.apply(ex);

        return handleExceptionInternal(ex, new ApiError(errors), headers, status, request);
    }

    private Function<Exception, List<String>> getErrorExtractor(Exception ex) {
        if (ex instanceof MethodArgumentNotValidException) {
            return exception -> {
                final List<String> errors = new ArrayList<>();
                for (ObjectError objectError : ((MethodArgumentNotValidException) exception).getAllErrors()) {
                    errors.add(objectError.getDefaultMessage());
                }
                return errors;
            };
        }
        return exception -> Collections.singletonList(exception.getMessage());
    }

    private ResponseEntity<ApiError> handleExceptionInternal(final Exception ex,
                                                             final ApiError body,
                                                             final HttpHeaders headers,
                                                             final HttpStatus status,
                                                             final WebRequest request) {

        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }
        return new ResponseEntity<>(body, headers, status);
    }
}
